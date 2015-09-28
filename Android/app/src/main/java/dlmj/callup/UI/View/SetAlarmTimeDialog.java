package dlmj.callup.UI.View;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dlmj.callup.BroadcastReceiver.AlarmReceiver;
import dlmj.callup.BusinessLogic.Alarm.CalendarHelper;
import dlmj.callup.BusinessLogic.Cache.AlarmCache;
import dlmj.callup.BusinessLogic.Network.AlarmLoadHelper;
import dlmj.callup.BusinessLogic.Network.NetworkHelper;
import dlmj.callup.Common.Factory.DialogFactory;
import dlmj.callup.Common.Factory.FragmentFactory;
import dlmj.callup.Common.Interfaces.ChangeFragmentListener;
import dlmj.callup.Common.Interfaces.DialogListener;
import dlmj.callup.Common.Interfaces.OnDownloadChangedListener;
import dlmj.callup.Common.Interfaces.UIDataListener;
import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Model.AlarmTime;
import dlmj.callup.Common.Model.Bean;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Params.UrlParams;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.Common.Util.StringUtil;
import dlmj.callup.R;

/**
 * Created by Two on 15/9/4.
 */
public class SetAlarmTimeDialog extends Dialog {
    private static String TAG = "SetAlarmTimeDialog";
    private TimePicker mTimePicker;
    private Button mSubmitButton;
    private Button mCancelButton;
    private DialogListener mDialogListener;
    private Button mEverydayButton;
    private Button mWeekdaysButton;
    private Button mWeekendsButton;
    private ProgressBar mLoadProgressBar;
    private Calendar mCalendar = Calendar.getInstance();
    private View.OnClickListener mDayChosenListener;
    private int[] mDayResult = new int[7];
    private List<Button> mEveryDayPackage = new LinkedList<>();
    private List<Button> mWeekDaysPackage = new LinkedList<>();
    private List<Button> mWeekendsPackage = new LinkedList<>();
    private int mCurrentSceneId;
    private int mAlarmId;
    private String mTime;
    private ChangeFragmentListener mChangeFragmentListener;
    private NetworkHelper mAddAlarmNetworkHelper;
    private NetworkHelper mEditAlarmNetworkHelper;
    private int mProgressBarStatus;
    private AlarmLoadHelper mAlarmLoadHelper;
    private Dialog mProgressDialog;

    public SetAlarmTimeDialog(Context context) {
        super(context, R.style.AlertDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_time);
        initializeData();
        findView();
        setListener();
    }

    private void initializeData() {
        mAddAlarmNetworkHelper = new NetworkHelper(this.getContext());
        mEditAlarmNetworkHelper = new NetworkHelper(this.getContext());
        mAlarmLoadHelper = AlarmLoadHelper.getInstance(this.getContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeWeekDayPackage();
        checkDayMatchRepeatPackage();
        setTimePicker();
        setButtonBack();
    }

    private void findView() {
        mTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        mSubmitButton = (Button) findViewById(R.id.submitButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);
        mEverydayButton = (Button) findViewById(R.id.everydayButton);
        mWeekdaysButton = (Button) findViewById(R.id.weekdaysButton);
        mWeekendsButton = (Button) findViewById(R.id.weekendsButton);
        mLoadProgressBar = (ProgressBar) findViewById(R.id.loadProgressBar);
        mLoadProgressBar.setMax(100);
        mLoadProgressBar.setProgress(0);
        mProgressDialog = DialogFactory.getProgressDialog(getContext());
    }

    public void setAlarmInfo(int sceneId) {
        mCurrentSceneId = sceneId;
        mTime = new AlarmTime().toString();
    }

    public void setAlarmInfo(int sceneId, String time, String frequent, int alarmId) {
        mCurrentSceneId = sceneId;
        mTime = time;
        mDayResult = StringUtil.StringToIntArray(frequent);
        mAlarmId = alarmId;
    }

    private void setTimePicker() {
        AlarmTime alarmTime = new AlarmTime(mTime);
        mTimePicker.setCurrentHour(alarmTime.getHour());
        mTimePicker.setCurrentMinute(alarmTime.getMinute());
    }

    private void setListener() {
        mAlarmLoadHelper.setOnChangeListener(new OnDownloadChangedListener() {
            @Override
            public void onChanged() {
                mProgressBarStatus = mAlarmLoadHelper.getDownloadedContent() * 100 /
                        mAlarmLoadHelper.getMaxContent();
                LogUtil.d(TAG, "ProgressBar status is " + mProgressBarStatus);
                mLoadProgressBar.setProgress(mProgressBarStatus);
            }

            @Override
            public void onFinished() {
                mDialogListener.closeDialog();
                mChangeFragmentListener.ChangeFragment(FragmentFactory.FragmentName.SetAlarm);

                Alarm alarm = mAlarmLoadHelper.getAlarm();
                Intent intent = new Intent(getContext(), AlarmReceiver.class);
                intent.putExtra(IntentExtraParams.ALARM, alarm);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),
                        alarm.getSceneId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mCalendar = CalendarHelper.getInstance().getNextCalendar(alarm);
                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(),
                        pendingIntent);
            }
        });

        mAddAlarmNetworkHelper.setUiDataListener(new UIDataListener<Bean>() {
            @Override
            public void onDataChanged(Bean data) {
                try {
                    mProgressDialog.cancel();
                    JSONObject result = new JSONObject(data.getResult());
                    String alarmInfoStr = result.getString("alarminfo");
                    JSONObject alarmInfo = new JSONObject(alarmInfoStr);
                    Alarm alarm = new Alarm(alarmInfo.getInt("id"),
                            alarmInfo.getInt("Scene_id"),
                            alarmInfo.getString("Logo"),
                            alarmInfo.getString("Picture"),
                            alarmInfo.getString("Name"),
                            alarmInfo.getString("Time"),
                            alarmInfo.getString("frequent_type"),
                            alarmInfo.getString("Audio"));
                    AlarmCache.getInstance().addItem(alarm);

                    mAlarmLoadHelper.loadAlarmInfo(alarm);

                } catch (JSONException e) {
                    LogUtil.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onErrorHappened(int errorCode, String errorMessage) {
                mProgressDialog.cancel();
            }
        });

        mEditAlarmNetworkHelper.setUiDataListener(new UIDataListener<Bean>() {
            @Override
            public void onDataChanged(Bean data) {
                LogUtil.d(TAG, "Success to update the alarm info");
                AlarmCache.getInstance().updateAlarm(mAlarmId, mTime);
                Alarm alarm = AlarmCache.getInstance().getAlarm(mAlarmId);

                mAlarmLoadHelper.loadAlarmInfo(alarm);

                mProgressDialog.cancel();
            }

            @Override
            public void onErrorHappened(int errorCode, String errorMessage) {
                mProgressDialog.cancel();
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultStr = "";
                for (int day : mDayResult) {
                    resultStr += day;
                }

                AlarmTime alarmTime = new AlarmTime(mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE));
                mTime = alarmTime.toString();

                Map<String, String> params = new HashMap<>();

                if (mAlarmId == 0) {
                    params.put("Time", mTime);
                    params.put("Scene_id", mCurrentSceneId + "");
                    params.put("frequent_type", resultStr);
                    mAddAlarmNetworkHelper.sendPostRequest(UrlParams.CREATE_ALARM_URL, params);
                } else {
                    params.put("id", mAlarmId + "");
                    params.put("key", "Time");
                    params.put("val", mTime);
                    mEditAlarmNetworkHelper.sendPostRequest(UrlParams.Edit_ALARM_URL, params);
                }

                mProgressDialog.show();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogListener.closeDialog();
            }
        });

        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
            }
        });

        View.OnClickListener repeatChosenClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearRepeatTypeBackground();
                clearDayBackground();
                if (view.getTag() != null && (Boolean) view.getTag()) {
                    view.setBackgroundResource(R.drawable.cancel_btn);
                    view.setTag(false);
                } else {
                    view.setTag(true);
                    setCurrentRepeatType(view.getId());
                    view.setBackgroundResource(R.drawable.submit_btn);
                }
            }
        };

        mDayChosenListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag() != null && (Boolean) view.getTag()) {
                    view.setTag(false);
                    setCurrentDay(((Button) view).getText().toString(), false);
                    view.setBackgroundResource(R.drawable.cancel_btn);
                } else {
                    view.setTag(true);
                    setCurrentDay(((Button) view).getText().toString(), true);
                    view.setBackgroundResource(R.drawable.submit_btn);
                }
                checkDayMatchRepeatPackage();
            }
        };

        mEverydayButton.setOnClickListener(repeatChosenClickListener);
        mWeekdaysButton.setOnClickListener(repeatChosenClickListener);
        mWeekendsButton.setOnClickListener(repeatChosenClickListener);
    }

    public void setChangeFragmentListener(ChangeFragmentListener changeFragmentListener) {
        mChangeFragmentListener = changeFragmentListener;
    }

    private void setCurrentRepeatType(int id) {
        switch (id) {
            case R.id.everydayButton:
                setWeekDayPackage(mEveryDayPackage);
                break;
            case R.id.weekdaysButton:
                setWeekDayPackage(mWeekDaysPackage);
                break;
            case R.id.weekendsButton:
                setWeekDayPackage(mWeekendsPackage);
                break;
            default:
                break;
        }
    }

    private void initializeWeekDayPackage() {
        Button monButton = (Button) findViewById(R.id.MonButton);
        Button tueButton = (Button) findViewById(R.id.TueButton);
        Button wedButton = (Button) findViewById(R.id.WedButton);
        Button thurButton = (Button) findViewById(R.id.ThurButton);
        Button friButton = (Button) findViewById(R.id.FriButton);
        Button satButton = (Button) findViewById(R.id.SatButton);
        Button sunButton = (Button) findViewById(R.id.SunButton);

        mWeekendsPackage.clear();
        mWeekDaysPackage.clear();
        mEveryDayPackage.clear();
        mWeekendsPackage.add(satButton);
        mWeekendsPackage.add(sunButton);
        mWeekDaysPackage.add(monButton);
        mWeekDaysPackage.add(tueButton);
        mWeekDaysPackage.add(wedButton);
        mWeekDaysPackage.add(thurButton);
        mWeekDaysPackage.add(friButton);
        mEveryDayPackage.addAll(mWeekDaysPackage);
        mEveryDayPackage.addAll(mWeekendsPackage);

        for (Button button : mEveryDayPackage) {
            button.setOnClickListener(mDayChosenListener);
        }
    }

    public void setDialogListener(DialogListener dialogListener) {
        mDialogListener = dialogListener;
    }

    private void setWeekDayPackage(List<Button> weekDayPackage) {
        for (Button button : weekDayPackage) {
            button.setBackgroundResource(R.drawable.submit_btn);
            button.setTag(true);
            setCurrentDay(button.getText().toString(), true);
        }
    }

    private void setCurrentDay(String dayStr, boolean ifSelected) {
        int day = Integer.parseInt(dayStr) - 1;
        mDayResult[day] = ifSelected ? 1 : 0;
    }

    public void clearRepeatTypeBackground() {
        mEverydayButton.setBackgroundResource(R.drawable.cancel_btn);
        mWeekdaysButton.setBackgroundResource(R.drawable.cancel_btn);
        mWeekendsButton.setBackgroundResource(R.drawable.cancel_btn);
        mEverydayButton.setTag(false);
        mWeekdaysButton.setTag(false);
        mWeekendsButton.setTag(false);
    }

    public void clearDayBackground() {
        for (Button button : mEveryDayPackage) {
            button.setBackgroundResource(R.drawable.cancel_btn);
            button.setTag(false);
        }
    }

    private void setButtonBack() {
        clearDayBackground();
        for (int i = 0; i < mDayResult.length; i++) {
            if (mDayResult[i] == 1) {
                mEveryDayPackage.get(i).setBackgroundResource(R.drawable.submit_btn);
            }
        }
    }

    private void checkDayMatchRepeatPackage() {
        clearRepeatTypeBackground();
        String resultStr = "";

        for (int day : mDayResult) {
            resultStr += day;
        }

        Log.d("resultStr", resultStr);

        switch (resultStr) {
            case "1111111":
                mEverydayButton.setBackgroundResource(R.drawable.submit_btn);
                mEverydayButton.setTag(true);
                break;
            case "1111100":
                mWeekdaysButton.setBackgroundResource(R.drawable.submit_btn);
                mWeekdaysButton.setTag(true);
                break;
            case "0000011":
                mWeekendsButton.setBackgroundResource(R.drawable.submit_btn);
                mWeekendsButton.setTag(true);
                break;
            default:
                break;

        }
    }
}
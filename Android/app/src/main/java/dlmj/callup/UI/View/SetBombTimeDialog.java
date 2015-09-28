package dlmj.callup.UI.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dlmj.callup.Common.Interfaces.AddHistoryListener;
import dlmj.callup.Common.Interfaces.DialogListener;
import dlmj.callup.R;

/**
 * Created by Two on 15/9/25.
 */
public class SetBombTimeDialog extends Dialog {
    private Button mSubmitButton;
    private Button mCancelButton;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private DialogListener mDialogListener;
    private AddHistoryListener mAddHistoryListener;
    private int mSceneId;
    private Calendar mCalendar;

    public SetBombTimeDialog(Context context) {
        super(context, R.style.AlertDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_bomb_time);
        findView();
        setListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCalendar = Calendar.getInstance();
        mTimePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
        mTimePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        mDatePicker.updateDate(mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public void setDialogListener(DialogListener dialogListener) {
        mDialogListener = dialogListener;
    }

    public void setAddHistoryListener(AddHistoryListener addHistoryListener) {
        mAddHistoryListener = addHistoryListener;
    }

    private void setListener() {
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                mCalendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
                mCalendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());
                Date date = mCalendar.getTime();
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mAddHistoryListener.addHistory(mSceneId, dateFormat.format(date));
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogListener.closeDialog();
            }
        });
    }

    private void findView() {
        mSubmitButton = (Button) findViewById(R.id.submitButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);
        mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
    }

    public void setSceneInfo(int sceneId) {
        mSceneId = sceneId;
    }
}

package dlmj.callup.BusinessLogic.Alarm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;

import dlmj.callup.Common.Interfaces.ChangeVibrationTimeListener;
import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.Service.AlarmService;
import dlmj.callup.UI.Activity.SelfSave.AlarmActivity;

/**
 * Created by Two on 15/9/22.
 */
public class AlarmHelper {
    private final static int SWING_TIME = 5;
    private static String TAG = "AlarmHelper";
    private int mSwingTime = 0;
    public SensorEventListener mSensorEventListener;
    public ChangeVibrationTimeListener mChangeVibrationTimeListener;
    private SensorManager mSensorManager;
    private Context mContext;
    private Intent mServiceIntent;
    private static AlarmHelper mInstance;
    Vibrator mVibrator = null;
    private Alarm mAlarm;
    private boolean mIfFromBomb;
    private Handler mHandler;

    public static AlarmHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AlarmHelper(context);
        }
        return mInstance;
    }

    public AlarmHelper(Context context) {
        mContext = context;
        Initialize();
        setListener();
    }

    private void Initialize() {
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mServiceIntent = new Intent(mContext, AlarmService.class);
        mVibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
        mSwingTime = 0;
    }

    private void setListener() {
        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                int sensorType = event.sensor.getType();
                float[] values = event.values;

                if (mSwingTime > SWING_TIME) {
                    unregisterListener();
                    if (mIfFromBomb) {
                        mChangeVibrationTimeListener.showCancelBombDialog();
                    } else {
                        stopAudio();
                    }
                }

                if (sensorType == Sensor.TYPE_ACCELEROMETER) {
                    //values[0]:X，values[1]：Y，values[2]：Z
                    onVibrationStart(values[0], values[1], values[2]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void onVibrationStart(float x, float y, float z) {
        if ((Math.abs(x) > 17 || Math.abs(y) > 17 || Math
                .abs(z) > 17)) {
            LogUtil.d(TAG, "Sensor X: " + x);
            LogUtil.d(TAG, "Sensor Y: " + y);
            LogUtil.d(TAG, "Sensor Z: " + z);
            LogUtil.d(TAG, "Swing time: " + mSwingTime);
            mVibrator.vibrate(500);

            if (mSwingTime < SWING_TIME) {
                Intent intent = new Intent(mContext, AlarmActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                mChangeVibrationTimeListener.onChanged(mSwingTime);
            }

            mSwingTime++;
        }
    }

    public void setChangeVibrationTimeListener(ChangeVibrationTimeListener changeVibrationTimeListener) {
        mChangeVibrationTimeListener = changeVibrationTimeListener;
    }

    public void setAlarm(Alarm alarm, boolean ifFromBomb) {
        mAlarm = alarm;
        mSwingTime = 0;
        startAction(AlarmService.ACTION_PLAY);
        mIfFromBomb = ifFromBomb;
    }

    public Alarm getAlarm() {
        return mAlarm;
    }

    private void startAction(String action) {
        mServiceIntent.putExtra(IntentExtraParams.ALARM, mAlarm);
        mServiceIntent.setAction(action);

        mContext.startService(mServiceIntent);
    }

    public void unregisterListener() {
        mSensorManager.unregisterListener(mSensorEventListener);
    }

    public void registerListener() {
        mSensorManager.registerListener(mSensorEventListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopAudio() {
        startAction(AlarmService.ACTION_STOP);
        mContext.stopService(mServiceIntent);
        if(mHandler != null) {
            Message message = new Message();
            message.what = AlarmActivity.FINISHED;
            mHandler.sendMessage(message);
        }
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }
}

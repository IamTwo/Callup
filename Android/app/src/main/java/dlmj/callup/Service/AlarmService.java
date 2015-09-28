package dlmj.callup.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import dlmj.callup.BusinessLogic.Alarm.AlarmPlayerEngine;
import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Util.LogUtil;

/**
 * Created by Two on 15/8/28.
 */
public class AlarmService extends Service {
    private static final String TAG = "AlarmService";
    public static final String ACTION_PLAY = "Play";
    public static final String ACTION_STOP = "Stop";
    private AlarmPlayerEngine mAlarmPlayerEngine;
    private Alarm mAlarm;
    private Thread mThread;
    private boolean ifThreadRun = false;

   @Override
    public void onCreate() {
        super.onCreate();
        mAlarmPlayerEngine = new AlarmPlayerEngine(this);
        mThread = new Thread(alarmTask);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "AlarmService is started.");
        if(intent != null){
            LogUtil.d(TAG, "Receive command.");
            String action = intent.getAction();
            mAlarm = (Alarm)intent.getSerializableExtra(IntentExtraParams.ALARM);

            if (action.equals(ACTION_PLAY)) {
                if(!ifThreadRun){
                    mThread.start();
                    ifThreadRun = true;
                }
            } else if (action.equals(ACTION_STOP)) {
                mAlarmPlayerEngine.stop();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    Runnable alarmTask = new Runnable() {
        @Override
        public void run() {
            mAlarmPlayerEngine.updateAlarm(mAlarm);
            mAlarmPlayerEngine.play();
            LogUtil.d(TAG, "Start to play audio");
        }
    };

    @Override
    public void onDestroy(){
        mAlarmPlayerEngine.stop();
        LogUtil.d(TAG, "Stop audio");
    }
}

package dlmj.callup.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import dlmj.callup.BusinessLogic.AlarmPlayerEngine;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Model.Scene;

/**
 * Created by Two on 15/8/28.
 */
public class AlarmService extends Service {
    public static final String ACTION_PLAY = "Play";
    public static final String ACTION_STOP = "Stop";
    private AlarmPlayerEngine mAlarmPlayerEngine;
    private Scene mScene;
    private Thread mThread;
    private volatile boolean mExit = false;
    private boolean ifThreadRun = false;

   @Override
    public void onCreate() {
        super.onCreate();
        mThread = new Thread(alarmTask);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String action = intent.getAction();
            mScene = (Scene)intent.getSerializableExtra(IntentExtraParams.SCENE);

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
            mAlarmPlayerEngine = new AlarmPlayerEngine(getApplicationContext());
            mAlarmPlayerEngine.updateScene(mScene);
            mAlarmPlayerEngine.play();
        }
    };

    @Override
    public void onDestroy(){
        mAlarmPlayerEngine.stop();
    }
}

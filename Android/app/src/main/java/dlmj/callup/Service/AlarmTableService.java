package dlmj.callup.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import dlmj.callup.BusinessLogic.Cache.AlarmCache;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.DataAccess.AlarmTableManager;

/**
 * Created by Two on 15/9/29.
 */
public class AlarmTableService extends Service {
    public final static String TAG = "AlarmTableService";
    public final static String DELETE = "Delete";
    public final static String SAVE = "Save";
    public final static String CLEAR = "Clear";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "AlarmTableService is started.");
        if (intent != null) {
            LogUtil.d(TAG, "Receive command.");
            String action = intent.getAction();
            AlarmTableManager alarmTableManager = AlarmTableManager.getInstance(this);

            if (action.equals(SAVE)) {
                alarmTableManager.saveAlarms(AlarmCache.getInstance(this).getList());
            } else if (action.equals(DELETE)) {
                int alarmId = intent.getIntExtra(IntentExtraParams.ALARM_ID, 0);
                alarmTableManager.removeAlarm(alarmId);
            } else if (action.equals(CLEAR)) {
                alarmTableManager.clear();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
}

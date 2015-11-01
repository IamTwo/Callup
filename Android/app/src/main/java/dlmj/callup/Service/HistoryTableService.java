package dlmj.callup.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.DataAccess.AlarmTableManager;

/**
 * Created by Two on 15/9/29.
 */
public class HistoryTableService extends Service {
    private final static String TAG = "HistoryTableService";
    public final static String CLEAR = "Clear";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "HistoryTableService is started.");
        if (intent != null) {
            LogUtil.d(TAG, "Receive command.");
            String action = intent.getAction();
            AlarmTableManager alarmTableManager = AlarmTableManager.getInstance(this);

            if (action.equals(CLEAR)) {
                alarmTableManager.clear();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
}

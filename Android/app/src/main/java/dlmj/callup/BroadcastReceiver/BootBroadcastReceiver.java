package dlmj.callup.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.DataAccess.AlarmTableManager;

/**
 * Created by Two on 15/9/7.
 */
public class BootBroadcastReceiver extends BroadcastReceiver{
    static final String action_boot = "android.intent.action.Boot_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(action_boot)){
            AlarmTableManager alarmTableManager = new AlarmTableManager(context);

            List<Alarm> alarms = alarmTableManager.getAlarms();
        }
    }
}

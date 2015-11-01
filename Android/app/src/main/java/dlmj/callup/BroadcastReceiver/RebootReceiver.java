package dlmj.callup.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import dlmj.callup.BusinessLogic.Alarm.AlarmSetManager;
import dlmj.callup.BusinessLogic.Cache.AlarmCache;
import dlmj.callup.Common.Model.Alarm;

/**
 * Created by Two on 15/9/28.
 */
public class RebootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_REBOOT.equals(action)) {
            List<Alarm> alarmList = AlarmCache.getInstance(context).getList();
            for(Alarm alarm : alarmList) {
                AlarmSetManager.setAlarm(context, alarm);
            }
        }

    }
}

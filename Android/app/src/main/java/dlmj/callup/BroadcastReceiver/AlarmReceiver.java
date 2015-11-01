package dlmj.callup.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import dlmj.callup.BusinessLogic.Alarm.AlarmSetManager;
import dlmj.callup.BusinessLogic.Cache.AlarmCache;
import dlmj.callup.BusinessLogic.Network.NetworkHelper;
import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Params.UrlParams;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.UI.Activity.SelfSave.AlarmActivity;

/**
 * Created by Two on 15/8/26.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private final static String TAG = "AlarmReceiver";
    private NetworkHelper mDeleteAlarmNetworkHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        mDeleteAlarmNetworkHelper = new NetworkHelper(context);
        Alarm alarm = (Alarm) intent.getSerializableExtra(IntentExtraParams.ALARM);
        Intent newIntent = new Intent(context, AlarmActivity.class);
        LogUtil.d(TAG, "Alarm Info: " + alarm.getAudioUrl());

        if (alarm.isRepeat()) {
            AlarmSetManager.setAlarm(context, alarm);
        } else {
            AlarmCache.getInstance(context).removeAlarm(alarm.getAlarmId());
            Map<String, String> params = new HashMap<>();
            params.put("id", alarm.getAlarmId()+ "");
            mDeleteAlarmNetworkHelper.sendPostRequest(UrlParams.DELETE_ALARM_URL, params);
        }
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        newIntent.putExtra(IntentExtraParams.ALARM, alarm);
        context.startActivity(newIntent);
    }
}

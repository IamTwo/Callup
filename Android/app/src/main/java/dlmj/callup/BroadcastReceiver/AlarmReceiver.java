package dlmj.callup.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.UI.Activity.SelfSave.AlarmActivity;

/**
 * Created by Two on 15/8/26.
 */
public class AlarmReceiver extends BroadcastReceiver{
    private final static String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Alarm alarm = (Alarm)intent.getSerializableExtra(IntentExtraParams.ALARM);
        Intent newIntent = new Intent(context, AlarmActivity.class);
        LogUtil.d(TAG, "Alarm Info: " + alarm.getAudioUrl());
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        newIntent.putExtra(IntentExtraParams.ALARM, alarm);
        context.startActivity(newIntent);
    }
}

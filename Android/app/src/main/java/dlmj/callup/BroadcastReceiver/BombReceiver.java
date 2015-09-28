package dlmj.callup.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Model.Friend;
import dlmj.callup.Common.Model.Scene;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.UI.Activity.SelfSave.AlarmActivity;

/**
 * Created by Two on 15/9/27.
 */
public class BombReceiver extends BroadcastReceiver {
    private final static String TAG = "BombReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Scene scene = (Scene)intent.getSerializableExtra(IntentExtraParams.SCENE);
        Friend friend = (Friend) intent.getSerializableExtra(IntentExtraParams.FRIEND);
        String time = intent.getStringExtra(IntentExtraParams.TIME);
        Intent newIntent = new Intent(context, AlarmActivity.class);
        newIntent.putExtra(IntentExtraParams.ALARM, new Alarm(0,
                scene.getSceneId(), scene.getLogoUrl(), scene.getImageUrl(),
                scene.getName(), time, "0000000", scene.getAudioUrl()));
        newIntent.putExtra(IntentExtraParams.FRIEND, friend);
        newIntent.putExtra(IntentExtraParams.FROM, "BombReceiver");
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }
}

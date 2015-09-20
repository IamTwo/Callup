package dlmj.callup.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Model.Scene;
import dlmj.callup.UI.Activity.SelfSave.AlarmActivity;

/**
 * Created by Two on 15/8/26.
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Scene scene = (Scene)intent.getSerializableExtra(IntentExtraParams.SCENE);
        Intent newIntent = new Intent(context, AlarmActivity.class);
        Log.d("hehe", scene.getImageUrl());
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        newIntent.putExtra(IntentExtraParams.SCENE, scene);
        context.startActivity(newIntent);
//        context.unregisterReceiver(this);
    }
}

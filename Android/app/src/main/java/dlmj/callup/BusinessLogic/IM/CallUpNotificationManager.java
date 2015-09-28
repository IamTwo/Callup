package dlmj.callup.BusinessLogic.IM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import dlmj.callup.Common.Model.Friend;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Util.BaseInfoUtil;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.Common.Util.NotificationUtil;
import dlmj.callup.R;
import dlmj.callup.UI.Activity.Account.IntroduceActivity;
import dlmj.callup.UI.Activity.FriendBomb.HistoryActivity;

/**
 * Created by Two on 15/9/8.
 */
public class CallUpNotificationManager {
    private static CallUpNotificationManager mInstance;
    private Context mContext;

    public static CallUpNotificationManager getInstance() {
        if(mInstance == null) {
            mInstance = new CallUpNotificationManager(BaseInfoUtil.getContext());
        }

        return mInstance;
    }

    private CallUpNotificationManager(Context context){
        mContext = context;
    }

    private void cancel() {
        NotificationManager notificationManager = (NotificationManager) BaseInfoUtil
                .getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager == null) {
            return;
        }

        notificationManager.cancel(0);
    }

    public void forceCancelNotification() {
        cancel();
    }

    public void showCustomNewMessageNotification(Context context, Friend friend,
                                                 String sessionId, int lastMessageType) {
        LogUtil.w("showCustomNewMessageNotification fromUserName: " + friend.getName()
                + " ,sessionId: " + sessionId + " ,msgType: " + lastMessageType);
        Intent intent = new Intent(mContext, HistoryActivity.class);
        intent.putExtra(IntentExtraParams.FRIEND, friend);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 35, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = NotificationUtil.buildNotification(context,
                R.drawable.ic_launcher, friend.getName(),
                mContext.getString(R.string.send_a_bomb), pendingIntent);
        notification.flags = (Notification.FLAG_AUTO_CANCEL | notification.flags);
        ((NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(friend.getUserId(), notification);
    }
}

package dlmj.callup.BusinessLogic.IM;

import android.app.LauncherActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import dlmj.callup.Common.Util.BaseInfoUtil;
import dlmj.callup.Common.Util.LogUtil;

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

    public void showCustomNewMessageNotification(Context context, String pushContent,
                                                 String fromUserName, String sessionId,
                                                 int lastMessageType) {
        LogUtil.w("showCustomNewMessageNotification pushContent： " + pushContent
                        + ", fromUserName: " + fromUserName + " ,sessionId: "
                        + sessionId + " ,msgType: " + lastMessageType);

        Intent intent = new Intent(mContext, LauncherActivity.class);
//        intent.putExtra("notification_type", "pushcontent_notification");
//        intent.putExtra("Intro_Is_Muti_Talker", true);
//        intent.putExtra("Main_FromUserName", fromUserName);
//        intent.putExtra("Main_Session", sessionId);
//        intent.putExtra("MainUI_User_Last_Msg_Type", lastMsgType);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}

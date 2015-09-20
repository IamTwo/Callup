package dlmj.callup.BusinessLogic.IM;

import android.app.NotificationManager;

import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

import java.util.ArrayList;
import java.util.List;

import dlmj.callup.Common.Model.Contact;
import dlmj.callup.Common.Util.BaseInfoUtil;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.DataAccess.ContactTableManager;

/**
 * Created by Two on 15/9/8.
 */
public class IMChattingHelper implements OnChatReceiveListener {
    private static final String TAG = "IMChattingHelper";
    private OnMessageReportCallback mOnMessageReportCallback;
    private static IMChattingHelper mInstance;

    public static IMChattingHelper getInstance(){
        if(mInstance == null) {
            mInstance = new IMChattingHelper();
        }
        return mInstance;
    }

    /**
     * Get new instance message and attached files.
     * @param ecMessage
     */
    @Override
    public void OnReceivedMessage(ECMessage ecMessage) {
        LogUtil.d(TAG, "[OnReceivedMessage] show notice true" + ecMessage.getBody());
        if(ecMessage == null) {
            return ;
        }
        postReceiveMessage(ecMessage, true);
    }

    @Override
    public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage ecGroupNoticeMessage) {

    }

    @Override
    public void onOfflineMessageCount(int i) {

    }

    @Override
    public int onGetOfflineMessage() {
        return 0;
    }

    @Override
    public void onReceiveOfflineMessage(List<ECMessage> ecMessages) {

    }

    @Override
    public void onReceiveOfflineMessageCompletion() {

    }

    @Override
    public void onServicePersonVersion(int i) {

    }

    @Override
    public void onReceiveDeskMessage(ECMessage ecMessage) {

    }

    @Override
    public void onSoftVersion(String s, int i) {

    }

    public interface OnMessageReportCallback{
        void onMessageReport(ECError error ,ECMessage message);
        void onPushMessage(String sessionId ,List<ECMessage> messages);
    }

    private synchronized void postReceiveMessage(ECMessage ecMessage, boolean showNotice) {
        if(mOnMessageReportCallback != null) {
            ArrayList<ECMessage> messages = new ArrayList<ECMessage>();
            messages.add(ecMessage);
            mOnMessageReportCallback.onPushMessage(ecMessage.getSessionId(), messages);
        }

        // 是否状态栏提示
        if(showNotice)
            showNotification(ecMessage);
    }

    private static void showNotification(ECMessage ecMessage) {
        CallUpNotificationManager.getInstance().forceCancelNotification();
        String lastMessage = "";
        if(ecMessage.getType() == ECMessage.Type.TXT) {
            lastMessage = ((ECTextMessageBody) ecMessage.getBody()).getMessage();
        }

        Contact contact = ContactTableManager.getContact(ecMessage.getForm());
        if(contact == null) {
            return;
        }

        CallUpNotificationManager.getInstance().showCustomNewMessageNotification(
                BaseInfoUtil.getContext(),
                lastMessage,
                contact.getNickName(),
                ecMessage.getSessionId(),
                ecMessage.getType().ordinal());
    }
}

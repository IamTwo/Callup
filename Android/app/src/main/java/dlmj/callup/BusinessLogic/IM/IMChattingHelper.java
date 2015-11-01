package dlmj.callup.BusinessLogic.IM;

import android.content.Context;

import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDeskManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

import java.util.ArrayList;
import java.util.List;

import dlmj.callup.BusinessLogic.Alarm.AlarmHelper;
import dlmj.callup.BusinessLogic.Alarm.AlarmSetManager;
import dlmj.callup.BusinessLogic.Cache.ConversationCache;
import dlmj.callup.BusinessLogic.Cache.FriendCache;
import dlmj.callup.BusinessLogic.Cache.HistoryCache;
import dlmj.callup.BusinessLogic.Cache.UserCache;
import dlmj.callup.Common.Interfaces.OnMessageReportCallback;
import dlmj.callup.Common.Model.Conversation;
import dlmj.callup.Common.Model.Friend;
import dlmj.callup.Common.Model.History;
import dlmj.callup.Common.Util.BaseInfoUtil;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.R;

/**
 * Created by Two on 15/9/8.
 */
public class IMChattingHelper {
    private static final String TAG = "IMChattingHelper";
    private OnMessageReportCallback mOnMessageReportCallback;
    private OnChatReceiveListener mOnChatReceiverListener;
    private ECChatManager.OnSendMessageListener mOnSendMessageListener;
    private static IMChattingHelper mInstance;
    private ECMessage mOfflineMsg = null;
    private Context mContext;

    public static IMChattingHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new IMChattingHelper(context);
        }
        return mInstance;
    }

    public IMChattingHelper(Context context) {
        setListener();
        mContext = context;
    }

    private void setListener() {
        mOnSendMessageListener = new ECDeskManager.OnSendDeskMessageListener() {
            @Override
            public void onSendMessageComplete(ECError ecError, ECMessage ecMessage) {
                if (mOnMessageReportCallback != null) {
                    mOnMessageReportCallback.onMessageReport(ecError, ecMessage);
                }
            }

            @Override
            public void onComplete(ECError ecError) {

            }

            @Override
            public void onProgress(String s, int i, int i2) {

            }
        };

        mOnChatReceiverListener = new OnChatReceiveListener() {
            @Override
            public void OnReceivedMessage(ECMessage ecMessage) {
                LogUtil.d(TAG, "[OnReceivedMessage] show notice true" + ecMessage.getBody());
                if (ecMessage == null) {
                    return;
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
                LogUtil.d(TAG , "[onReceiveOfflineMessage] show notice false");
                for(ECMessage msg : ecMessages) {
                    mOfflineMsg = msg;
                    postReceiveMessage(msg, false);
                }
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
        };
    }

    public void setOnMessageReportCallback(OnMessageReportCallback onMessageReportCallback) {
        mOnMessageReportCallback = onMessageReportCallback;
    }

    private synchronized void postReceiveMessage(ECMessage ecMessage, boolean showNotice) {
        int userId = Integer.parseInt(ecMessage.getSessionId());
        Friend friend = FriendCache.getInstance(mContext).getFriend(userId);
        String message = ((ECTextMessageBody)ecMessage.getBody()).getMessage();
        History history = new History(message, userId, mContext);
        HistoryCache.getInstance(mContext).updateHistory(history, friend);
        ConversationCache.getInstance().updateConversation(new Conversation(
                userId,
                String.format(mContext.getString(R.string.send_bomb_message), history.getTime())));
        if(history.getStatus() == History.accepted) {
            AlarmSetManager.setAlarm(mContext, history, friend);
        } else if(history.getStatus() == History.failed) {
            AlarmHelper.getInstance(mContext).stopAudio();
        }
        LogUtil.d(TAG, ecMessage.getBody().toString());
        if (mOnMessageReportCallback != null) {
            ArrayList<ECMessage> messages = new ArrayList<>();
            messages.add(ecMessage);
            mOnMessageReportCallback.onPushMessage();
        }

        // 是否状态栏提示
        if (showNotice)
            showNotification(ecMessage);
    }

    private void showNotification(ECMessage ecMessage) {
        CallUpNotificationManager.getInstance().forceCancelNotification();

        int userId = Integer.parseInt(ecMessage.getForm());
        Friend friend = FriendCache.getInstance(mContext).getFriend(userId);
        if (friend == null) {
            return;
        }

        CallUpNotificationManager.getInstance().showCustomNewMessageNotification(
                BaseInfoUtil.getContext(),
                friend,
                ecMessage.getSessionId(),
                ecMessage.getType().ordinal());
    }

    public void sendMessage(final Friend friend, History history) {
        try {
            ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
            msg.setForm(UserCache.getInstance().getClientUser().getUserId() + "");
            msg.setMsgTime(System.currentTimeMillis());

            msg.setTo(friend.getUserId() + "");
            msg.setSessionId(friend.getUserId() + "");
            msg.setDirection(ECMessage.Direction.SEND);

            String content = history.getScene().getSceneId() + "/" + history.getTime() + "/" +
                    history.getStatus();
            ECTextMessageBody msgBody = new ECTextMessageBody(content);
            LogUtil.d(TAG, "The content is " + content);
            msg.setBody(msgBody);
            ECChatManager manager = ECDevice.getECChatManager();
            manager.sendMessage(msg, mOnSendMessageListener);
        } catch (Exception e) {
            LogUtil.e(TAG, "send message fail , e=" + e.getMessage());
        }
    }

    public OnChatReceiveListener getOnChatReceiverListener() {
        return mOnChatReceiverListener;
    }
}

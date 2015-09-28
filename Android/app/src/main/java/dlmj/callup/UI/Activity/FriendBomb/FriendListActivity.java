package dlmj.callup.UI.Activity.FriendBomb;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dlmj.callup.BusinessLogic.Network.NetworkHelper;
import dlmj.callup.Common.Interfaces.UIDataListener;
import dlmj.callup.Common.Model.Bean;
import dlmj.callup.Common.Model.Friend;
import dlmj.callup.Common.Params.UrlParams;
import dlmj.callup.R;
import dlmj.callup.UI.Adapter.FriendAdapter;

/**
 * Created by Two on 15/9/8.
 */
public class FriendListActivity extends Activity implements UIDataListener<Bean> {
    private ListView mFriendListView;
    private NetworkHelper mNetworkHelper;
    Map<String, String> mParams = new HashMap<>();
    private List<Friend> mFriendList = new LinkedList<>();
    private FriendAdapter mFriendAdapter;

    @Override
    public void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
//        mRightDrawer.setContentView(R.layout.friend_bomb);
        initializeData();
        findView();
        setListener();

        ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
        msg.setForm("12345");
        msg.setMsgTime(System.currentTimeMillis());

        msg.setTo("12345");
        msg.setSessionId("12345");
        msg.setDirection(ECMessage.Direction.SEND);

        ECTextMessageBody msgBody = new ECTextMessageBody("haha");
        msg.setBody(msgBody);

        ECChatManager manager = ECDevice.getECChatManager();
        manager.sendMessage(msg, new ECChatManager.OnSendMessageListener() {
            @Override
            public void onSendMessageComplete(ECError error, ECMessage message) {
                // 处理消息发送结果
                if(message == null) {
                    return ;
                }
                // 将发送的消息更新到本地数据库并刷新UI
            }

            @Override
            public void onProgress(String msgId, int totalByte, int progressByte) {
                // 处理文件发送上传进度（尽上传文件、图片时候SDK回调该方法）
            }

            @Override
            public void onComplete(ECError error) {
                // 忽略
            }
        });
    }

    private void initializeData() {
        mNetworkHelper = new NetworkHelper(this);
        mNetworkHelper.sendPostRequest(UrlParams.GET_FRIENDS_URL, mParams);
        mFriendAdapter = new FriendAdapter(this, mFriendList);
    }

    private void findView() {
//        mFriendListView = (ListView)mRightDrawer.getContentContainer().findViewById(R.id.friendListView);
//        mFriendListView.setAdapter(mFriendAdapter);
    }

    private void setListener() {
        mNetworkHelper.setUiDataListener(this);
    }

    @Override
    public void onDataChanged(Bean data) {
        JSONObject result = null;
        try {
            result = new JSONObject(data.getResult());
            Log.d("result", result.toString());
            String friendListStr = result.getString("relation.list");
            JSONArray friendList = new JSONArray(friendListStr);
            mFriendList.clear();
            String friendStr;
            for (int i = 0; i < friendList.length(); i++) {
                friendStr = friendList.getString(i);
                JSONObject friend = new JSONObject(friendStr);
                mFriendList.add(new Friend(
                        friend.getInt("UserBid"),
                        friend.getString("SmallFace"),
                        friend.getString("Account"),
                        friend.getString("Name")));
            }
            mFriendAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorHappened(int errorCode, String errorMessage) {

    }
}

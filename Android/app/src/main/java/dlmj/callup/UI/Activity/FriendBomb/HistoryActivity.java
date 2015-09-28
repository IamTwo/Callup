package dlmj.callup.UI.Activity.FriendBomb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dlmj.callup.BusinessLogic.Alarm.AlarmHelper;
import dlmj.callup.BusinessLogic.Alarm.AlarmSetManager;
import dlmj.callup.BusinessLogic.Cache.ConversationCache;
import dlmj.callup.BusinessLogic.Cache.FriendCache;
import dlmj.callup.BusinessLogic.Cache.HistoryCache;
import dlmj.callup.BusinessLogic.Cache.SceneCache;
import dlmj.callup.BusinessLogic.Cache.UserCache;
import dlmj.callup.BusinessLogic.IM.IMChattingHelper;
import dlmj.callup.BusinessLogic.Network.NetworkHelper;
import dlmj.callup.Common.Factory.BackColorFactory;
import dlmj.callup.Common.Factory.FragmentFactory;
import dlmj.callup.Common.Interfaces.AddHistoryListener;
import dlmj.callup.Common.Interfaces.DialogListener;
import dlmj.callup.Common.Interfaces.OnMessageReportCallback;
import dlmj.callup.Common.Interfaces.UIDataListener;
import dlmj.callup.Common.Model.Bean;
import dlmj.callup.Common.Model.ClientUser;
import dlmj.callup.Common.Model.Conversation;
import dlmj.callup.Common.Model.Friend;
import dlmj.callup.Common.Model.History;
import dlmj.callup.Common.Model.Scene;
import dlmj.callup.Common.Params.CodeParams;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Params.UrlParams;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.R;
import dlmj.callup.UI.Activity.MenuActivity;
import dlmj.callup.UI.Adapter.HistoryAdapter;
import dlmj.callup.UI.Adapter.SceneHorizonAdapter;
import dlmj.callup.UI.View.HorizontalListView;
import dlmj.callup.UI.View.SetBombTimeDialog;

/**
 * Created by Two on 15/9/19.
 */
public class HistoryActivity extends Activity{
    private final static String TAG = "HistoryActivity";
    private Friend mCurrentFriend;
    private TextView mNameTextView;
    private HorizontalListView mSceneListView;
    private SceneHorizonAdapter mSceneHorizonAdapter;
    private ListView mHistoryListView;
    private HistoryAdapter mHistoryAdapter;
    private List<History> mHistoryList;
    private List<Scene> mSceneList = new LinkedList<>();
    private NetworkHelper mGetScenesNetworkHelper;
    Map<String, String> mParams = new HashMap<>();
    private OnMessageReportCallback mOnMessageReportCallback;
    private SetBombTimeDialog mSetBombTimeDialog;
    private ImageView mBackImageView;
    private TextView mBackTextView;

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.history);
        initializeData();
        findView();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHistoryAdapter.notifyDataSetChanged();
        IMChattingHelper.getInstance(HistoryActivity.this).setOnMessageReportCallback(mOnMessageReportCallback);
    }

    public void initializeData() {
        mGetScenesNetworkHelper = new NetworkHelper(this);
        mCurrentFriend = (Friend)getIntent().getSerializableExtra(IntentExtraParams.FRIEND);
        mHistoryList = HistoryCache.getInstance().getHistoryList(mCurrentFriend);
        SceneCache sceneCache = SceneCache.getInstance();
        if (sceneCache.getList().size() > 0) {
            mSceneList = sceneCache.getList();
        } else {
            mGetScenesNetworkHelper.sendGetRequest(UrlParams.GET_SCENES_URL, mParams);
        }
        BackColorFactory backColorFactory = new BackColorFactory(this);
        mSceneHorizonAdapter = new SceneHorizonAdapter(this, mSceneList, backColorFactory);
        mHistoryAdapter = new HistoryAdapter(this, mCurrentFriend, mHistoryList);
    }

    public void findView() {
        mNameTextView = (TextView)findViewById(R.id.nameTextView);
        mNameTextView.setText(mCurrentFriend.getName());
        mSceneListView = (HorizontalListView)findViewById(R.id.sceneListView);
        mSceneListView.setAdapter(mSceneHorizonAdapter);
        mSetBombTimeDialog = new SetBombTimeDialog(this);
        mHistoryListView = (ListView) findViewById(R.id.historyListView);
        mHistoryListView.setAdapter(mHistoryAdapter);
        mBackImageView = (ImageView) findViewById(R.id.backImageView);
        mBackTextView = (TextView) findViewById(R.id.backTextView);
    }

    private void setListener() {
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MenuActivity.class);
                intent.putExtra(IntentExtraParams.FRAGMENT_NAME, FragmentFactory.FragmentName.FriendBomb);
                startActivity(intent);
                finish();
            }
        });

        mBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, MenuActivity.class);
                intent.putExtra(IntentExtraParams.FRAGMENT_NAME, FragmentFactory.FragmentName.FriendBomb);
                startActivity(intent);
                finish();
            }
        });

        mSetBombTimeDialog.setDialogListener(new DialogListener() {
            @Override
            public void closeDialog() {
                mSetBombTimeDialog.dismiss();
            }
        });

        mSetBombTimeDialog.setAddHistoryListener(new AddHistoryListener() {
            @Override
            public void addHistory(int sceneId, String time) {
                LogUtil.d(TAG, "Time is: " + time);
                ClientUser clientUser = UserCache.getInstance().getClientUser();
                History history = new History(SceneCache.getInstance().getScene(sceneId),
                        time, 0,
                        clientUser.getUserId(),
                        clientUser.getUserName());
                mHistoryList.add(history);
                mHistoryAdapter.notifyDataSetChanged();
                ConversationCache.getInstance().updateConversation(new Conversation(
                        mCurrentFriend.getUserId(),
                        String.format(getString(R.string.send_bomb_message), history.getTime())));
                mSetBombTimeDialog.dismiss();

                IMChattingHelper.getInstance(HistoryActivity.this).sendMessage(mCurrentFriend, history);
            }
        });

        mOnMessageReportCallback = new OnMessageReportCallback() {
            @Override
            public void onMessageReport(ECError error, ECMessage message) {

            }

            @Override
            public void onPushMessage() {
                mHistoryAdapter.notifyDataSetChanged();
            }
        };

        mGetScenesNetworkHelper.setUiDataListener(new UIDataListener<Bean>() {
            @Override
            public void onDataChanged(Bean data) {
                try {
                    JSONObject result = new JSONObject(data.getResult());
                    String sceneListStr = result.getString("scene.list");
                    JSONArray sceneList = new JSONArray(sceneListStr);
                    initializeSceneList();
                    String sceneStr;
                    for (int i = 0; i < sceneList.length(); i++) {
                        sceneStr = sceneList.getString(i);
                        JSONObject scene = new JSONObject(sceneStr);
                        mSceneList.add(new Scene(
                                scene.getInt("id"),
                                scene.getString("Name"),
                                scene.getString("Logo"),
                                scene.getString("Picture"),
                                scene.getString("Audio")));
                    }
                    mSceneHorizonAdapter.notifyDataSetChanged();
                    SceneCache.getInstance().setList(mSceneList);
                } catch (JSONException e) {
                    this.onErrorHappened(CodeParams.ERROR_SAVE_SESSION_TOKEN, e.toString());
                }
            }

            @Override
            public void onErrorHappened(int errorCode, String errorMessage) {

            }
        });

        mSceneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSetBombTimeDialog.setSceneInfo(mSceneList.get(position).getSceneId());
                mSetBombTimeDialog.show();

            }
        });
    }

    private void initializeSceneList() {
        mSceneList.clear();
        mSceneList.add(new Scene(getString(R.string.random)));
    }
}

package dlmj.callup.UI.Activity.SelfSave;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import dlmj.callup.BusinessLogic.Alarm.AlarmHelper;
import dlmj.callup.BusinessLogic.Cache.HistoryCache;
import dlmj.callup.BusinessLogic.Cache.ImageCacheManager;
import dlmj.callup.BusinessLogic.Cache.SceneCache;
import dlmj.callup.BusinessLogic.Cache.UserCache;
import dlmj.callup.BusinessLogic.IM.IMChattingHelper;
import dlmj.callup.Common.Interfaces.ChangeVibrationTimeListener;
import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Model.ClientUser;
import dlmj.callup.Common.Model.Friend;
import dlmj.callup.Common.Model.History;
import dlmj.callup.Common.Model.Scene;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.R;
import dlmj.callup.UI.View.CancelBombDialog;

/**
 * Created by Two on 15/8/27.
 */
public class AlarmActivity extends Activity {
    public final static int FINISHED = 1;
    private final static String TAG = "AlarmActivity";
    private ImageView mSwingTimeImageView;
    private NetworkImageView mNetworkImageView;

    private Alarm mAlarm;
    private ImageLoader mImageLoader;
    private TextView mSceneNameTextView;
    private int[] mNumArray;
    private AlarmHelper mAlarmHelper;
    private boolean mIfFromBomb;
    private CancelBombDialog mCancelBombDialog;
    private Friend mFriend;
    private ChangeVibrationTimeListener mChangeVibrationTimeListener;

    @Override
    public void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.alarm_ring);
        initializeData();
        findView();
        setListener();
        mAlarmHelper.registerListener();
    }

    private void initializeData() {
        String from = getIntent().getStringExtra(IntentExtraParams.FROM);
        mIfFromBomb = "BombReceiver".equals(from);
        mAlarmHelper = AlarmHelper.getInstance(this);
        mAlarmHelper.setHandler(mHandler);
        mFriend = (Friend) getIntent().getSerializableExtra(IntentExtraParams.FRIEND);
        mAlarm = (Alarm) getIntent().getSerializableExtra(IntentExtraParams.ALARM);

        if (mAlarm == null) {
            mAlarm = mAlarmHelper.getAlarm();
        } else {
            mAlarmHelper.setAlarm(mAlarm, mIfFromBomb);
        }
        mImageLoader = ImageCacheManager.getInstance(this).getImageLoader();

        TypedArray array = getResources().obtainTypedArray(R.array.number_image);
        mNumArray = new int[array.length()];
        for (int i = 0; i < array.length(); i++) {
            mNumArray[i] = array.getResourceId(i, 0);
        }
        array.recycle();
    }

    private void findView() {
        mSwingTimeImageView = (ImageView) findViewById(R.id.swingTimeImageView);
        mNetworkImageView = (NetworkImageView) findViewById(R.id.sceneImageView);
        mNetworkImageView.setImageUrl(mAlarm.getImageUrl(), mImageLoader);
        mSceneNameTextView = (TextView) findViewById(R.id.sceneNameTextView);
        mSceneNameTextView.setText(mAlarm.getSceneName());
        mCancelBombDialog = new CancelBombDialog(this);
        mCancelBombDialog.setCancelable(false);
    }

    private void setListener() {
        mChangeVibrationTimeListener = new ChangeVibrationTimeListener() {
            @Override
            public void onChanged(int swingTime) {
                mSwingTimeImageView.setBackgroundResource(mNumArray[swingTime]);
            }

            @Override
            public void showCancelBombDialog() {
                mCancelBombDialog.show();
            }

            @Override
            public void closeCancelBombDialog() {
                mAlarmHelper.stopAudio();
                mCancelBombDialog.dismiss();
                Scene scene = SceneCache.getInstance().getScene(mAlarm.getSceneId());
                ClientUser clientUser = UserCache.getInstance().getClientUser();
                History history = new History(scene,
                        mAlarm.getTime(), History.failed,
                        clientUser.getUserId(),
                        clientUser.getUserName());
                HistoryCache.getInstance().updateHistory(history, mFriend);
                IMChattingHelper.getInstance(AlarmActivity.this).sendMessage(mFriend, history);
            }
        };

        mCancelBombDialog.setChangeVibrationTimeListener(mChangeVibrationTimeListener);
        mAlarmHelper.setChangeVibrationTimeListener(mChangeVibrationTimeListener);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case FINISHED:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
}

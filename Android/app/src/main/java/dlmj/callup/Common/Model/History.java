package dlmj.callup.Common.Model;

import android.content.Context;

import java.util.Calendar;

import dlmj.callup.BusinessLogic.Cache.FriendCache;
import dlmj.callup.BusinessLogic.Cache.SceneCache;
import dlmj.callup.BusinessLogic.Cache.UserCache;

/**
 * Created by Two on 15/9/22.
 */
public class History {
    public final static int waiting = 0;
    public final static int accepted = 1;
    public final static int refused = 2;
    public final static int failed = 3;
    private String mTime;
    private Scene mScene;
    private int mStatus;
    private int mFromUserId;
    private String mFromUserName;

    public History(Scene scene, String time, int status, int fromUserId, String fromUserName) {
        mScene = scene;
        mTime = time;
        mStatus = status;
        mFromUserId = fromUserId;
        mFromUserName = fromUserName;
    }

    public History(String message, int fromUserId, Context context) {
        ClientUser clientUser = UserCache.getInstance().getClientUser();
        int sceneId = Integer.parseInt(message.split("/")[0]);
        mScene = SceneCache.getInstance(context).getScene(sceneId);
        mTime = message.split("/")[1];
        mStatus = Integer.parseInt(message.split("/")[2]);
        switch(mStatus) {
            case History.waiting:
            case History.failed:
                mFromUserId = fromUserId;
                mFromUserName = FriendCache.getInstance(context)
                        .getFriend(mFromUserId).getName();
                break;
            case History.accepted:
                mFromUserId = clientUser.getUserId();
                mFromUserName = clientUser.getUserName();
                break;
            default:
                break;
        }
    }

    public long getHistoryId() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public String getTime() {
        return mTime;
    }

    public Scene getScene() {
        return mScene;
    }

    public int getFromUserId() {
        return mFromUserId;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public String getFromUserName() {
        return mFromUserName;
    }
}

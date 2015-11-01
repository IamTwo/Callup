package dlmj.callup.BusinessLogic.Cache;

import android.content.Context;

import java.util.List;

import dlmj.callup.Common.Model.Friend;
import dlmj.callup.DataAccess.FriendTableManager;

/**
 * Created by Two on 15/9/19.
 */
public class FriendCache extends BaseCache<Friend>{
    private static FriendCache mInstance;
    private Context mContext;

    private FriendCache(Context context){
        super();
        mContext = context;
    }

    public static final FriendCache getInstance(Context context){
        synchronized (FriendCache.class){
            if(mInstance == null){
                mInstance = new FriendCache(context);
            }
        }
        return mInstance;
    }

    public Friend getFriend(int friendId) {
        for(Friend friend : mList) {
            if(friend.getUserId() == friendId){
                return friend;
            }
        }

        return FriendTableManager.getInstance(mContext).getFriend(friendId);
    }

    public List<Friend> getList() {
        if(super.getList().size() > 0) {
            return mList;
        }

        return FriendTableManager.getInstance(mContext).getFriends();
    }

    public void clear() {
        mList.clear();

        FriendTableManager.getInstance(mContext).clear();
    }
}

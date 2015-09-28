package dlmj.callup.BusinessLogic.Cache;

import dlmj.callup.Common.Model.Friend;

/**
 * Created by Two on 15/9/19.
 */
public class FriendCache extends BaseCache<Friend>{
    private static FriendCache mInstance;

    public FriendCache(){
        super();
    }

    public static final FriendCache getInstance(){
        synchronized (FriendCache.class){
            if(mInstance == null){
                mInstance = new FriendCache();
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

        return null;
    }
}

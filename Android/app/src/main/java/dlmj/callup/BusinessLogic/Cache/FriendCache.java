package dlmj.callup.BusinessLogic.Cache;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import dlmj.callup.Common.Model.Friend;

/**
 * Created by Two on 15/9/19.
 */
public class FriendCache {
    private static FriendCache mInstance;
    private List<Friend> mFriendList;

    public FriendCache(){
        mFriendList = new LinkedList<>();
    }

    public static final FriendCache getInstance(Context context){
        synchronized (FriendCache.class){
            if(mInstance == null){
                mInstance = new FriendCache();
            }
        }
        return mInstance;
    }

    public void setFriendList(List<Friend> friendList){
        mFriendList = friendList;
    }

    public List<Friend> getFriendList(){
        return mFriendList;
    }
}

package dlmj.callup.BusinessLogic.Cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dlmj.callup.Common.Model.Friend;
import dlmj.callup.Common.Model.History;

/**
 * Created by Two on 15/9/24.
 */
public class HistoryCache {
    private static HistoryCache mInstance;
    private static HashMap<Friend, List<History>> mHistoryMap = new HashMap<>();

    public HistoryCache(){
        super();
    }

    public static final HistoryCache getInstance(){
        synchronized (HistoryCache.class){
            if(mInstance == null){
                mInstance = new HistoryCache();
            }
        }
        return mInstance;
    }

    public List<History> getHistoryList(Friend currentFriend) {
        Friend friend;
        List<History> histories;
        Iterator iterator = mHistoryMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            friend = (Friend)entry.getKey();
            histories = (List<History>)entry.getValue();

            if(friend.getUserId() == currentFriend.getUserId()) {
                return histories;
            }
        }
        return null;
    }

    public void setHistoryList(List<Friend> friends) {
        for (Friend friend : friends) {
            mHistoryMap.put(friend, new LinkedList<History>());
        }
    }

    public void removeHistory() {
        mHistoryMap.clear();
    }

    private void updateHistory(History history, List<History> histories) {
        for(History item : histories) {
            if(item.getTime().equals(history.getTime())) {
                histories.remove(item);
                break;
            }
        }

        histories.add(history);
    }

    public void updateHistory(History history, Friend mFriend) {
        List<History> historyList = getHistoryList(mFriend);
        updateHistory(history, historyList);
    }
}

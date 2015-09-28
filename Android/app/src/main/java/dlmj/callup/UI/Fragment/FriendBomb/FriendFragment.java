package dlmj.callup.UI.Fragment.FriendBomb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dlmj.callup.BusinessLogic.Cache.FriendCache;
import dlmj.callup.BusinessLogic.Cache.HistoryCache;
import dlmj.callup.BusinessLogic.Network.NetworkHelper;
import dlmj.callup.Common.Interfaces.UIDataListener;
import dlmj.callup.Common.Model.Bean;
import dlmj.callup.Common.Model.Friend;

import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Params.UrlParams;
import dlmj.callup.R;
import dlmj.callup.UI.Activity.Account.ProfileActivity;
import dlmj.callup.UI.Adapter.FriendAdapter;

/**
 * Created by Two on 15/9/19.
 */
public class FriendFragment extends Fragment {
    private NetworkHelper mGetFriendNetworkHelper;
    private PullToRefreshListView mFriendListView;
    private List<Friend> mFriendList = new LinkedList<>();
    Map<String, String> mParams = new HashMap<>();
    private FriendAdapter mFriendAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friend, null);
        initializeData();
        findView(view);
        setListener();
        return view;
    }

    private void initializeData() {
        mGetFriendNetworkHelper = new NetworkHelper(getActivity());
        FriendCache friendCache = FriendCache.getInstance();
        if (friendCache.getList().size() > 0) {
            mFriendList = friendCache.getList();
        } else {
            mGetFriendNetworkHelper.sendGetRequest(UrlParams.GET_FRIENDS_URL, mParams);
        }
    }

    private void findView(View view) {
        mFriendListView = (PullToRefreshListView) view.findViewById(R.id.friendListView);
        mFriendAdapter = new FriendAdapter(getActivity(), mFriendList);
        mFriendListView.setAdapter(mFriendAdapter);
    }

    private void setListener() {
        mGetFriendNetworkHelper.setUiDataListener(new UIDataListener<Bean>() {
            @Override
            public void onDataChanged(Bean data) {
                try {
                    JSONObject result = new JSONObject(data.getResult());
                    String friendListStr = result.getString("relation.list");
                    JSONArray sceneList = new JSONArray(friendListStr);
                    mFriendList.clear();
                    String friendStr;
                    for (int i = 0; i < sceneList.length(); i++) {
                        friendStr = sceneList.getString(i);
                        JSONObject friend = new JSONObject(friendStr);
                        mFriendList.add(new Friend(
                                friend.getInt("UserBid"),
                                friend.getString("SmallFace"),
                                friend.getString("Account"),
                                friend.getString("Name")));
                    }

                    mFriendAdapter.notifyDataSetChanged();
                    FriendCache.getInstance().setList(mFriendList);
                    HistoryCache.getInstance().removeHistory();
                    HistoryCache.getInstance().setHistoryList(mFriendList);
                    mFriendListView.onRefreshComplete();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorHappened(int errorCode, String errorMessage) {

            }
        });

        mFriendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(IntentExtraParams.FRIEND, mFriendList.get(position - 1));
                startActivity(intent);
            }
        });

        mFriendListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                mGetFriendNetworkHelper.sendPostRequest(UrlParams.GET_FRIENDS_URL, mParams);
            }
        });
    }
}

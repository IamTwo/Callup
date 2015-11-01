package dlmj.callup.UI.Fragment.FriendBomb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import dlmj.callup.BusinessLogic.Cache.ConversationCache;
import dlmj.callup.BusinessLogic.Cache.FriendCache;
import dlmj.callup.Common.Model.Conversation;
import dlmj.callup.Common.Model.Friend;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Params.SharedPreferenceParams;
import dlmj.callup.R;
import dlmj.callup.UI.Activity.FriendBomb.HistoryActivity;
import dlmj.callup.UI.Adapter.ConversationAdapter;

/**
 * Created by Two on 15/9/19.
 */
public class BombFragment extends Fragment {
    private ConversationAdapter mConversationAdapter;
    private ListView mBombListView;
    private List<Conversation> mConversationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bomb, null);
        initializeData();
        findView(view);
        setListener();
        return view;
    }

    private void initializeData() {
        ConversationCache conversationCache = ConversationCache.getInstance();
        mConversationList = conversationCache.getList();
        mConversationAdapter = new ConversationAdapter(this.getActivity(), mConversationList);
    }

    private void setListener() {
        mBombListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int size  = mConversationList.size();
                Conversation conversation = mConversationList.get(size - position - 1);
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                Friend friend = FriendCache.getInstance(getActivity())
                        .getFriend(conversation.getFriendUserId());
                intent.putExtra(IntentExtraParams.FRIEND, friend);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void findView(View view) {
        mBombListView = (ListView) view.findViewById(R.id.conversationListView);
        mBombListView.setAdapter(mConversationAdapter);
    }
}

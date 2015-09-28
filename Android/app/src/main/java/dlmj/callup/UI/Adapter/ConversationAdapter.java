package dlmj.callup.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.Collections;
import java.util.List;

import dlmj.callup.BusinessLogic.Cache.FriendCache;
import dlmj.callup.BusinessLogic.Cache.ImageCacheManager;
import dlmj.callup.Common.Model.Conversation;
import dlmj.callup.Common.Model.Friend;
import dlmj.callup.R;
import dlmj.callup.UI.View.CircleImageView;

/**
 * Created by Two on 15/9/26.
 */
public class ConversationAdapter extends BaseAdapter{
    private List<Conversation> mConversations;
    private Context mContext;
    private ImageLoader mImageLoader;

    public ConversationAdapter(Context context, List<Conversation> conversations){
        this.mContext = context;
        this.mImageLoader = ImageCacheManager.getInstance(context).getImageLoader();
        this.mConversations = conversations;
    }

    @Override
    public int getCount() {
        if (mConversations != null) {
            return mConversations.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mConversations.get(getCount() - position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.conversation_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.mFriendPhotoImageView = (CircleImageView) convertView
                    .findViewById(R.id.friendPhotoImageView);
            viewHolder.mFriendNameTextView = (TextView) convertView
                    .findViewById(R.id.friendNameTextView);
            viewHolder.mLastMessageTextView = (TextView) convertView
                    .findViewById(R.id.lastMessageTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Conversation conversation = (Conversation)getItem(position);
        Friend friend = FriendCache.getInstance().getFriend(conversation.getFriendUserId());
        viewHolder.mFriendPhotoImageView.setImageUrl(friend.getFaceUrl(), mImageLoader);
        viewHolder.mFriendNameTextView.setText(friend.getName());
        viewHolder.mLastMessageTextView.setText(conversation.getLastMessage());
        return convertView;
    }

    class ViewHolder {
        CircleImageView mFriendPhotoImageView;
        TextView mFriendNameTextView;
        TextView mLastMessageTextView;
    }
}

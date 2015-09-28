package dlmj.callup.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import dlmj.callup.BusinessLogic.Cache.ImageCacheManager;
import dlmj.callup.Common.Factory.BackColorFactory;
import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Model.Friend;
import dlmj.callup.R;

/**
 * Created by Two on 15/9/8.
 */
public class FriendAdapter extends BaseAdapter {
    private List<Friend> mFriends;
    private Context mContext;
    private ImageLoader mImageLoader;

    public FriendAdapter(Context context, List<Friend> friends){
        this.mFriends = friends;
        this.mContext = context;
        this.mImageLoader = ImageCacheManager.getInstance(context).getImageLoader();
    }

    @Override
    public int getCount() {
        if (mFriends != null) {
            return mFriends.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mFriends.get(position);
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
            convertView = inflater.inflate(R.layout.friend_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.mFriendImageView = (NetworkImageView) convertView.findViewById(R.id.friendImageView);
            viewHolder.mFriendNameTextView = (TextView) convertView.findViewById(R.id.friendNameTextView);
            viewHolder.mMessageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Friend friend = (Friend)getItem(position);
        viewHolder.mFriendImageView
                .setImageUrl(friend.getFaceUrl(), mImageLoader);
        viewHolder.mFriendImageView.setDefaultImageResId(R.drawable.default_photo);
        viewHolder.mFriendNameTextView.setText(friend.getName());
        return convertView;
    }

    class ViewHolder {
        NetworkImageView mFriendImageView;
        TextView mFriendNameTextView;
        TextView mMessageTextView;
    }
}

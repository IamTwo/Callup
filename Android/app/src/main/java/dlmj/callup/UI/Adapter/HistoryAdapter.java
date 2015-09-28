package dlmj.callup.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import dlmj.callup.BusinessLogic.Alarm.AlarmSetManager;
import dlmj.callup.BusinessLogic.Cache.ImageCacheManager;
import dlmj.callup.BusinessLogic.Cache.UserCache;
import dlmj.callup.BusinessLogic.IM.IMChattingHelper;
import dlmj.callup.Common.Model.Friend;
import dlmj.callup.Common.Model.History;
import dlmj.callup.R;
import dlmj.callup.UI.View.CircleImageView;

/**
 * Created by Two on 15/9/22.
 */
public class HistoryAdapter extends BaseAdapter {
    private List<History> mHistories;
    private Context mContext;
    private Friend mFriend;
    private ImageLoader mImageLoader;

    public HistoryAdapter(Context context, Friend friend, List<History> histories) {
        mContext = context;
        mFriend = friend;
        mHistories = histories;
        this.mImageLoader = ImageCacheManager.getInstance(context).getImageLoader();
    }

    @Override
    public int getCount() {
        if (mHistories != null) {
            return mHistories.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mHistories.get(position);
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
            convertView = inflater.inflate(R.layout.history_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.mMeLayout = (LinearLayout) convertView.findViewById(R.id.meLayout);
            viewHolder.mMePhotoCircleImageView = (CircleImageView) convertView.findViewById(R.id.mePhotoCircleImageView);
            viewHolder.mMeSceneImageView = (NetworkImageView) convertView.findViewById(R.id.meSceneImageView);
            viewHolder.mMeMessageTextView = (TextView) convertView.findViewById(R.id.meMessageTextView);
            viewHolder.mMeAcceptButton = (Button) convertView.findViewById(R.id.meAcceptButton);
            viewHolder.mFriendLayout = (LinearLayout) convertView.findViewById(R.id.friendLayout);
            viewHolder.mFriendPhotoCircleImageView = (CircleImageView) convertView.findViewById(R.id.friendPhotoCircleImageView);
            viewHolder.mFriendSceneImageView = (NetworkImageView) convertView.findViewById(R.id.friendSceneImageView);
            viewHolder.mFriendMessageTextView = (TextView) convertView.findViewById(R.id.friendMessageTextView);
            viewHolder.mFriendAcceptButton = (Button) convertView.findViewById(R.id.friendAcceptButton);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        History history = (History)getItem(position);

        if(history.getFromUserId() == UserCache.getInstance().getClientUser().getUserId()) {
            viewHolder.mMeLayout.setVisibility(View.VISIBLE);
            viewHolder.mFriendLayout.setVisibility(View.GONE);
            viewHolder.mMePhotoCircleImageView.setImageUrl(UserCache.getInstance().getClientUser().getFaceUrl(),
                    mImageLoader);
            viewHolder.mMeSceneImageView.setImageUrl(history.getScene().getLogoUrl(), mImageLoader);
            viewHolder.mMeMessageTextView.setText(String.format(mContext.getString(R.string.send_bomb_message),
                    history.getTime()));
        }else {
            viewHolder.mFriendLayout.setVisibility(View.VISIBLE);
            viewHolder.mMeLayout.setVisibility(View.GONE);
            viewHolder.mFriendPhotoCircleImageView.setImageUrl(mFriend.getFaceUrl(), mImageLoader);
            viewHolder.mFriendSceneImageView.setImageUrl(history.getScene().getLogoUrl(), mImageLoader);
            viewHolder.mFriendMessageTextView.setText(String.format(mContext.getString(R.string.receive_bomb_message),
                    history.getTime()));
        }
        checkFriendStatus(history, viewHolder);
        return convertView;
    }

    private void checkFriendStatus(final History history, final ViewHolder viewHolder) {
        switch(history.getStatus()) {
            case History.waiting:
                viewHolder.mMeAcceptButton.setBackgroundResource(R.drawable.accept_disabled_btn);
                viewHolder.mMeAcceptButton.setText(mContext.getString(R.string.waiting));
                viewHolder.mFriendAcceptButton.setBackgroundResource(R.drawable.accept_btn_back);
                viewHolder.mFriendAcceptButton.setText(mContext.getString(R.string.accept));
                viewHolder.mFriendAcceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        history.setStatus(History.accepted);
                        IMChattingHelper.getInstance(mContext).sendMessage(mFriend, history);
                        AlarmSetManager.setAlarm(mContext, history, mFriend);
                        notifyDataSetChanged();
                    }
                });
                break;
            case History.accepted:
                viewHolder.mMeAcceptButton.setBackgroundResource(R.drawable.accept_disabled_btn);
                viewHolder.mMeAcceptButton.setText(mContext.getString(R.string.accepted));
                viewHolder.mFriendAcceptButton.setBackgroundResource(R.drawable.accept_disabled_btn);
                viewHolder.mFriendAcceptButton.setText(mContext.getString(R.string.accepted));
                break;
            case History.refused:
                viewHolder.mMeAcceptButton.setBackgroundResource(R.drawable.accept_disabled_btn);
                viewHolder.mMeAcceptButton.setText(mContext.getString(R.string.refused));
                break;
            case History.failed:
                viewHolder.mMeMessageTextView.setText(String.format(mContext.getString(R.string.win_pk),
                        history.getFromUserName()));
                viewHolder.mFriendMessageTextView.setText(String.format(mContext.getString(R.string.win_pk),
                        history.getFromUserName()));
                viewHolder.mMeAcceptButton.setVisibility(View.GONE);
                viewHolder.mFriendAcceptButton.setVisibility(View.GONE);
        }
    }

    class ViewHolder {
        CircleImageView mMePhotoCircleImageView;
        NetworkImageView mMeSceneImageView;
        TextView mMeMessageTextView;
        Button mMeAcceptButton;
        LinearLayout mMeLayout;

        CircleImageView mFriendPhotoCircleImageView;
        NetworkImageView mFriendSceneImageView;
        TextView mFriendMessageTextView;
        Button mFriendAcceptButton;
        LinearLayout mFriendLayout;
    }
}

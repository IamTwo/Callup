package dlmj.callup.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import dlmj.callup.BusinessLogic.Cache.ImageCacheManager;
import dlmj.callup.Common.Factory.BackColorFactory;
import dlmj.callup.Common.Model.Scene;
import dlmj.callup.R;

/**
 * Created by Two on 15/8/16.
 */
public class SceneAdapter extends BaseAdapter {
    private List<Scene> mScenes;
    private ImageLoader mImageLoader;
    private Context mContext;
    private BackColorFactory mBackColorFactory;

    public SceneAdapter(Context context, List<Scene> scenes, BackColorFactory backColorFactory) {
        this.mScenes = scenes;
        this.mContext = context;
        this.mImageLoader = ImageCacheManager.getInstance(context).getImageLoader();
        this.mBackColorFactory = backColorFactory;
    }

    @Override
    public int getCount() {
        if (mScenes != null) {
            return mScenes.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mScenes.get(position);
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
            convertView = inflater.inflate(R.layout.scene_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.mSceneImageView = (NetworkImageView) convertView
                    .findViewById(R.id.scene_imageView);
            viewHolder.mSceneTextView = (TextView) convertView
                    .findViewById(R.id.scene_textView);
            viewHolder.mSceneLayout = (LinearLayout) convertView
                    .findViewById(R.id.scene_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setViewHolder(position, viewHolder);

        return convertView;
    }

    private void setViewHolder(int position, ViewHolder viewHolder) {
        Scene scene = (Scene)getItem(position);
        viewHolder.mSceneImageView.setDefaultImageResId(R.drawable.question);
        viewHolder.mSceneImageView
                .setImageUrl(scene.getLogoUrl(), mImageLoader);
        viewHolder.mSceneTextView.setText(scene.getName());
        viewHolder.mSceneLayout.setBackgroundColor(mBackColorFactory.get(position));
    }

    class ViewHolder {
        NetworkImageView mSceneImageView;
        TextView mSceneTextView;

        LinearLayout mSceneLayout;
    }
}

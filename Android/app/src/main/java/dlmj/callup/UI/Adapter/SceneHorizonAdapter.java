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
 * Created by Two on 15/9/22.
 */
public class SceneHorizonAdapter extends BaseAdapter {
    private List<Scene> mScenes;
    private Context mContext;
    private ImageLoader mImageLoader;
    private BackColorFactory mBackColorFactory;

    public SceneHorizonAdapter(Context context, List<Scene> scenes, BackColorFactory backColorFactory) {
        mContext = context;
        mScenes = scenes;
        this.mImageLoader = ImageCacheManager.getInstance(context).getImageLoader();
        mBackColorFactory = backColorFactory;
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
            convertView = inflater.inflate(R.layout.scene_horizon_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.mSceneImageView = (NetworkImageView) convertView
                    .findViewById(R.id.sceneImageView);
            viewHolder.mSceneNameTextView = (TextView) convertView
                    .findViewById(R.id.sceneNameTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Scene scene = (Scene)getItem(position);
        viewHolder.mSceneImageView.setDefaultImageResId(R.drawable.question);
        viewHolder.mSceneImageView
                .setImageUrl(scene.getLogoUrl(), mImageLoader);
        viewHolder.mSceneImageView.setBackgroundColor(mBackColorFactory.get(position));
        viewHolder.mSceneNameTextView.setText(scene.getName(mContext));
        return convertView;
    }

    class ViewHolder {
        NetworkImageView mSceneImageView;
        TextView mSceneNameTextView;
    }
}

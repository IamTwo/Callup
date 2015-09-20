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
import dlmj.callup.R;

/**
 * Created by Two on 15/8/23.
 */
public class AlarmAdapter extends BaseAdapter{
    private List<Alarm> mAlarms;
    private Context mContext;
    private ImageLoader mImageLoader;
    private BackColorFactory mBackColorFactory;

    public AlarmAdapter(Context context, List<Alarm> alarms, BackColorFactory backColorFactory){
        this.mAlarms = alarms;
        this.mContext = context;
        this.mImageLoader = ImageCacheManager.getInstance(context).getImageLoader();
        this.mBackColorFactory = backColorFactory;
    }

    @Override
    public int getCount() {
        if (mAlarms != null) {
            return mAlarms.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mAlarms.get(position);
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
            convertView = inflater.inflate(R.layout.alarm_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.mSceneImageView = (NetworkImageView) convertView
                    .findViewById(R.id.sceneImageView);
            viewHolder.mTimeTextView = (TextView) convertView
                    .findViewById(R.id.timeTextView);
            viewHolder.mSceneTextView = (TextView) convertView
                    .findViewById(R.id.sceneTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Alarm alarm = (Alarm)getItem(position);
        viewHolder.mSceneImageView
                .setImageUrl(alarm.getLogoUrl(), mImageLoader);
        viewHolder.mSceneTextView.setText(alarm.getSceneName());
        viewHolder.mTimeTextView.setText(alarm.getTime());
        viewHolder.mSceneImageView.setBackgroundColor(mBackColorFactory.get(position));
        return convertView;
    }

    class ViewHolder {
        NetworkImageView mSceneImageView;
        TextView mTimeTextView;
        TextView mSceneTextView;
    }
}

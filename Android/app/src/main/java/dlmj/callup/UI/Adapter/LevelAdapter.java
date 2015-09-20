package dlmj.callup.UI.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by Two on 15/8/10.
 */
public class LevelAdapter extends PagerAdapter {
    private List<View> mLevelViews;

    public LevelAdapter(List<View> levelViews) {
        this.mLevelViews = levelViews;
    }

    @Override
    public int getCount() {
        if (mLevelViews != null) {
            return mLevelViews.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        container.addView(mLevelViews.get(position));
        return mLevelViews.get(position);
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
}

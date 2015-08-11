package dlmj.callup.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Created by Two on 15/8/10.
 */
public class LevelAdapter extends PagerAdapter {
    private List<View> mSceneViews;

    public LevelAdapter(List<View> sceneViews) {
        this.mSceneViews = sceneViews;
    }

    @Override
    public int getCount() {
        if (mSceneViews != null) {
            return mSceneViews.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

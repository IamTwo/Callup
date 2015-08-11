package dlmj.callup.Activity.SelfSave;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import dlmj.callup.Adapter.LevelAdapter;
import dlmj.callup.R;

/**
 * Created by Two on 15/8/6.
 */
public class SetLevelActivity extends Activity {
    private ViewPager mLevelViewPager;
    @Override
    public void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.set_level);
        findView();
    }

    private void findView() {
        mLevelViewPager = (ViewPager)findViewById(R.id.layoutViewPager);
        LevelAdapter levelAdapter = new LevelAdapter()
        mLevelViewPager.setAdapter();
    }

    private void loadLevels(){
        List<View> levelList = new ArrayList<View>();


    }
}

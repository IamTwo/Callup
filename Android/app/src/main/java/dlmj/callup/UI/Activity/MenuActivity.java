package dlmj.callup.UI.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import dlmj.callup.Common.Factory.FragmentFactory;
import dlmj.callup.Common.Interfaces.ChangeFragmentListener;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.R;
import dlmj.callup.UI.Fragment.MenuFragment;

/**
 * Created by Two on 15/8/27.
 */
public class MenuActivity extends FragmentActivity {
    private final static String TAG = "MenuActivity";
    private SlidingMenu mSlidingMenu;
    public View.OnClickListener menuClickListener;
    public ChangeFragmentListener changeFragmentListener;
    private FragmentFactory mFragmentFactory;
    private FragmentManager mFragmentManager;

    @Override
    public void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        initializeData();
        setListener();
    }

    public void initializeData() {
        mFragmentFactory = new FragmentFactory(this);
        mFragmentManager = getSupportFragmentManager();

        try {
            FragmentFactory.FragmentName fragmentName = (FragmentFactory.FragmentName) getIntent().
                    getSerializableExtra(IntentExtraParams.FRAGMENT_NAME);
            Fragment fragment = mFragmentFactory.get(fragmentName);
            initSlidingMenu(fragment);
        } catch (Exception ex) {
            LogUtil.e(TAG, ex.getMessage());
        }
    }

    private void setListener() {
        changeFragmentListener = new ChangeFragmentListener() {
            @Override
            public void ChangeFragment(FragmentFactory.FragmentName fragmentName) {
                try {
                    Fragment fragment = mFragmentFactory.get(fragmentName);
                    mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                } catch (Exception ex) {
                    LogUtil.e(TAG, ex.getMessage());
                }
            }
        };

        menuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSlidingMenu.isMenuShowing()) {
                    mSlidingMenu.showContent();
                } else {
                    mSlidingMenu.showMenu();
                }
            }
        };
    }

    protected void initSlidingMenu(Fragment fragment) {
        setContentView(R.layout.content_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();

        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mSlidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        mSlidingMenu.setMenu(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new MenuFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        if (mSlidingMenu.isMenuShowing()) {
            mSlidingMenu.showContent();
        } else {
            super.onBackPressed();
        }
    }
}

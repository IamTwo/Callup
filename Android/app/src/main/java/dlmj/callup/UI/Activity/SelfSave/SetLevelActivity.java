package dlmj.callup.UI.Activity.SelfSave;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dlmj.callup.BusinessLogic.Cache.LevelCache;
import dlmj.callup.BusinessLogic.Network.NetworkHelper;
import dlmj.callup.Common.Factory.FragmentFactory;
import dlmj.callup.Common.Interfaces.UIDataListener;
import dlmj.callup.Common.Model.Level;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Params.SharedPreferenceParams;
import dlmj.callup.Common.Params.SharedPreferenceSettings;
import dlmj.callup.Common.Params.UrlParams;
import dlmj.callup.Common.Util.CallUpPreferences;
import dlmj.callup.R;
import dlmj.callup.UI.Activity.MenuActivity;
import dlmj.callup.UI.Adapter.LevelAdapter;

/**
 * Created by Two on 15/8/6.
 */
public class SetLevelActivity extends Activity {
    private ViewPager mLevelViewPager;
    private View.OnClickListener mOnSelectClickListener;
    private View.OnClickListener mOnSubmitClickListener;
    private NetworkHelper mSetLevelNetworkHelper;
    Map<String, String> mParams = new HashMap<>();
    private int mLevel = 0;

    @Override
    public void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        initializeData();
        setContentView(R.layout.set_level);
        findView();
        setListener();
        loadLevels();
    }

    private void initializeData() {
        mSetLevelNetworkHelper = new NetworkHelper(this);
    }

    private void findView() {
        mLevelViewPager = (ViewPager) findViewById(R.id.layoutViewPager);
    }

    private void setListener() {
        mSetLevelNetworkHelper.setUiDataListener(new UIDataListener() {
            @Override
            public void onDataChanged(Object data) {
                SharedPreferenceSettings levelValue = SharedPreferenceSettings.LEVEL_VALUE;
                SharedPreferences sharedPreferences = CallUpPreferences.getSharedPreferences();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(levelValue.getId(), mLevel);
                editor.apply();
                Intent intent = new Intent();
                intent.setClass(SetLevelActivity.this, MenuActivity.class);
                intent.putExtra(IntentExtraParams.FRAGMENT_NAME,
                        FragmentFactory.FragmentName.SetAlarm);
                startActivity(intent);
                finish();
            }

            @Override
            public void onErrorHappened(int errorCode, String errorMessage) {

            }
        });

        mOnSelectClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ifSelected = true;

                if (view.getTag() != null) {
                    ifSelected = !(boolean) view.getTag();
                }

                if (ifSelected) {
                    mLevel++;
                    ((ImageView) view).setImageResource(R.drawable.arrow_selected);
                } else {
                    mLevel--;
                    ((ImageView) view).setImageResource(R.drawable.arrow);
                }

                view.setTag(ifSelected);
            }
        };

        mOnSubmitClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParams.put("key", "Level");
                mParams.put("val", mLevel + "");
                mSetLevelNetworkHelper.sendPostRequest(UrlParams.EDIT_ACCOUNT_URL, mParams);
            }
        };
    }

    private void loadLevels() {
        LevelCache levelCache = LevelCache.getInstance(this);
        List<View> levelViews = new ArrayList<>();
        List<Level> levels = levelCache.getList();
        for (int i = 0; i < levels.size(); i++) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.level_item, null);
            ImageView levelImageView = (ImageView) view.findViewById(R.id.levelImageView);
            levelImageView.setImageResource(levels.get(i).getImageResource());
            TextView levelTextView = (TextView) view.findViewById(R.id.levelTextView);
            levelTextView.setText(getResources().getString(levels.get(i).getDescription()));
            ImageView levelSelectImageView = (ImageView) view.findViewById(R.id.levelSelectImageView);
            levelSelectImageView.setOnClickListener(mOnSelectClickListener);
            levelViews.add(view);
        }

        View view = LayoutInflater.from(this)
                .inflate(R.layout.set_level_item, null);
        Button submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(mOnSubmitClickListener);
        levelViews.add(view);

        LevelAdapter levelAdapter = new LevelAdapter(levelViews);
        mLevelViewPager.setAdapter(levelAdapter);
    }
}

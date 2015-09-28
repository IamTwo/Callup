package dlmj.callup.UI.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dlmj.callup.Common.Factory.FragmentFactory;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Params.SharedPreferenceSettings;
import dlmj.callup.Common.Util.CallUpPreferences;
import dlmj.callup.R;
import dlmj.callup.UI.Activity.MenuActivity;
import dlmj.callup.UI.Activity.SelfSave.SetLevelActivity;

/**
 * Created by Two on 15/8/15.
 */
public class MenuFragment extends Fragment {
    private Button mSelfSaveBtn;
    private Button mFriendBombBtn;
    private Button mSettingBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu, container, false);
        findView(view);
        setListener();
        return view;
    }

    public void findView(View view) {
        mSelfSaveBtn = (Button) view.findViewById(R.id.selfSaveButton);
        mFriendBombBtn = (Button) view.findViewById(R.id.friendBombButton);
        mSettingBtn = (Button) view.findViewById(R.id.settingButton);
    }

    public void setListener() {
        mSelfSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                SharedPreferences sharedPreferences = CallUpPreferences.getSharedPreferences();
                SharedPreferenceSettings levelValue = SharedPreferenceSettings.LEVEL_VALUE;
                int level = sharedPreferences.getInt(levelValue.getId(),
                        (int) levelValue.getDefaultValue());

                if (level >= 0) {
                    intent.setClass(getActivity(), MenuActivity.class);
                    intent.putExtra(IntentExtraParams.FRAGMENT_NAME,
                            FragmentFactory.FragmentName.SetAlarm);
                } else {
                    intent.setClass(getActivity(), SetLevelActivity.class);
                }
                startActivity(intent);
            }
        });

        mFriendBombBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MenuActivity.class);
                intent.putExtra(IntentExtraParams.FRAGMENT_NAME,
                        FragmentFactory.FragmentName.FriendBomb);
                startActivity(intent);
            }
        });

        mSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MenuActivity.class);
                intent.putExtra(IntentExtraParams.FRAGMENT_NAME,
                        FragmentFactory.FragmentName.Settings);
                startActivity(intent);
            }
        });
    }
}

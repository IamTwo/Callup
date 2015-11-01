package dlmj.callup.UI.Fragment.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import dlmj.callup.BusinessLogic.Alarm.AlarmSetManager;
import dlmj.callup.Common.Params.SharedPreferenceSettings;
import dlmj.callup.Common.Util.CallUpPreferences;
import dlmj.callup.R;
import dlmj.callup.UI.Activity.Account.AboutActivity;
import dlmj.callup.UI.Activity.Account.AccountActivity;
import dlmj.callup.UI.Activity.Account.IntroduceActivity;
import dlmj.callup.UI.Activity.SelfSave.SetLevelActivity;
import dlmj.callup.UI.Fragment.CallUpFragment;

/**
 * Created by Two on 15/9/22.
 */
public class SettingFragment extends CallUpFragment {
    private Button mLogOutButton;
    private RelativeLayout mAccountLayout;
    private RelativeLayout mLevelLayout;
    private RelativeLayout mAboutCallUpLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting, null);
        initializeData();
        findView(view);
        setListener();
        return view;
    }

    public void setListener() {
        super.setListener();
        mLevelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetLevelActivity.class);
                startActivity(intent);
            }
        });

        mAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
            }
        });

        mAboutCallUpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = CallUpPreferences.getSharedPreferences();
                SharedPreferenceSettings sessionToken = SharedPreferenceSettings.SESSION_TOKEN;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(sessionToken.getId());
                editor.apply();
                AlarmSetManager.clear(getActivity());
                Intent intent = new Intent(getActivity(), IntroduceActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void findView(View view) {
        mMenuButton = (Button) view.findViewById(R.id.menuButton);
        mLogOutButton = (Button) view.findViewById(R.id.logOutButton);
        mAccountLayout = (RelativeLayout) view.findViewById(R.id.accountLayout);
        mLevelLayout = (RelativeLayout) view.findViewById(R.id.levelLayout);
        mAboutCallUpLayout = (RelativeLayout) view.findViewById(R.id.aboutCallUpLayout);
    }

    private void initializeData() {

    }
}

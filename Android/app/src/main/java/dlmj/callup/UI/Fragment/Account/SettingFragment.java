package dlmj.callup.UI.Fragment.Account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dlmj.callup.Common.Params.SharedPreferenceSettings;
import dlmj.callup.Common.Util.CallUpPreferences;
import dlmj.callup.R;
import dlmj.callup.UI.Activity.Account.IntroduceActivity;
import dlmj.callup.UI.Fragment.CallUpFragment;

/**
 * Created by Two on 15/9/22.
 */
public class SettingFragment extends CallUpFragment {
    private Button mLogOutButton;

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

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = CallUpPreferences.getSharedPreferences();
                SharedPreferenceSettings sessionToken = SharedPreferenceSettings.SESSION_TOKEN;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(sessionToken.getId());
                editor.apply();
                Intent intent = new Intent(getActivity(), IntroduceActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void findView(View view) {
        mMenuButton = (Button) view.findViewById(R.id.menuButton);
        mLogOutButton = (Button) view.findViewById(R.id.logOutButton);
    }

    private void initializeData() {

    }
}

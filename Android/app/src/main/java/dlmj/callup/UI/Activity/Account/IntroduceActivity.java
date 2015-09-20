package dlmj.callup.UI.Activity.Account;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dlmj.callup.Common.Factory.ErrorMessageFactory;
import dlmj.callup.Common.Params.CodeParams;
import dlmj.callup.Common.Interfaces.UIDataListener;
import dlmj.callup.Common.Params.SharedPreferenceParams;
import dlmj.callup.Common.Params.SharedPreferenceSettings;
import dlmj.callup.Common.Params.UrlParams;
import dlmj.callup.Common.Model.Bean;
import dlmj.callup.BusinessLogic.Network.NetworkHelper;
import dlmj.callup.Common.Util.CallUpPreferences;
import dlmj.callup.R;
import dlmj.callup.UI.Activity.MainActivity;
import dlmj.callup.UI.View.CustomAlertDialog;

/**
 * Created by Two on 15/7/27.
 */
public class IntroduceActivity extends Activity implements UIDataListener<Bean> {
    private Button mLoginMenuButton;
    private Button mRegisterMenuButton;
    private Dialog mLoginDialog;
    private Dialog mRegisterDialog;
    private NetworkHelper mNetworkHelper;
    private ErrorMessageFactory mErrorMessageFactory;
    private SharedPreferences mSharedPreferences;

    private Button mLoginButton;
    private String mAccount;
    private String mPassword;

    @Override
    public void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.introduce);
        initializeData();
        checkIfAutoLogin();
        findView();
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoginDialog.dismiss();
    }

    public void initializeData() {
        mNetworkHelper = new NetworkHelper(this);
        mErrorMessageFactory = new ErrorMessageFactory(this);
        mSharedPreferences = CallUpPreferences.getSharedPreferences();
    }

    private View.OnClickListener mOnLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Map<String, String> params = new HashMap<>();
            params.put("account", mAccount);
            params.put("password", mPassword);
            mNetworkHelper.sendPostRequestWithoutSid(UrlParams.LOGIN_URL, params);
        }
    };

    private void findView() {
        mLoginMenuButton = (Button) findViewById(R.id.loginButton);
        mRegisterMenuButton = (Button) findViewById(R.id.registerButton);

        mLoginDialog = new Dialog(IntroduceActivity.this, R.style.AlertDialog);
        mRegisterDialog = new Dialog(IntroduceActivity.this, R.style.AlertDialog);
    }

    private void setListener() {
        mNetworkHelper.setUiDataListener(this);

        mLoginMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View loginView = LayoutInflater.from(IntroduceActivity.this).inflate(R.layout.login, null);
                mLoginDialog.setContentView(loginView);
                mLoginDialog.show();

                mLoginMenuButton.setVisibility(View.INVISIBLE);
                mRegisterMenuButton.setVisibility(View.INVISIBLE);

                mLoginButton = (Button) loginView.findViewById(R.id.loginButton);
                EditText accountEditText = (EditText) loginView.findViewById(R.id.accountEditText);
                EditText passwordEditText = (EditText) loginView.findViewById(R.id.passwordEditText);
                mLoginButton.setOnClickListener(mOnLoginClickListener);
                accountEditText.addTextChangedListener(accountTextWatcher);
                passwordEditText.addTextChangedListener(passwordTextWatcher);
            }
        });

        mRegisterMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View registerView = LayoutInflater.from(IntroduceActivity.this).inflate(R.layout.register, null);
                mRegisterDialog.setContentView(registerView);
                mRegisterDialog.show();

                mLoginMenuButton.setVisibility(View.INVISIBLE);
                mRegisterMenuButton.setVisibility(View.INVISIBLE);
            }
        });

        mLoginDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mLoginMenuButton.setVisibility(View.VISIBLE);
                mRegisterMenuButton.setVisibility(View.VISIBLE);
            }
        });

        mRegisterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mLoginMenuButton.setVisibility(View.VISIBLE);
                mRegisterMenuButton.setVisibility(View.VISIBLE);
            }
        });
    }

    TextWatcher accountTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mAccount = s.toString();
            Log.d("Account", mAccount);

            CheckLoginEnabled();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mPassword = s.toString();
            Log.d("Password", mPassword);
            CheckLoginEnabled();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void checkIfAutoLogin() {
        SharedPreferenceSettings sessionToken = SharedPreferenceSettings.SESSION_TOKEN;
        String sessionTokenValue = mSharedPreferences.getString(
                sessionToken.getId(),
                (String)sessionToken.getDefaultValue());
        if (!sessionTokenValue.isEmpty()) {
            GoToMainActivity();
        }
    }

    private void CheckLoginEnabled() {
        if (mAccount == null || mAccount.isEmpty() ||
                mPassword == null || mPassword.isEmpty()) {
            mLoginButton.setEnabled(false);
        } else {
            mLoginButton.setEnabled(true);
        }
    }

    private void GoToMainActivity() {
        Intent intent = new Intent();
        intent.setClass(IntroduceActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDataChanged(Bean data) {
        try {
            JSONObject result = new JSONObject(data.getResult());
            String userInfoStr = result.getString("Customer");
            JSONObject userInfo = new JSONObject(userInfoStr);
            String sessionTokenValue = userInfo.getString("sid");
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            SharedPreferenceSettings sessionToken = SharedPreferenceSettings.SESSION_TOKEN;
            editor.putString(sessionToken.getId(), sessionTokenValue);
            editor.commit();
            GoToMainActivity();
        } catch (JSONException e) {
            this.onErrorHappened(CodeParams.ERROR_SAVE_SESSION_TOKEN, e.toString());
        }
    }

    @Override
    public void onErrorHappened(int flag, String errorMessage) {
        Log.d("Error message when login: ", errorMessage);
        String errorCodeMessage = mErrorMessageFactory.get(flag);
        CustomAlertDialog dialog = new CustomAlertDialog(this,
                errorCodeMessage);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }
}

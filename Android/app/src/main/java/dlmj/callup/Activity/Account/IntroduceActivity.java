package dlmj.callup.Activity.Account;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import dlmj.callup.R;

/**
 * Created by Two on 15/7/27.
 */
public class IntroduceActivity extends Activity{
    private Button mLoginButton;
    private Button mRegisterButton;
    private Dialog mLoginDialog;

    @Override
    public void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.introduce);
        findView();
        setListener();
    }

    private void findView() {
        mLoginButton = (Button)findViewById(R.id.loginButton);
        mRegisterButton = (Button)findViewById(R.id.registerButton);

        mLoginDialog = new Dialog(IntroduceActivity.this, R.style.LoginAlertDialog);
    }

    private void setListener() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View loginView = LayoutInflater.from(IntroduceActivity.this).inflate(R.layout.login, null);
                mLoginDialog.setContentView(loginView);
                mLoginDialog.show();

                mLoginButton.setVisibility(View.INVISIBLE);
                mRegisterButton.setVisibility(View.INVISIBLE);
            }
        });

        mLoginDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mLoginButton.setVisibility(View.VISIBLE);
                mRegisterButton.setVisibility(View.VISIBLE);
            }
        });
    }
}

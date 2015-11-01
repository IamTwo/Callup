package dlmj.callup.UI.Activity.Account;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dlmj.callup.BusinessLogic.Cache.UserCache;
import dlmj.callup.R;
import dlmj.callup.UI.Activity.CallUpActivity;

/**
 * Created by Two on 15/9/30.
 */
public class AccountActivity extends CallUpActivity {
    private TextView mAccountTextView;
    private ImageView mBackImageView;
    private TextView mBackTextView;

    @Override
    public void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.account);

        findView();
        setListener();
    }

    private void findView() {
        mBackImageView = (ImageView) findViewById(R.id.backImageView);
        mBackTextView = (TextView) findViewById(R.id.backTextView);
        mAccountTextView = (TextView) findViewById(R.id.accountTextView);
        mAccountTextView.setText(UserCache.getInstance().getClientUser().getUserName());
    }

    private void setListener() {
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

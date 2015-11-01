package dlmj.callup.UI.Activity.Account;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dlmj.callup.R;
import dlmj.callup.UI.Activity.CallUpActivity;

/**
 * Created by Two on 15/9/30.
 */
public class AboutActivity extends CallUpActivity {
    private ImageView mBackImageView;
    private TextView mBackTextView;

    @Override
    public void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.about);
        findView();
        setListener();
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

    private void findView() {
        mBackImageView = (ImageView) findViewById(R.id.backImageView);
        mBackTextView = (TextView) findViewById(R.id.backTextView);
    }
}

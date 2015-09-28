package dlmj.callup.UI.Activity.Account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import dlmj.callup.BusinessLogic.Cache.ImageCacheManager;
import dlmj.callup.Common.Model.Friend;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.R;
import dlmj.callup.UI.Activity.FriendBomb.HistoryActivity;
import dlmj.callup.UI.View.CircleImageView;

/**
 * Created by Two on 15/9/21.
 */
public class ProfileActivity extends Activity {
    private Friend mCurrentFriend;
    private CircleImageView mPhotoImageView;
    private TextView mNameTextView;
    private TextView mAccountTextView;
    private ImageView mBackImageView;
    private TextView mBackTextView;
    private ImageLoader mImageLoader;
    private Button mSendBombsButton;

    @Override
    public void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.profile);
        InitializeData();
        findView();
        setListener();
    }

    private void InitializeData() {
        mCurrentFriend = (Friend)getIntent().getSerializableExtra(IntentExtraParams.FRIEND);
        mImageLoader = ImageCacheManager.getInstance(this).getImageLoader();
    }

    private void findView() {
        mNameTextView = (TextView) findViewById(R.id.nameTextView);
        mNameTextView.setText(mCurrentFriend.getName());
        mAccountTextView = (TextView) findViewById(R.id.accountTextView);
        mAccountTextView.setText(String.format(
                getString(R.string.account_info),
                mCurrentFriend.getAccount()));
        mBackImageView = (ImageView) findViewById(R.id.backImageView);
        mBackTextView = (TextView) findViewById(R.id.backTextView);
        mPhotoImageView = (CircleImageView) findViewById(R.id.photoImageView);
        mPhotoImageView.setImageUrl(mCurrentFriend.getFaceUrl(), mImageLoader);
        mPhotoImageView.setDefaultImageResId(R.drawable.default_photo);

        mSendBombsButton = (Button) findViewById(R.id.sendBombsButton);
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

        mSendBombsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
                intent.putExtra(IntentExtraParams.FRIEND, mCurrentFriend);
                startActivity(intent);
            }
        });
    }
}

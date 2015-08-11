package dlmj.callup.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import dlmj.callup.Activity.SelfSave.SetLevelActivity;
import dlmj.callup.R;


public class MainActivity extends Activity {
    private Button mSelfSaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        findView();
        setListener();
    }

    public void findView(){
        mSelfSaveBtn = (Button)findViewById(R.id.self_save_btn);
    }

    public void setListener(){
        mSelfSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SetLevelActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

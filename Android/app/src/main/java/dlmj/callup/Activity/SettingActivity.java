package dlmj.callup.Activity;

import android.app.Activity;
import android.os.Bundle;

import dlmj.callup.R;

/**
 * Created by Two on 15/8/2.
 */
public class SettingActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.setting);
        findView();
    }

    private void findView() {

    }
}

package dlmj.callup.UI.Activity.Account;

import android.app.Activity;
import android.os.Bundle;

import dlmj.callup.R;
import dlmj.callup.UI.Activity.CallUpActivity;

/**
 * Created by Two on 15/8/2.
 */
public class RegisterActivity extends CallUpActivity {
    @Override
    public void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.register);
    }
}

package dlmj.callup.UI.Activity;

import android.app.Activity;
import android.os.Bundle;

import dlmj.callup.CallUpApplication;

/**
 * Created by Two on 15/9/29.
 */
public class CallUpActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        CallUpApplication.getInstance().initialize();
    }
}

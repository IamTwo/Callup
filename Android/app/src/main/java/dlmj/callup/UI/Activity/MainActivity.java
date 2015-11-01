package dlmj.callup.UI.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import dlmj.callup.CallUpApplication;
import dlmj.callup.UI.Fragment.MenuFragment;
import dlmj.callup.R;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CallUpApplication.getInstance().initialize();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new MenuFragment()).commit();
    }
}

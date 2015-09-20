package dlmj.callup.UI.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import dlmj.callup.UI.Activity.MenuActivity;

/**
 * Created by Two on 15/8/25.
 */
public class CallUpFragment extends Fragment{
    protected Button mMenuButton;
    private View.OnClickListener mMenuClickListener;

    protected void setListener(){
        mMenuButton.setOnClickListener(mMenuClickListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mMenuClickListener = ((MenuActivity)activity).menuClickListener;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement MenuClickListener");
        }
    }
}

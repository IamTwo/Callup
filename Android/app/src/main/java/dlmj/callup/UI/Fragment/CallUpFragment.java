package dlmj.callup.UI.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import dlmj.callup.UI.Activity.MenuActivity;

/**
 * Created by Two on 15/8/25.
 */
public class CallUpFragment extends Fragment{
    protected Button mMenuButton;
    private View.OnClickListener mMenuClickListener;

    protected void setListener(){
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                mMenuClickListener.onClick(view);
            }
        });
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

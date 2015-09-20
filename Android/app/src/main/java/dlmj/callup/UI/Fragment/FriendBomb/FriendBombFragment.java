package dlmj.callup.UI.Fragment.FriendBomb;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import dlmj.callup.R;
import dlmj.callup.UI.Fragment.CallUpFragment;

/**
 * Created by Two on 15/9/15.
 */
public class FriendBombFragment extends CallUpFragment {
    private FragmentTabHost mFragmentTabHost;
    private int mTabImage[] = {R.drawable.bubble, R.drawable.friend};
    private int mTabText[] = {R.string.bombs, R.string.friends};
    private Class mFragmentArray[] = {BombFragment.class, FriendFragment.class};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friend_bomb, null);
        findView(view);
        setListener();
        return view;
    }

    public void findView(View view) {
        mMenuButton = (Button) view.findViewById(R.id.menuButton);
        mFragmentTabHost = (FragmentTabHost)view.findViewById(R.id.tabHost);
        mFragmentTabHost.setup(getActivity(), getActivity().getSupportFragmentManager(),
                R.id.mainContent);

        for(int i = 0; i < mTabText.length; i++) {
            TabHost.TabSpec spec = mFragmentTabHost
                    .newTabSpec(getActivity().getString(mTabText[i]))
                    .setIndicator(getView(i));
            mFragmentTabHost.addTab(spec, mFragmentArray[i], null);
            mFragmentTabHost.getTabWidget().getChildAt(i)
                    .setBackgroundResource(R.drawable.bottom_menu_back);
        }
    }

    public void setListener(){
        super.setListener();
    }

    private View getView(int index) {
        View view = View.inflate(getActivity(), R.layout.tab_content, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView = (TextView)view.findViewById(R.id.text);

        imageView.setImageResource(mTabImage[index]);
        textView.setText(mTabText[index]);

        return view;
    }
}

package dlmj.callup.Common.Factory;

import android.content.Context;
import android.support.v4.app.Fragment;

import dlmj.callup.Common.Exception.NoFragmentFoundException;
import dlmj.callup.UI.Fragment.Account.SettingFragment;
import dlmj.callup.UI.Fragment.FriendBomb.FriendBombFragment;
import dlmj.callup.UI.Fragment.SelfSave.SetAlarmFragment;
import dlmj.callup.UI.Fragment.SelfSave.SetSceneFragment;

/**
 * Created by Two on 15/9/15.
 */
public class FragmentFactory {
    public static enum FragmentName{
        SetAlarm, SetScene, FriendBomb, Settings
    }

    private Context mContext;

    public FragmentFactory(Context context) {
        mContext = context;
    }

    public Fragment get(FragmentName fragmentName) throws NoFragmentFoundException {
        switch(fragmentName) {
            case SetAlarm:
                return new SetAlarmFragment();
            case SetScene:
                return new SetSceneFragment();
            case FriendBomb:
                return new FriendBombFragment();
            case Settings:
                return new SettingFragment();
            default:
                throw new NoFragmentFoundException(mContext, fragmentName);
        }
    }
}

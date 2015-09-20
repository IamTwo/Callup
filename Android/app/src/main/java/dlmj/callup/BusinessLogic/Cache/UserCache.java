package dlmj.callup.BusinessLogic.Cache;

import android.content.SharedPreferences;
import android.text.TextUtils;

import dlmj.callup.Common.Model.ClientUser;
import dlmj.callup.Common.Params.SharedPreferenceSettings;
import dlmj.callup.Common.Util.CallUpPreferences;

/**
 * Created by Two on 15/9/8.
 */
public class UserCache {
    private static ClientUser mClientUser;

    public static void setClientUser(ClientUser clientUser) {
        mClientUser = clientUser;
    }

    public static ClientUser getClientUser() {
        if(mClientUser != null) {
            return mClientUser;
        }
        String registerAccount = getAutoRegisterAccount();
        if(!TextUtils.isEmpty(registerAccount)) {
            mClientUser = new ClientUser();
            return mClientUser.from(registerAccount);
        }
        return null;
    }

    private static String getAutoRegisterAccount() {
        SharedPreferences sharedPreferences = CallUpPreferences.getSharedPreferences();
        SharedPreferenceSettings accountInfo = SharedPreferenceSettings.ACCOUNT_INFO;
        String registerAccount = sharedPreferences.getString(accountInfo.getId(),
                (String) accountInfo.getDefaultValue());
        return registerAccount;
    }
}

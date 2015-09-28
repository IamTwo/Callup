package dlmj.callup.BusinessLogic.Cache;

import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import dlmj.callup.Common.Model.ClientUser;
import dlmj.callup.Common.Params.SharedPreferenceSettings;
import dlmj.callup.Common.Util.CallUpPreferences;
import dlmj.callup.Common.Util.LogUtil;

/**
 * Created by Two on 15/9/8.
 */
public class UserCache {
    private final static String TAG = "UserCache";
    private ClientUser mClientUser;
    private static UserCache mInstance;
    private SharedPreferences mSharedPreferences;

    private UserCache() {
        mSharedPreferences = CallUpPreferences.getSharedPreferences();
    }

    public static UserCache getInstance(){
        synchronized (UserCache.class){
            if(mInstance == null){
                mInstance = new UserCache();
            }
        }
        return mInstance;
    }

    public void setClientUser(String userInfoStr) {
        mClientUser = from(userInfoStr);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        SharedPreferenceSettings sessionToken = SharedPreferenceSettings.SESSION_TOKEN;
        SharedPreferenceSettings accountInfo = SharedPreferenceSettings.ACCOUNT_INFO;
        editor.putString(sessionToken.getId(), mClientUser.getSid());
        editor.putString(accountInfo.getId(), userInfoStr);
        editor.commit();
    }

    public ClientUser getClientUser() {
        if(mClientUser != null) {
            return mClientUser;
        }

        SharedPreferenceSettings accountInfo = SharedPreferenceSettings.ACCOUNT_INFO;
        String userInfoStr = mSharedPreferences.getString(accountInfo.getId(),
                accountInfo.getDefaultValue().toString());

        return from(userInfoStr);
    }

    public ClientUser from(String userInfoStr) {
        try {
            JSONObject userInfo = new JSONObject(userInfoStr);
            return new ClientUser(
                    userInfo.getInt("id"), userInfo.getString("sid"),
                    userInfo.getString("Name"), userInfo.getString("Location"),
                    userInfo.getInt("Level"), userInfo.getString("UserState"),
                    userInfo.getString("Face"), userInfo.getString("SmallFace")
            );
        } catch (JSONException e) {
            LogUtil.e(TAG, e.getMessage());
            return null;
        }
    }
}

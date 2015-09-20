package dlmj.callup.Common.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Two on 15/9/8.
 */
public class ClientUser {
    private String mAccount;
    private String mUserName;
    private String mAppKey;
    private String mAppToken;

    public String getAccount() {
        return mAccount;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getAppKey() {
        return mAppKey;
    }

    public String getAppToken() {
        return mAppToken;
    }

    public ClientUser from(String input) {
        JSONObject object = null;
        try {
            object = new JSONObject(input);
            if(object.has("account")) {
                this.mAccount = object.getString("account");
            }
            if(object.has("userName")) {
                this.mUserName = object.getString("userName");
            }
            if(object.has("appKey")) {
                this.mAppKey = object.getString("appKey");
            }
            if(object.has("appToken")) {
                this.mAppToken = object.getString("appToken");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
}

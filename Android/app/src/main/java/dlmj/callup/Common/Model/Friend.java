package dlmj.callup.Common.Model;

import java.io.Serializable;

/**
 * Created by Two on 15/9/8.
 */
public class Friend implements Serializable{
    private int mUserId;
    private String mFaceUrl;
    private String mName;
    private String mAccount;

    public Friend(int userId, String faceUrl, String account, String name) {
        this.mUserId = userId;
        this.mFaceUrl = faceUrl;
        this.mAccount = account;
        this.mName = name;
    }

    public int getUserId() {
        return mUserId;
    }

    public String getFaceUrl() {
        return mFaceUrl;
    }

    public String getName() {
        return mName;
    }

    public String getAccount() {
        return mAccount;
    }
}

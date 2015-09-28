package dlmj.callup.Common.Model;

/**
 * Created by Two on 15/9/8.
 */
public class ClientUser {
    private int mUserId;
    private String mSid;
    private String mName;
    private String mLocation;
    private int mLevel;
    private String mUserState;
    private String mImageUrl;
    private String mFaceUrl;

    public ClientUser(int userId, String sid, String name, String location,
                      int level, String userState, String imageUrl,
                      String faceUrl) {
        mUserId = userId;
        mSid = sid;
        mName = name;
        mLocation = location;
        mLevel = level;
        mUserState = userState;
        mImageUrl = imageUrl;
        mFaceUrl = faceUrl;
    }

    public int getUserId() {
        return mUserId;
    }

    public String getSid() {
        return mSid;
    }

    public String getFaceUrl() {
        return mFaceUrl;
    }

    public String getUserName() {
        return mName;
    }
}

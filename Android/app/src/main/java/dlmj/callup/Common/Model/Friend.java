package dlmj.callup.Common.Model;

/**
 * Created by Two on 15/9/8.
 */
public class Friend {
    private String mImageUrl;
    private String mName;

    public Friend(String imageUrl, String name) {
        this.mImageUrl = imageUrl;
        this.mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getName() {
        return mName;
    }
}

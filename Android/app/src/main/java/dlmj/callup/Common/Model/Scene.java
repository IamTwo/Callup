package dlmj.callup.Common.Model;

import java.io.Serializable;

/**
 * Created by Two on 15/8/10.
 */
public class Scene implements Serializable {
    private int mSceneId;
    private String mName;
    private String mLogoUrl;
    private String mImageUrl;
    private String mAudioUrl;

    public Scene(String name){
        mName = name;
    }

    public Scene(int sceneId, String name, String logoUrl, String imageUrl, String audioUrl){
        mSceneId = sceneId;
        mName = name;
        mLogoUrl = logoUrl;
        mImageUrl = imageUrl;
        mAudioUrl = audioUrl;
    }

    public int getSceneId() {
        return mSceneId;
    }

    public String getName() {
        return mName;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getAudioUrl() {
        return mAudioUrl;
    }
}

package dlmj.callup.Common.Model;

import android.content.Context;

import java.io.Serializable;

import dlmj.callup.Common.Util.StringUtil;

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

    public String getName(Context context) {
        return StringUtil.getStringFromResource(context, mName);
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

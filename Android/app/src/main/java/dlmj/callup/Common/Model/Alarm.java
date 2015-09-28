package dlmj.callup.Common.Model;

import java.io.Serializable;

/**
 * Created by Two on 15/8/23.
 */
public class Alarm implements Serializable {
    private int mAlarmId;
    private int mSceneId;
    private String mLogoUrl;
    private String mImageUrl;
    private String mSceneName;
    private String mTime;
    private String mFrequent;
    private String mAudioUrl;

    public Alarm(int alarmId, int sceneId, String logoUrl, String imageUrl,
                 String sceneName, String time, String frequent, String audioUrl){
        mAlarmId = alarmId;
        mSceneId = sceneId;
        mLogoUrl = logoUrl;
        mImageUrl = imageUrl;
        mSceneName = sceneName;
        mTime = time;
        mFrequent = frequent;
        mAudioUrl = audioUrl;
    }

    public int getAlarmId() {
        return mAlarmId;
    }

    public int getSceneId() {
        return mSceneId;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    public String getSceneName(){
        return mSceneName;
    }

    public String getTime() {
        return mTime;
    }

    public String getFrequent() {
        return mFrequent;
    }

    public String getAudioUrl() {
        return mAudioUrl;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public boolean isRepeat() {
        return !mFrequent.equals("0000000");
    }
}

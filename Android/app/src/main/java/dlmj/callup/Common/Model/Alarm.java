package dlmj.callup.Common.Model;

/**
 * Created by Two on 15/8/23.
 */
public class Alarm {
    private int mAlarmId;
    private int mSceneId;
    private String mLogoUrl;
    private String mImageUrl;
    private String mSceneName;
    private String mTime;
    private String mFrequent;

    public Alarm(int alarmId, int sceneId, String logoUrl, String imageUrl,
                 String sceneName, String time, String frequent){
        mAlarmId = alarmId;
        mSceneId = sceneId;
        mLogoUrl = logoUrl;
        mImageUrl = imageUrl;
        mSceneName = sceneName;
        mTime = time;
        mFrequent = frequent;
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
}

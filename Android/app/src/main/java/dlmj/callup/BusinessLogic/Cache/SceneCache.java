package dlmj.callup.BusinessLogic.Cache;

import dlmj.callup.Common.Model.Scene;

/**
 * Created by Two on 15/8/29.
 */
public class SceneCache extends BaseCache<Scene>{
    private static SceneCache mInstance;

    public SceneCache(){
        super();
    }

    public static final SceneCache getInstance(){
        synchronized (SceneCache.class){
            if(mInstance == null){
                mInstance = new SceneCache();
            }
        }
        return mInstance;
    }

    public Scene getScene(int sceneId) {
        for(Scene scene : mList) {
            if(scene.getSceneId() == sceneId) {
                return scene;
            }
        }

        return null;
    }
}

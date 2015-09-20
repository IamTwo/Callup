package dlmj.callup.BusinessLogic.Cache;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import dlmj.callup.Common.Model.Scene;

/**
 * Created by Two on 15/8/29.
 */
public class SceneCache {
    private static SceneCache mInstance;
    private List<Scene> mSceneList;

    public SceneCache(){
        mSceneList = new LinkedList<>();
    }

    public static final SceneCache getInstance(Context context){
        synchronized (SceneCache.class){
            if(mInstance == null){
                mInstance = new SceneCache();
            }
        }
        return mInstance;
    }

    public void setSceneList(List<Scene> sceneList){
        mSceneList = sceneList;
    }

    public List<Scene> getSceneList(){
        return mSceneList;
    }
}

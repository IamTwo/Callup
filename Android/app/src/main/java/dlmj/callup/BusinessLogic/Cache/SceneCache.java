package dlmj.callup.BusinessLogic.Cache;

import android.content.Context;

import java.util.List;

import dlmj.callup.Common.Model.Scene;
import dlmj.callup.DataAccess.SceneTableManager;

/**
 * Created by Two on 15/8/29.
 */
public class SceneCache extends BaseCache<Scene>{
    private static SceneCache mInstance;
    private Context mContext;

    public SceneCache(Context context){
        super();
        mContext = context;
    }

    public static final SceneCache getInstance(Context context){
        synchronized (SceneCache.class){
            if(mInstance == null){
                mInstance = new SceneCache(context);
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

        return SceneTableManager.getInstance(mContext).getScene(sceneId);
    }

    public List<Scene> getList() {
        if(super.getList().size() > 0) {
            return super.getList();
        }

        return SceneTableManager.getInstance(mContext).getScenes();
    }

    public void setList(List<Scene> scenes) {
        super.setList(scenes);
        synchronized (SceneTableManager.class){
            SceneTableManager.getInstance(mContext).saveScenes(scenes);
        }
    }

    public void clear() {
        mList.clear();

        SceneTableManager.getInstance(mContext).clear();
    }
}

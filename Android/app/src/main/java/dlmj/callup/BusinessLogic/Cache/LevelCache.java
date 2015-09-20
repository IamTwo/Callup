package dlmj.callup.BusinessLogic.Cache;

import android.content.Context;
import android.content.res.TypedArray;

import java.util.LinkedList;
import java.util.List;

import dlmj.callup.Common.Model.Level;
import dlmj.callup.R;

/**
 * Created by Two on 15/8/11.
 */
public class LevelCache {
    private static LevelCache mInstance;

    /**
     * The list of level items.
     */
    private List<Level> mLevels;

    public static final LevelCache getInstance(Context context){
        synchronized (LevelCache.class){
            if(mInstance == null){
                mInstance = new LevelCache(context);
            }
        }
        return mInstance;
    }

    public LevelCache(Context context){
        initializeLevelList(context);
    }

    /**
     * Initialize the list of level items.
     * @param context
     */
    private void initializeLevelList(Context context) {
        mLevels = new LinkedList();
        TypedArray imageArray = context.getResources().obtainTypedArray(R.array.level_image);
        TypedArray strArray = context.getResources().obtainTypedArray(R.array.level_str);

        for(int i = 0; i < imageArray.length(); i++){
            mLevels.add(new Level(imageArray.getResourceId(i, 0), strArray.getResourceId(i, 0)));
        }

        imageArray.recycle();
        strArray.recycle();
    }

    public List<Level> getLevels(){
        return mLevels;
    }
}

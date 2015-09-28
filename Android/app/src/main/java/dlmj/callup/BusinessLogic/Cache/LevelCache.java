package dlmj.callup.BusinessLogic.Cache;

import android.content.Context;
import android.content.res.TypedArray;

import dlmj.callup.Common.Model.Level;
import dlmj.callup.R;

/**
 * Created by Two on 15/8/11.
 */
public class LevelCache extends BaseCache<Level>{
    private static LevelCache mInstance;

    public static final LevelCache getInstance(Context context){
        synchronized (LevelCache.class){
            if(mInstance == null){
                mInstance = new LevelCache(context);
            }
        }
        return mInstance;
    }

    public LevelCache(Context context){
        super();
        TypedArray imageArray = context.getResources().obtainTypedArray(R.array.level_image);
        TypedArray strArray = context.getResources().obtainTypedArray(R.array.level_str);

        for(int i = 0; i < imageArray.length(); i++){
            addItem(new Level(imageArray.getResourceId(i, 0), strArray.getResourceId(i, 0)));
        }

        imageArray.recycle();
        strArray.recycle();
    }
}

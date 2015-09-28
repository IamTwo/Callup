package dlmj.callup.BusinessLogic.Cache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;


import dlmj.callup.BusinessLogic.Network.VolleyQueueController;

/**
 * Created by Two on 15/8/16.
 */
public class ImageCacheManager implements ImageLoader.ImageCache{
    private ImageLoader mImageLoader;
    private static ImageCacheManager mInstance;
    private BitmapCache mBitmapCache;
    private static final String DISK_CACHE_DIR = "CallUp";

    private ImageCacheManager(Context context){
        int maxSize = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass() * 1024 * 1024 / 8;
        mBitmapCache = new BitmapCache(maxSize);
        VolleyQueueController controller = VolleyQueueController.getInstance(context);
        mImageLoader = new ImageLoader(controller.getRequestQueue(), mBitmapCache);
    }

    public static ImageCacheManager getInstance(Context context) {
        synchronized (ImageCacheManager.class) {
            if (mInstance == null) {
                mInstance = new ImageCacheManager(context);
            }
        }
        return mInstance;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mBitmapCache.getBitmap(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mBitmapCache.put(url, bitmap);
    }
}

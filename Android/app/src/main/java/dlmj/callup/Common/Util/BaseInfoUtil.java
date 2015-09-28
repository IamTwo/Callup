package dlmj.callup.Common.Util;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.File;

/**
 * Created by Two on 15/8/28.
 */
public class BaseInfoUtil {
    private static Context mContext = null;

    public static String getAppName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String appName = context.getApplicationInfo().loadLabel(packageManager).toString();
        return appName;
    }

    public static String getDownloadMusicPath(Context context) {
        return File.separator + getAppName(context) + File.separator;
    }

    public static boolean ifFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static Context getContext(){
        return mContext;
    }
}

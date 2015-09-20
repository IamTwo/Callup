package dlmj.callup.Common.Util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

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
        return "/";
    }

    public static boolean ifFileExists(String Path) {
        File file = new File(Path);

        return file.exists();
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static Context getContext(){
        return mContext;
    }
}

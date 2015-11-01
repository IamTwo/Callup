package dlmj.callup.Common.Util;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * Created by Two on 15/9/10.
 */
public class StringUtil {
    public static int[] StringToIntArray(String string) {
        int num[] = new int[string.length()];
        String temp;
        for(int i = 0;i < string.length(); i++) {
            temp = "" + string.charAt(i);
            num[i] = Integer.parseInt(temp);
        }
        return num;
    }

    public static String getStringFromResource(Context context, String key) {
        try{
            int stringId = context.getResources().getIdentifier(key, "string", context.getPackageName());
            return context.getResources().getString(stringId);
        }catch(Exception ex) {
            return key;
        }
    }
}

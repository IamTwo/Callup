package dlmj.callup.Common.Util;

import android.content.Context;
import android.content.SharedPreferences;

import dlmj.callup.CallUpApplication;
import dlmj.callup.Common.Params.SharedPreferenceParams;

/**
 * Created by Two on 15/9/8.
 */
public class CallUpPreferences {
    public static SharedPreferences getSharedPreferences() {
        CallUpApplication application = CallUpApplication.getInstance();
        return application.getSharedPreferences(
                getDefaultSharedPreferencesFileName(), Context.MODE_MULTI_PROCESS);
    }

    public static String getDefaultSharedPreferencesFileName() {
        return SharedPreferenceParams.CALL_UP_FILE;
    }
}

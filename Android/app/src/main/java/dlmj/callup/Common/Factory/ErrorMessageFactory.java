package dlmj.callup.Common.Factory;

import android.content.Context;
import android.content.res.Resources;

import dlmj.callup.Common.Params.CodeParams;
import dlmj.callup.R;

/**
 * Created by Two on 15/8/22.
 */
public class ErrorMessageFactory {
    Resources mResources;

    public ErrorMessageFactory(Context context){
        mResources = context.getResources();
    }
    public String get(int errorCode){
        switch(errorCode){
            case CodeParams.ERROR_LOGIN_FAILED:
                return mResources.getString(R.string.error_login_failed);
            default:
                return mResources.getString(R.string.error_server_connection_failed);
        }
    }
}

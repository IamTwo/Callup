package dlmj.callup;

import android.app.Application;

import com.yuntongxun.ecsdk.ECDevice;

import dlmj.callup.BusinessLogic.IM.SDKCoreHelper;
import dlmj.callup.Common.Util.BaseInfoUtil;
import dlmj.callup.Common.Util.LogUtil;

/**
 * Created by Two on 15/9/8.
 */
public class CallUpApplication extends Application {
    private static CallUpApplication mInstance;

    public static CallUpApplication getInstance() {
        if (mInstance == null) {
            LogUtil.w("[CallUpApplication] instance is null.");
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        BaseInfoUtil.setContext(mInstance);
        SDKCoreHelper.getInstance();

        if (SDKCoreHelper.getConnectState() != ECDevice.ECConnectState.CONNECT_SUCCESS) {
//            ContactsCache.getInstance().load();
            SDKCoreHelper.initialize(this);
        }
    }
}

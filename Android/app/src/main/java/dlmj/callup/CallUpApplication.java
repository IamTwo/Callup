package dlmj.callup;

import android.app.Application;

import com.yuntongxun.ecsdk.ECDevice;

import java.util.List;

import dlmj.callup.BusinessLogic.Alarm.AlarmHelper;
import dlmj.callup.BusinessLogic.Alarm.AlarmSetManager;
import dlmj.callup.BusinessLogic.Cache.FriendCache;
import dlmj.callup.BusinessLogic.Cache.HistoryCache;
import dlmj.callup.BusinessLogic.Cache.UserCache;
import dlmj.callup.BusinessLogic.IM.SDKCoreHelper;
import dlmj.callup.Common.Model.Friend;
import dlmj.callup.Common.Util.BaseInfoUtil;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.DataAccess.AlarmTableManager;

/**
 * Created by Two on 15/9/8.
 */
public class CallUpApplication extends Application {
    private final static String TAG = "CallUpApplication";
    private static CallUpApplication mInstance;

    public static CallUpApplication getInstance() {
        if (mInstance == null) {
            LogUtil.w(TAG, "[CallUpApplication] instance is null.");
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        BaseInfoUtil.setContext(mInstance);
        SDKCoreHelper.getInstance();
        AlarmHelper.getInstance(this);
        initialize();
    }

    public void initialize() {
        if (SDKCoreHelper.getConnectState() != ECDevice.ECConnectState.CONNECT_SUCCESS) {
            if(UserCache.getInstance().getClientUser() != null) {
                SDKCoreHelper.initialize(this);
            }
        }
    }
}

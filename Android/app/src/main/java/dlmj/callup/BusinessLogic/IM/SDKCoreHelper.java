package dlmj.callup.BusinessLogic.IM;

import android.content.Context;
import android.content.Intent;

import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;

import dlmj.callup.BusinessLogic.Cache.UserCache;
import dlmj.callup.Common.Model.ClientUser;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.Common.Util.ToastUtil;
import dlmj.callup.R;

/**
 * Created by Two on 15/9/8.
 */
public class SDKCoreHelper implements ECDevice.InitListener, ECDevice.OnECDeviceConnectListener{
    private static final String TAG = "SDKCoreHelper";
    public static final String ACTION_SDK_CONNECT = "ACTION_SDK_CONNECT";
    private static final String APP_ID = "8a48b5514fa577af014fab3dfd6e10c8";
    private static final String APP_TOKEN = "15e348354efa66a908627048a7096b64";
    private ECInitParams mInitParams;
    private ECInitParams.LoginMode mMode = ECInitParams.LoginMode.FORCE_LOGIN;
    private static SDKCoreHelper mInstance;
    private Context mContext;
    private boolean mIfKickOff = false;
    private ECDevice.ECConnectState mConnect = ECDevice.ECConnectState.CONNECT_FAILED;

    public static SDKCoreHelper getInstance() {
        if (mInstance == null) {
            mInstance = new SDKCoreHelper();
        }
        return mInstance;
    }

    public static void initialize(Context context) {
        initialize(context, ECInitParams.LoginMode.AUTO);
    }

    public static void initialize(Context context, ECInitParams.LoginMode mode) {
        mInstance.mIfKickOff = false;
        LogUtil.d(TAG, "[initialize] start registering..");
        mInstance.mContext = context;

        if(!ECDevice.isInitialized()) {
            mInstance.mConnect = ECDevice.ECConnectState.CONNECTING;
            ECDevice.initial(context, mInstance);

            return;
        }

        LogUtil.d(TAG, " SDK has initialized , then register..");

        mInstance.onInitialized();
    }

    public static ECDevice.ECConnectState getConnectState() {
        return mInstance.mConnect;
    }

    @Override
    public void onInitialized() {
        LogUtil.d(TAG, "ECSDK is ready");
        ClientUser clientUser = UserCache.getInstance().getClientUser();
        if(mInitParams == null || mInitParams.getInitParams() == null
                || mInitParams.getInitParams().isEmpty()) {
            mInitParams = new ECInitParams();
        }

        mInitParams.reset();
        mInitParams.setUserid(clientUser.getUserId() + "");
        mInitParams.setAppKey(APP_ID);
        mInitParams.setToken(APP_TOKEN);
        mInitParams.setMode(mMode);

        if(!mInitParams.validate()) {
            ToastUtil.showMessage(R.string.register_params_error);
            Intent intent = new Intent(ACTION_SDK_CONNECT);
            intent.putExtra("error", -1);
            mContext.sendBroadcast(intent);
            return ;
        }

        mInitParams.setOnChatReceiveListener(IMChattingHelper.getInstance(mContext).getOnChatReceiverListener());
        mInitParams.setOnDeviceConnectListener(this);
        ECDevice.login(mInitParams);
    }

    @Override
    public void onError(Exception e) {
        LogUtil.e(TAG, e.getMessage());
    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onDisconnect(ECError ecError) {

    }

    @Override
    public void onConnectState(ECDevice.ECConnectState ecConnectState, ECError ecError) {

    }
}

package dlmj.callup.Common.Interfaces;

import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;

import java.util.List;

/**
 * Created by Two on 15/9/24.
 */
public interface OnMessageReportCallback {
    void onMessageReport(ECError error ,ECMessage message);
    void onPushMessage();
}

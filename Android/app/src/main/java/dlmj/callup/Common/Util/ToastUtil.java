package dlmj.callup.Common.Util;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import dlmj.callup.CallUpApplication;

/**
 * Created by Two on 15/9/8.
 */
public class ToastUtil {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Object synObj = new Object();
    private static Toast toast = null;

    public static void showMessage(final String message) {
        showMessage(message, Toast.LENGTH_SHORT);
    }

    public static void showMessage(final int messageResource) {
        showMessage(messageResource, Toast.LENGTH_SHORT);
    }

    /**
     * Show the message in the resources.
     * @param messageResource
     * @param length
     */
    public static void showMessage(final int messageResource, final int length) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) {
                    if (toast != null) {
                        //toast.cancel();
                        toast.setText(messageResource);
                        toast.setDuration(length);
                    } else {
                        toast = Toast.makeText(CallUpApplication.getInstance().getApplicationContext(),
                                messageResource, length);
                    }
                    toast.show();
                }
            }
        });
    }

    /**
     * Show the message directly.
     * @param message
     * @param length
     */
    public static void showMessage(final CharSequence message, final int length) {
        if(message == null || message.equals("")){
            LogUtil.w("[ToastUtil] response message is null");
            return;
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) { //Ensure every toast can be showed.
                    if (toast != null) {
                        toast.setText(message);
                        toast.setDuration(length);
                    } else {
                        toast = Toast.makeText(CallUpApplication.getInstance().getApplicationContext(),
                                message, length);
                    }
                    toast.show();
                }
            }
        });
    }
}

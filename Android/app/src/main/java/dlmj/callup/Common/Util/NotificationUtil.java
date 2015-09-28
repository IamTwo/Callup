package dlmj.callup.Common.Util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

/**
 * Created by Two on 15/9/25.
 */
public class NotificationUtil {
    public static final String TAG = "NotificationUtil";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Notification buildNotification(Context context, int icon,
                                                 String contentTitle, String contentText,
                                                 PendingIntent intent) {

//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
//            Notification.Builder builder = new Notification.Builder(context);
//            builder.setLights(-16711936, 300, 1000)
//                    .setSmallIcon(icon)
//                    .setTicker(tickerText)
//                    .setContentTitle(contentTitle)
//                    .setContentText(contentText)
//                    .setContentIntent(intent);
//
//            builder.setDefaults(Notification.DEFAULT_VIBRATE);
//            if(largeIcon != null) {
//                builder.setLargeIcon(largeIcon);
//            }
//            return builder.getNotification();
//        }

        Notification notification = new Notification();
        notification.ledARGB = -16711936;
        notification.ledOnMS = 300;
        notification.ledOffMS = 1000;
        notification.flags = (Notification.FLAG_SHOW_LIGHTS | notification.flags);
        notification.icon = icon;
//        notification.tickerText = tickerText;
        notification.defaults = Notification.DEFAULT_VIBRATE;
        notification.setLatestEventInfo(context, contentTitle, contentText, intent);
        return notification;
    }
}

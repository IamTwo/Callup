package dlmj.callup.BusinessLogic.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dlmj.callup.BroadcastReceiver.AlarmReceiver;
import dlmj.callup.BroadcastReceiver.BombReceiver;
import dlmj.callup.BusinessLogic.Cache.AlarmCache;
import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Model.Friend;
import dlmj.callup.Common.Model.History;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Util.LogUtil;

/**
 * Created by Two on 15/9/28.
 */
public class AlarmSetManager {
    private static String TAG = "AlarmManager";
    public static void setAlarm(Context context, History history, Friend friend) {
        try {
            Intent intent = new Intent(context, BombReceiver.class);
            intent.putExtra(IntentExtraParams.SCENE, history.getScene());
            intent.putExtra(IntentExtraParams.TIME, history.getTime());
            intent.putExtra(IntentExtraParams.FRIEND, friend);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    (int)history.getHistoryId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(history.getTime());
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(),
                    pendingIntent);
        } catch (ParseException e) {
            LogUtil.e(TAG, e.getMessage());
        }
    }

    public static void setAlarm(Context context, Alarm alarm) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(IntentExtraParams.ALARM, alarm);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                alarm.getAlarmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = CalendarHelper.getInstance().getNextCalendar(alarm);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                pendingIntent);
    }

    public static void clear(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        for(Alarm alarm : AlarmCache.getInstance(context).getList()) {
            intent.putExtra(IntentExtraParams.ALARM, alarm);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    alarm.getSceneId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }

    public static void removeAlarm(Context context, Alarm alarm) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(IntentExtraParams.ALARM, alarm);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                alarm.getSceneId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}

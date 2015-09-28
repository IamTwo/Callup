package dlmj.callup.BusinessLogic.Alarm;

import java.util.Calendar;

import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Model.AlarmTime;

/**
 * Created by Two on 15/9/25.
 */
public class CalendarHelper {
    private static CalendarHelper mInstance;

    public static CalendarHelper getInstance() {
        if (mInstance == null) {
            mInstance = new CalendarHelper();
        }
        return mInstance;
    }

    public Calendar getNextCalendar(Alarm alarm) {
        String time = alarm.getTime();
        String frequent = alarm.getFrequent();
        Calendar calendar = Calendar.getInstance();
        AlarmTime alarmTime = new AlarmTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, alarmTime.getHour());
        calendar.set(Calendar.MINUTE, alarmTime.getMinute());

        if(alarm.isRepeat()) {
            updateDate(calendar, frequent);
        }

        return calendar;
    }

    private void updateDate(Calendar calendar, String frequent) {
        int weekDay = getCurrentWeekDay(calendar);

        for(int i = weekDay - 1;;i++) {
            if(i >= 7) {
                i = 0;
            }

            if(Integer.parseInt(frequent.charAt(i) + "") > 0) {
                return;
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

    }

    private int getCurrentWeekDay(Calendar calendar) {
        boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        if(isFirstSunday){
            weekDay = weekDay - 1;
            if(weekDay == 0){
                weekDay = 7;
            }
        }

        return weekDay;
    }
}

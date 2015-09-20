package dlmj.callup.Common.Model;

import java.util.Calendar;

/**
 * Created by Two on 15/9/4.
 */
public class AlarmTime {
    private int mHour;
    private int mMinute;
    private Calendar mCalendar = Calendar.getInstance();

    public AlarmTime() {
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
    }

    public AlarmTime(String time) {
        String[] times = time.split(":");
        mHour = Integer.parseInt(times[0]);
        mMinute = Integer.parseInt(times[1]);
    }

    public AlarmTime(int hour, int minute){
        mHour = hour;
        mMinute = minute;
    }

    public String toString(){
        return mHour + ":" + mMinute + ":00";
    }

    public int getHour() {
        return mHour;
    }

    public int getMinute() {
        return mMinute;
    }
}

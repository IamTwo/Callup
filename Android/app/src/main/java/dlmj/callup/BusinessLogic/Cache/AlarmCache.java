package dlmj.callup.BusinessLogic.Cache;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import dlmj.callup.Common.Model.Alarm;

/**
 * Created by Two on 15/8/29.
 */
public class AlarmCache {
    private static AlarmCache mInstance;

    /**
     * The list of alarm items..
     */
    private List<Alarm> mAlarmList;

    public AlarmCache(){
        mAlarmList = new LinkedList<>();
    }

    public static final AlarmCache getInstance(){
        synchronized (AlarmCache.class){
            if(mInstance == null){
                mInstance = new AlarmCache();
            }
        }
        return mInstance;
    }

    public void setAlarmList(List<Alarm> alarmList){
        mAlarmList = alarmList;
    }

    public List<Alarm> getAlarmList(){
        return mAlarmList;
    }

    public void addAlarm(Alarm alarm) {
        mAlarmList.add(alarm);
    }

    public void removeAlarm(int alarmId) {
        for(Alarm alarm : mAlarmList) {
            if(alarm.getAlarmId() == alarmId) {
                mAlarmList.remove(alarm);
                break;
            }
        }
    }
}

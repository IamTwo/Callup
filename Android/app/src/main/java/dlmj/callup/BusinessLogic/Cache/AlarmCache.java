package dlmj.callup.BusinessLogic.Cache;

import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Util.LogUtil;

/**
 * Created by Two on 15/8/29.
 */
public class AlarmCache extends BaseCache<Alarm>{
    private static AlarmCache mInstance;
    private final static String TAG = "AlarmCache";

    public AlarmCache() {
        super();
    }

    public static AlarmCache getInstance(){
        synchronized (AlarmCache.class){
            if(mInstance == null){
                mInstance = new AlarmCache();
            }
        }
        return mInstance;
    }

    public void updateAlarm(int alarmId, String time) {
        for(Alarm alarm : mList) {
            if(alarm.getAlarmId() == alarmId) {
                LogUtil.d(TAG, "Remove alarm [" + alarmId + "] in alarm cache.");
                mList.remove(alarm);
                alarm.setTime(time);
                mList.add(alarm);
                break;
            }
        }
    }

    public Alarm getAlarm(int alarmId) {
        for(Alarm alarm : mList) {
            if(alarm.getAlarmId() == alarmId) {
                return alarm;
            }
        }

        return null;
    }
}

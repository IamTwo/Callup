package dlmj.callup.BusinessLogic.Cache;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.DataAccess.AlarmTableManager;
import dlmj.callup.Service.AlarmTableService;

/**
 * Created by Two on 15/8/29.
 */
public class AlarmCache extends BaseCache<Alarm>{
    private static AlarmCache mInstance;
    private final static String TAG = "AlarmCache";
    private Intent intent;
    private Context mContext;

    public AlarmCache(Context context) {
        super();
        intent = new Intent(context, AlarmTableService.class);
        mContext = context;
    }

    public static AlarmCache getInstance(Context context){
        synchronized (AlarmCache.class){
            if(mInstance == null){
                mInstance = new AlarmCache(context);
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

    public void removeAlarm(int alarmId) {
        for(Alarm alarm : mList) {
            if(alarm.getAlarmId() == alarmId) {
                LogUtil.d(TAG, "Remove alarm [" + alarmId + "] in alarm cache.");
                mList.remove(alarm);
                break;
            }
        }

        intent.setAction(AlarmTableService.DELETE);
        intent.putExtra(IntentExtraParams.ALARM_ID, alarmId);
        mContext.startService(intent);
    }

    public List<Alarm> getList() {
        if(super.getList().size() > 0) {
            return mList;
        }

        return AlarmTableManager.getInstance(mContext).getAlarms();
    }

    public void setList(List<Alarm> alarms) {
        super.setList(alarms);

        intent.setAction(AlarmTableService.SAVE);
        mContext.startService(intent);
    }

    public Alarm getAlarm(int alarmId) {
        for(Alarm alarm : mList) {
            if(alarm.getAlarmId() == alarmId) {
                return alarm;
            }
        }

        return AlarmTableManager.getInstance(mContext).getAlarm(alarmId);
    }

    public void clear() {
        mList.clear();
        intent.setAction(AlarmTableService.CLEAR);
        mContext.startService(intent);
    }
}

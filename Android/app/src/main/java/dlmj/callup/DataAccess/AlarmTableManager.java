package dlmj.callup.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import dlmj.callup.Common.Model.Alarm;

/**
 * Created by Two on 15/9/7.
 */
public class AlarmTableManager {
    private SQLiteDatabase mSQLiteDatabase;
    private static AlarmTableManager mInstance;

    public AlarmTableManager(Context context){
        mSQLiteDatabase = DatabaseManager.getInstance(context).getDatabase();
        mSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS alarm (" +
                "alarm_id INTEGER, " +
                "scene_id INTEGER, " +
                "logo_url VARCHAR, " +
                "image_url VARCHAR, " +
                "scene_name VARCHAR, " +
                "time VARCHAR, " +
                "frequent_type VARCHAR, " +
                "audio_url VARCHAR)");
    }

    public static AlarmTableManager getInstance(Context context){
        synchronized (AlarmTableManager.class){
            if(mInstance == null){
                mInstance = new AlarmTableManager(context);
            }
        }
        return mInstance;
    }

    public void saveAlarms(List<Alarm> alarms) {
        synchronized (AlarmTableManager.class) {
            mSQLiteDatabase.beginTransaction();
            for(Alarm alarm : alarms){
                ContentValues contentValues = new ContentValues();
                contentValues.put("alarm_id", alarm.getAlarmId());
                contentValues.put("scene_id", alarm.getSceneId());
                contentValues.put("logo_url", alarm.getLogoUrl());
                contentValues.put("image_url", alarm.getImageUrl());
                contentValues.put("scene_name", alarm.getSceneName());
                contentValues.put("time", alarm.getTime());
                contentValues.put("frequent_type", alarm.getFrequent());
                contentValues.put("audio_url", alarm.getAudioUrl());
                mSQLiteDatabase.insert("alarm", null, contentValues);
            }

            mSQLiteDatabase.setTransactionSuccessful();
            mSQLiteDatabase.endTransaction();
        }
    }

    public List<Alarm> getAlarms(){
        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * FROM alarm", null);
        List<Alarm> alarms = new LinkedList<>();
        while (cursor.moveToNext()) {
            alarms.add(new Alarm(cursor.getInt(cursor.getColumnIndex("alarm_id")),
                    cursor.getInt(cursor.getColumnIndex("scene_id")),
                    cursor.getString(cursor.getColumnIndex("logo_url")),
                    cursor.getString(cursor.getColumnIndex("image_url")),
                    cursor.getString(cursor.getColumnIndex("scene_name")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("frequent_type")),
                    cursor.getString(cursor.getColumnIndex("audio_url"))));
        }
        cursor.close();
        return alarms;
    }

    public Alarm getAlarm(int alarmId){
        Cursor cursor = mSQLiteDatabase.query("alarm", null, "alarm_id=?", new String[]{alarmId + ""}
            ,null, null, null);
        Alarm alarm = null;
        while (cursor.moveToNext()) {
            alarm = new Alarm(cursor.getInt(cursor.getColumnIndex("alarm_id")),
                    cursor.getInt(cursor.getColumnIndex("scene_id")),
                    cursor.getString(cursor.getColumnIndex("logo_url")),
                    cursor.getString(cursor.getColumnIndex("image_url")),
                    cursor.getString(cursor.getColumnIndex("scene_name")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("frequent_type")),
                    cursor.getString(cursor.getColumnIndex("audio_url")));
            break;
        }
        cursor.close();
        return alarm;
    }

    public void removeAlarm(int alarmId) {
        synchronized (AlarmTableManager.class) {
            mSQLiteDatabase.beginTransaction();
            mSQLiteDatabase.delete("alarm", "alarm_id=?", new String[]{alarmId + ""});
            mSQLiteDatabase.close();
            mSQLiteDatabase.setTransactionSuccessful();
            mSQLiteDatabase.endTransaction();
        }
    }

    public void clear() {
        synchronized (AlarmTableManager.class) {
            mSQLiteDatabase.beginTransaction();
            mSQLiteDatabase.execSQL("delete from alarm; " +
                    "update sqlite_sequence SET seq = 0 where name ='alarm'");
            mSQLiteDatabase.setTransactionSuccessful();
            mSQLiteDatabase.endTransaction();
        }
    }
}

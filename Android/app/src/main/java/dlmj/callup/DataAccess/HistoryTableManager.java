package dlmj.callup.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import dlmj.callup.BusinessLogic.Cache.SceneCache;
import dlmj.callup.Common.Model.Friend;
import dlmj.callup.Common.Model.History;
import dlmj.callup.Common.Model.Scene;

/**
 * Created by Two on 15/9/29.
 */
public class HistoryTableManager {
    private SQLiteDatabase mSQLiteDatabase;
    private static HistoryTableManager mInstance;
    private Context mContext;

    public HistoryTableManager(Context context) {
        mSQLiteDatabase = DatabaseManager.getInstance(context).getDatabase();
        mSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS history (" +
                "scene_id INTEGER, " +
                "time VARCHAR, " +
                "status INTEGER, " +
                "from_user_id INTEGER, " +
                "from_user_name VARCHAR," +
                "friend_user_id INTEGER)");
        mContext = context;
    }

    public static HistoryTableManager getInstance(Context context) {
        synchronized (AlarmTableManager.class) {
            if (mInstance == null) {
                mInstance = new HistoryTableManager(context);
            }
        }
        return mInstance;
    }

    public List<History> getHistories(Friend currentFriend) {
        Cursor cursor = mSQLiteDatabase.query("history", null, "friend_user_id=?",
                new String[]{ currentFriend.getUserId() + ""}
                , null, null, null);
        List<History> histories = new LinkedList<>();
        while (cursor.moveToNext()) {
            Scene scene = SceneCache.getInstance(mContext)
                    .getScene(cursor.getInt(cursor.getColumnIndex("scene_id")));
            histories.add(new History(scene,
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getInt(cursor.getColumnIndex("status")),
                    cursor.getInt(cursor.getColumnIndex("from_user_id")),
                    cursor.getString(cursor.getColumnIndex("from_user_name"))));
        }
        cursor.close();
        return histories;
    }

    public void clear() {
        synchronized (HistoryTableManager.class) {
            mSQLiteDatabase.beginTransaction();
            mSQLiteDatabase.execSQL("delete from history; " +
                    "update sqlite_sequence SET seq = 0 where name ='history'");
            mSQLiteDatabase.setTransactionSuccessful();
            mSQLiteDatabase.endTransaction();
        }
    }

    public void updateHistory(History history, Friend friend) {
        synchronized (HistoryTableManager.class) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("scene_id", history.getScene().getSceneId());
            contentValues.put("time", history.getTime());
            contentValues.put("status", history.getStatus());
            contentValues.put("from_user_id", history.getFromUserId());
            contentValues.put("from_user_name", history.getFromUserName());
            contentValues.put("friend_user_id", friend.getUserId());
            mSQLiteDatabase.update("history", contentValues,
                    "friend_user_id=? and time=?",
                    new String[]{history.getFromUserId() + "", history.getTime()});
        }
    }
}

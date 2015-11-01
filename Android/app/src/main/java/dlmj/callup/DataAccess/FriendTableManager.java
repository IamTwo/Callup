package dlmj.callup.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import dlmj.callup.Common.Model.Friend;

/**
 * Created by Two on 15/9/29.
 */
public class FriendTableManager {
    private SQLiteDatabase mSQLiteDatabase;
    private static FriendTableManager mInstance;

    public FriendTableManager(Context context){
        mSQLiteDatabase = DatabaseManager.getInstance(context).getDatabase();
        mSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS friend (" +
                "user_id INTEGER, " +
                "face_url VARCHAR, " +
                "name VARCHAR, " +
                "account VARCHAR)");
    }

    public static FriendTableManager getInstance(Context context){
        synchronized (FriendTableManager.class){
            if(mInstance == null){
                mInstance = new FriendTableManager(context);
            }
        }
        return mInstance;
    }

    public void saveFriends(List<Friend> friends) {
        synchronized (SceneTableManager.class) {
            mSQLiteDatabase.beginTransaction();
            for(Friend friend : friends){
                ContentValues contentValues = new ContentValues();
                contentValues.put("user_id", friend.getUserId());
                contentValues.put("face_url", friend.getFaceUrl());
                contentValues.put("name", friend.getName());
                contentValues.put("account", friend.getAccount());
                mSQLiteDatabase.insert("friend", null, contentValues);
            }

            mSQLiteDatabase.setTransactionSuccessful();
            mSQLiteDatabase.endTransaction();
        }
    }

    public List<Friend> getFriends(){
        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * FROM friend", null);
        List<Friend> friends = new LinkedList<>();
        while (cursor.moveToNext()) {
            friends.add(new Friend(cursor.getInt(cursor.getColumnIndex("user_id")),
                    cursor.getString(cursor.getColumnIndex("face_url")),
                    cursor.getString(cursor.getColumnIndex("account")),
                    cursor.getString(cursor.getColumnIndex("name"))));
        }
        cursor.close();
        return friends;
    }

    public Friend getFriend(int userId){
        Cursor cursor = mSQLiteDatabase.query("friend", null, "user_id=?", new String[]{userId + ""}
                ,null, null, null);
        Friend friend = null;
        while (cursor.moveToNext()) {
            friend = new Friend(cursor.getInt(cursor.getColumnIndex("user_id")),
                    cursor.getString(cursor.getColumnIndex("face_url")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("account")));
            break;
        }
        cursor.close();
        return friend;
    }

    public void clear() {
        mSQLiteDatabase.beginTransaction();
        mSQLiteDatabase.execSQL("delete from friend; " +
                "update sqlite_sequence SET seq = 0 where name ='friend'");
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }
}

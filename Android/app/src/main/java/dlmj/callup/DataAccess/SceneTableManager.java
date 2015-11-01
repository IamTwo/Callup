package dlmj.callup.DataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import dlmj.callup.Common.Model.Scene;

/**
 * Created by Two on 15/9/29.
 */
public class SceneTableManager {
    private SQLiteDatabase mSQLiteDatabase;
    private static SceneTableManager mInstance;
    private Context mContext;

    public SceneTableManager(Context context){
        mSQLiteDatabase = DatabaseManager.getInstance(context).getDatabase();
        mSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS scene (" +
                "scene_id INTEGER, " +
                "name VARCHAR, " +
                "logo_url VARCHAR, " +
                "image_url VARCHAR, " +
                "audio_url VARCHAR)");
        mContext = context;
    }

    public static SceneTableManager getInstance(Context context){
        synchronized (SceneTableManager.class){
            if(mInstance == null){
                mInstance = new SceneTableManager(context);
            }
        }
        return mInstance;
    }

    public void saveScenes(List<Scene> scenes) {
        synchronized (SceneTableManager.class) {
            mSQLiteDatabase.beginTransaction();
            for(Scene scene : scenes){
                ContentValues contentValues = new ContentValues();
                contentValues.put("scene_id", scene.getSceneId());
                contentValues.put("name", scene.getName(mContext));
                contentValues.put("logo_url", scene.getLogoUrl());
                contentValues.put("image_url", scene.getImageUrl());
                contentValues.put("audio_url", scene.getAudioUrl());
                mSQLiteDatabase.insert("scene", null, contentValues);
            }

            mSQLiteDatabase.setTransactionSuccessful();
            mSQLiteDatabase.endTransaction();
        }
    }

    public List<Scene> getScenes(){
        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * FROM scene", null);
        List<Scene> scenes = new LinkedList<>();
        while (cursor.moveToNext()) {
            scenes.add(new Scene(cursor.getInt(cursor.getColumnIndex("scene_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("logo_url")),
                    cursor.getString(cursor.getColumnIndex("image_url")),
                    cursor.getString(cursor.getColumnIndex("audio_url"))));
        }
        cursor.close();
        return scenes;
    }

    public Scene getScene(int sceneId){
        Cursor cursor = mSQLiteDatabase.query("scene", null, "scene_id=?", new String[]{sceneId + ""}
                ,null, null, null);
        Scene scene = null;
        while (cursor.moveToNext()) {
            cursor.getInt(cursor.getColumnIndex("scene_id"));
            scene = new Scene(cursor.getInt(cursor.getColumnIndex("scene_id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("logo_url")),
                    cursor.getString(cursor.getColumnIndex("image_url")),
                    cursor.getString(cursor.getColumnIndex("audio_url")));
            break;
        }
        cursor.close();
        return scene;
    }

    public void clear() {
        mSQLiteDatabase.beginTransaction();
        mSQLiteDatabase.execSQL("delete from scene; " +
                "update sqlite_sequence SET seq = 0 where name ='scene'");
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }
}

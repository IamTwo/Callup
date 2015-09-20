package dlmj.callup.DataAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.List;

import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Params.DatabaseParams;

/**
 * Created by Two on 15/9/7.
 */
public class DatabaseManager {
    private SQLiteDatabase mDatabase;

    private static DatabaseManager mInstance;

    private DatabaseManager(Context context) {
        mDatabase = context.openOrCreateDatabase(DatabaseParams.ALARM_DATABASE,
                Context.MODE_PRIVATE, null);
    }

    public static DatabaseManager getInstance(Context context) {
        synchronized (DatabaseManager.class){
            if(mInstance == null){
                mInstance = new DatabaseManager(context);
            }
        }
        return mInstance;
    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }
}

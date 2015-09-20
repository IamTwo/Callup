package dlmj.callup.DataAccess;

import android.database.Cursor;
import android.text.TextUtils;

import dlmj.callup.Common.Model.Contact;

/**
 * Created by Two on 15/9/8.
 */
public class ContactTableManager {
    private static ContactTableManager mInstance;
    private static ContactTableManager getInstance() {
        if(mInstance == null) {
            mInstance = new ContactTableManager();
        }
        return mInstance;
    }

    public static Contact getContact(String contactAccount) {
        if(TextUtils.isEmpty(contactAccount)) {
            return null;
        }
        Contact contact = new Contact(contactAccount);

//        try{
//            Cursor cursor =
//        }
        return contact;
    }
}

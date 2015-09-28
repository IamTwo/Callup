package dlmj.callup.Common.Factory;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import dlmj.callup.R;

/**
 * Created by Two on 15/9/26.
 */
public class DialogFactory {
    public static Dialog getProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(context.getString(R.string.loading));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        return dialog;
    }
}

package dlmj.callup.BroadcastReceiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Two on 15/9/23.
 */
public class DownloadReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("tag", "" + intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));

        queryDownloadStatus();
    }

    private void queryDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
//        query.setFilterById()
    }
}

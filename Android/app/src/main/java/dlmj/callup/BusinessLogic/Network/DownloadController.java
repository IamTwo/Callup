package dlmj.callup.BusinessLogic.Network;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

import dlmj.callup.Common.Util.BaseInfoUtil;
import dlmj.callup.Common.Model.Scene;

/**
 * Created by Two on 15/8/29.
 */
public class DownloadController {
    private static String DOWNLOAD_SERVICE = Context.DOWNLOAD_SERVICE;
    public DownloadManager mDownloadManager;
    private DownloadController mInstance;
    private Context mContext;

    public DownloadController(Context context){
        mDownloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        mContext = context;
    }

    public DownloadController getInstance(Context context){
        if(mInstance == null){
            synchronized (VolleyQueueController.class){
                if(mInstance == null){
                    mInstance = new DownloadController(context);
                }
            }
        }
        return mInstance;
    }

    public long startDownload(Scene scene){
        String destination = BaseInfoUtil.getDownloadMusicPath(mContext);
        String fileName = scene.getName() + ".mp3";
        if(!FileExists(destination + fileName)) {
            Uri uri = Uri.parse(scene.getAudioUrl());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setDestinationInExternalPublicDir(destination, fileName);
            long reference = mDownloadManager.enqueue(request);
            return reference;
        }
        return 0;
    }

    public boolean FileExists(String path){
        File file = new File(Environment.getExternalStorageDirectory() + path);
        return file.exists();
    }
}

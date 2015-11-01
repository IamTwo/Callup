package dlmj.callup.BusinessLogic.Network;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import dlmj.callup.BusinessLogic.Cache.ImageCacheManager;
import dlmj.callup.Common.Interfaces.OnDownloadChangedListener;
import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Util.BaseInfoUtil;
import dlmj.callup.Common.Util.LogUtil;

/**
 * Created by Two on 15/9/23.
 */
public class AlarmLoadHelper {
    public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    private final static String TAG = "AlarmLoadHelper";
    private Alarm mAlarm;
    private Context mContext;
    private int mMaxContent = 0;
    private int mDownloadedContent = 0;
    public DownloadManager mDownloadManager;
    private DownloadManager.Query mQuery = new DownloadManager.Query();
    private static AlarmLoadHelper mInstance;
    private OnDownloadChangedListener mOnDownloadChangedListener;
    private long mDownloadId;

    private AlarmLoadHelper(Context context) {
        mContext = context;
        mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public static AlarmLoadHelper getInstance(Context context) {
        synchronized (AlarmLoadHelper.class) {
            if (mInstance == null) {
                mInstance = new AlarmLoadHelper(context);
            }
        }
        return mInstance;
    }

    public void loadAlarmInfo(Alarm alarm) {
        mAlarm = alarm;
        loadImage();
        loadAudio();
        mMaxContent = 0;
        mDownloadedContent = -1;
    }

    private void loadImage() {
        mMaxContent++;
        ImageRequest imageRequest = new ImageRequest(mAlarm.getImageUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        ImageCacheManager.getInstance(mContext).putBitmap(mAlarm.getImageUrl(), response);
                        mDownloadedContent++;

                    }
                }, 300, 200, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        VolleyQueueController.getInstance(mContext).getRequestQueue().add(imageRequest);
    }

    public void loadAudio() {
        String destination = BaseInfoUtil.getDownloadMusicPath(mContext);
        String fileName = mAlarm.getSceneName() + ".mp3";
        String path = Environment.getExternalStorageDirectory() + destination + fileName;
        if (!BaseInfoUtil.ifFileExists(path)) {
            Uri uri = Uri.parse(mAlarm.getAudioUrl());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setDestinationInExternalPublicDir(destination, fileName);
            mDownloadId = mDownloadManager.enqueue(request);

            DownloadChangeObserver downloadObserver = new DownloadChangeObserver(null);
            mContext.getContentResolver().registerContentObserver(CONTENT_URI, true, downloadObserver);
        } else {
            LogUtil.e(TAG, "Don't need to download the audio.");
            mOnDownloadChangedListener.onFinished();
        }
    }

    public void updateProgress() {
        Cursor cursor = null;
        try {
            mQuery.setFilterById(mDownloadId);
            cursor = mDownloadManager.query(mQuery);
            if (cursor != null && cursor.moveToFirst()) {
                mMaxContent = cursor.getInt(cursor.getColumnIndexOrThrow
                        (DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                mDownloadedContent = cursor.getInt(cursor.getColumnIndexOrThrow
                        (DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

                checkStatus(status);
                mOnDownloadChangedListener.onChanged();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void checkStatus(int status) {
        switch (status) {
            case DownloadManager.STATUS_RUNNING:
                mOnDownloadChangedListener.onChanged();
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                mOnDownloadChangedListener.onFinished();
                break;
            case DownloadManager.STATUS_FAILED:
                mDownloadManager.remove(mDownloadId);
                break;
            default:
                break;
        }
    }

    public void setOnChangeListener(OnDownloadChangedListener onChangeListener) {
        mOnDownloadChangedListener = onChangeListener;
    }

    public int getMaxContent() {
        return mMaxContent;
    }

    public int getDownloadedContent() {
        return mDownloadedContent;
    }

    public Alarm getAlarm() {
        return mAlarm;
    }

    class DownloadChangeObserver extends ContentObserver {
        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateProgress();
            LogUtil.d(TAG, "Download file is updated.");
        }
    }

    public void cancelDownloadTask() {
        mDownloadManager.remove(mDownloadId);
    }
}

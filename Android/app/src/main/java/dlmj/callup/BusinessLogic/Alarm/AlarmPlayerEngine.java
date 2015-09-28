package dlmj.callup.BusinessLogic.Alarm;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;

import java.io.IOException;

import dlmj.callup.Common.Interfaces.PlayerEngine;
import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Util.BaseInfoUtil;
import dlmj.callup.Common.Util.LogUtil;

/**
 * Created by Two on 15/8/28.
 */
public class AlarmPlayerEngine implements PlayerEngine {
    private static final String TAG = "AlarmPlayerEngine";
    private MediaPlayer mCurrentMediaPlayer;
    private String mPath;
    private Context mContext;

    public AlarmPlayerEngine(Context context) {
        mContext = context;
        mCurrentMediaPlayer = new MediaPlayer();
        mCurrentMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.start();
            }
        });
    }

    public void updateAlarm(Alarm alarm) {
        mPath = Environment.getExternalStorageDirectory() + BaseInfoUtil.getDownloadMusicPath(mContext)
                + alarm.getSceneName() + ".mp3";
        LogUtil.d(TAG, "The path is " + mPath);

        if (!BaseInfoUtil.ifFileExists(mPath)) {
            mPath = alarm.getAudioUrl();
        }
    }

    @Override
    public void play() {
        try {
            mCurrentMediaPlayer.reset();
            mCurrentMediaPlayer.setDataSource(mPath);
            mCurrentMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        mCurrentMediaPlayer.stop();
    }

    @Override
    public void pause() {
        mCurrentMediaPlayer.pause();
    }
}

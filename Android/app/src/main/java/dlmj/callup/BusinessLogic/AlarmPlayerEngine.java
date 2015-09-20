package dlmj.callup.BusinessLogic;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import dlmj.callup.Common.Interfaces.PlayerEngine;
import dlmj.callup.Common.Model.Scene;

/**
 * Created by Two on 15/8/28.
 */
public class AlarmPlayerEngine implements PlayerEngine {
    private MediaPlayer mCurrentMediaPlayer;
    private Context mContext;
    private String mPath;

    public AlarmPlayerEngine(Context context){
        mContext = context;
        mCurrentMediaPlayer = new MediaPlayer();
        mCurrentMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mCurrentMediaPlayer.setLooping(true);
    }

    public void updateScene(Scene scene){
        mPath = Environment.getExternalStorageDirectory() + "/" + scene.getName() + ".mp3";
        File file = new File(mPath);

        if(!file.exists()){
            mPath = scene.getAudioUrl();
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
        } catch (IllegalStateException e){
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

package dlmj.callup.UI.Activity.SelfSave;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import dlmj.callup.BusinessLogic.Cache.ImageCacheManager;
import dlmj.callup.Common.Params.IntentExtraParams;
import dlmj.callup.Common.Model.Scene;
import dlmj.callup.R;
import dlmj.callup.Service.AlarmService;

/**
 * Created by Two on 15/8/27.
 */
public class AlarmActivity extends Activity implements SensorEventListener {
    private static int SWING_TIME = 5;
    private TextView mSwingTimeTextView;
    private NetworkImageView mNetworkImageView;
    SensorManager mSensorManager = null;
    Vibrator mVibrator = null;
    private int mSwingTime = 0;
    private Intent mServiceIntent;
    private Scene mScene;
    private ImageLoader mImageLoader;
    private TextView mSceneNameTextView;

    @Override
    public void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.alarm_ring);
        initializeData();
        findView();
        startAction(AlarmService.ACTION_PLAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void initializeData() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        mScene = (Scene)getIntent().getSerializableExtra(IntentExtraParams.SCENE);
        mServiceIntent = new Intent(this, AlarmService.class);
        mServiceIntent.putExtra(IntentExtraParams.SCENE, mScene);
        mImageLoader = ImageCacheManager.getInstance(this).getImageLoader();
    }

    private void findView(){
        mSwingTimeTextView = (TextView)findViewById(R.id.swingTimeTextView);
        mNetworkImageView = (NetworkImageView)findViewById(R.id.sceneImageView);
        mNetworkImageView.setImageUrl(mScene.getImageUrl(), mImageLoader);
        mSceneNameTextView = (TextView)findViewById(R.id.sceneNameTextView);
        mSceneNameTextView.setText(mScene.getName());
    }

    private void startAction(String action) {
        mServiceIntent.setAction(action);

        getApplicationContext().startService(mServiceIntent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER)
        {
            if ((Math.abs(values[0]) > 17 || Math.abs(values[1]) > 17 || Math
                    .abs(values[2]) > 17))
            {
                Log.d("sensor x ", "============ values[0] = " + values[0]);
                Log.d("sensor y ", "============ values[1] = " + values[1]);
                Log.d("sensor z ", "============ values[2] = " + values[2]);
                mSwingTime++;
                mVibrator.vibrate(500);
                mSwingTimeTextView.setText(mSwingTime + "");
            }

        }

        if(mSwingTime > SWING_TIME){
            startAction(AlarmService.ACTION_STOP);
            mSensorManager.unregisterListener(this);
            getApplicationContext().stopService(mServiceIntent);
            finish();

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

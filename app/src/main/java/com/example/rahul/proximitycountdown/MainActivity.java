package com.example.rahul.proximitycountdown;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    TextView CountDown,CountCheck,Count_near_far;
    private static SensorManager mSensorManager;
    private static Sensor mSensor;
    CountDownTimer timer;

    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CountDown=(TextView)findViewById(R.id.countDown);
        CountCheck=(TextView)findViewById(R.id.countCheck);
        Count_near_far = (TextView)findViewById(R.id.near_far);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mMediaPlayer = MediaPlayer.create(this, R.raw.song);
        mMediaPlayer.setLooping(true);

        TenSeconds();

    }

    public void TenSeconds()
    {
        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                CountDown.setText(String.valueOf((1000+millisUntilFinished)/1000));
            }

            @Override
            public void onFinish() {
                mMediaPlayer.start();
                CountCheck.setTextColor(Color.parseColor("#FF0000"));
                CountCheck.setTextSize(30);
                CountCheck.setText("Count Down Over");
                CountDown.setText(String.valueOf(10));
            }
        };

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            if(event.values[0] == 0) {
                Count_near_far.setText("You are NEAR");
                CountCheck.setText("Count Down ON");
                timer.start();
            }
            else {
                Count_near_far.setText("You Are FAR");
                if(mMediaPlayer.isPlaying())
                    mMediaPlayer.pause();
                timer.cancel();
                CountCheck.setText("Count Down OFF");
                CountCheck.setTextColor(Color.parseColor("#000000"));
                CountCheck.setTextSize(24);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }
}

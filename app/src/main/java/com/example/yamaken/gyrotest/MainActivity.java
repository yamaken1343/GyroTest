package com.example.yamaken.gyrotest;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager manager;
    Sensor acceleSensor;
    Sensor gyroSensor;
    TextView xText;
    TextView yText;
    TextView zText;
    TextView gyroText;
    Button startButton;
    Button stopButton;
    private float[] accValue = new float[3];
    private float[] gyroValue = new float[3];
    VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xText = findViewById(R.id.gyroX);
        yText = findViewById(R.id.gyroY);
        zText = findViewById(R.id.gyroZ);
        gyroText = findViewById(R.id.gyro);

        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        acceleSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        startButton = findViewById(R.id.statButton);
        stopButton = findViewById(R.id.stopButton);

        videoView = findViewById(R.id.video);
//        参照: http://etc.dounokouno.com/testmovie/link/mpeg4.html
        videoView.setVideoURI(Uri.parse("http://etc.dounokouno.com/testmovie/mpeg4/testmovie-960x540.mp4"));
        videoView.setMediaController(new MediaController(this));


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
                onResume();
                Log.v("SampleVideoPlayer", "Now playing.");
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
                onPause();
                Log.v("SampleVideoPlayer", "Now step.");
            }
        });

    }




    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accValue = event.values.clone();
        } else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            gyroValue = event.values.clone();
        }
        xText.setText("Acc X:" + accValue[0]);
        yText.setText("Acc Y:" + accValue[1]);
        zText.setText("Acc Z:" + accValue[2]);
        gyroText.setText("Gyro X:" + gyroValue[0] + "\n" +
                "Gyro Y:" + gyroValue[1] + "\n" +
                "Gyro Z:" + gyroValue[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.registerListener(this, acceleSensor, SensorManager.SENSOR_DELAY_UI);
        manager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }
}

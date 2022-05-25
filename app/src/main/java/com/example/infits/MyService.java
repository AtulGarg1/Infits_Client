package com.example.infits;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class MyService extends Service implements SensorEventListener {

    SensorManager sensorManager;
    Sensor stepSensor;

    Intent intent = new Intent("com.example.infits");

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor == null) {
            Log.e("Ser","No sensor");
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }

        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, DashBoardMain.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("step","in");
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            FetchTrackerInfos.totalSteps = (int) event.values[0];
            FetchTrackerInfos.currentSteps = ((int) FetchTrackerInfos.totalSteps - (int)FetchTrackerInfos.previousStep);

            Log.d("step", String.valueOf(FetchTrackerInfos.totalSteps));
            Log.d("stepPre", String.valueOf(FetchTrackerInfos.previousStep));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel chan = new NotificationChannel(
                        "MyChannelId",
                        "My Foreground Service",
                        NotificationManager.IMPORTANCE_LOW);
                chan.setLightColor(Color.BLUE);
                chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                assert manager != null;
                manager.createNotificationChannel(chan);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                        this, "MyChannelId");
                Notification notification = notificationBuilder.setOngoing(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(String.valueOf(FetchTrackerInfos.currentSteps))
                        .setPriority(NotificationManager.IMPORTANCE_LOW)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .setChannelId("MyChannelId")
                        .build();
                startForeground(1, notification);
                intent.putExtra("steps", FetchTrackerInfos.currentSteps);
                sendBroadcast(intent);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


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
            Log.e("Ser", "No sensor");
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
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            SharedPreferences sh = getSharedPreferences("DateForSteps", Context.MODE_PRIVATE);

            Date dateObj = new Date();

            String date = sh.getString("date","");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy");

            System.out.println(date);

            System.out.println(simpleDateFormat.format(dateObj));

            if (!date.equals(simpleDateFormat.format(dateObj))) {
                FetchTrackerInfos.previousStep = FetchTrackerInfos.totalSteps;
                System.out.println("Reset");
                SharedPreferences sharedPreferences = getSharedPreferences("DateForSteps", Context.MODE_PRIVATE);
                Date dateForSteps = new Date();

                System.out.println(simpleDateFormat.format(dateForSteps));

                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                myEdit.putString("date", simpleDateFormat.format(dateForSteps));
                myEdit.putBoolean("verified",false);
                myEdit.commit();
            }

            Calendar calendar = new GregorianCalendar();
            String am_pm;
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
//            if (calendar.get(Calendar.AM_PM) == 0) {
//                if (hour >= 0){
//                    FetchTrackerInfos.previousStep = FetchTrackerInfos.totalSteps;
//                    System.out.println("Reset");
//                }
//            }
            FetchTrackerInfos.totalSteps = (int) event.values[0];
            FetchTrackerInfos.currentSteps = ((int) FetchTrackerInfos.totalSteps - (int) FetchTrackerInfos.previousStep);

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
                String time = "Current Time : " + hour + ":" + minute + " " ;
                System.out.println(time);
//                updateSteps();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    void updateSteps() {
//        String url="http://192.168.227.91/infits/steptracker.php";;
//        StringRequest request = new StringRequest(Request.Method.POST,url, response -> {
//            if (response.equals("updated")){
//                Toast.makeText(getApplicationContext(), "Updated bro", Toast.LENGTH_SHORT).show();
//                Log.d("Response",response);
//            }
//            else{
//                Toast.makeText(getApplicationContext(), "Not working", Toast.LENGTH_SHORT).show();
//                Log.d("Response",response);
//            }
//        },error -> {
//            Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
//        }){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Date date = new Date();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                sdf.format(date);
//                Map<String,String> data = new HashMap<>();
//                data.put("userID","Azarudeen");
//                data.put("dateandtime", String.valueOf(date));
//                data.put("distance", "14");
//                data.put("avgspeed", "14");
//                data.put("calories","34");
//                data.put("steps", String.valueOf(FetchTrackerInfos.currentSteps));
//                data.put("goal", "5000");
//                return data;
//            }
//        };
//        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

}
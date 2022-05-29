package com.example.infits;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.PowerManager;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SleepTrackerFragment extends Fragment {

    String sleep;

    Button setalarm, startcycle, endcycle;
    ImageButton imgback;
    TextView texttime, tvDuration;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String timerTime;

    private int seconds;
    private boolean running;
    private boolean wasRunning;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SleepTrackerFragment() {

    }

    public static SleepTrackerFragment newInstance(String param1, String param2) {
        SleepTrackerFragment fragment = new SleepTrackerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sleep_tracker, container, false);

        PowerManager powerManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            powerManager = (PowerManager) getActivity().getSystemService(getActivity().POWER_SERVICE);
        }
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();

        imgback = view.findViewById(R.id.imgback);
        setalarm = view.findViewById(R.id.setalarm);
        startcycle = view.findViewById(R.id.startcycle);
        endcycle = view.findViewById(R.id.endcycle);
        texttime = view.findViewById(R.id.texttime);
        tvDuration = view.findViewById(R.id.tvDuration);

        if (!foregroundServiceRunning()){
            endcycle.setVisibility(View.GONE);
            startcycle.setVisibility(View.VISIBLE);
        }

        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("com.example.infits.sleep"));

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(calendar.getTime());

        if(savedInstanceState != null) {
            savedInstanceState.getInt("seconds");
            savedInstanceState.getBoolean("running");
            savedInstanceState.getBoolean("wasRunning");
        }

//        runTimer();
        

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_sleepTrackerFragment_to_dashBoardFragment);
            }
        });

        setalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mClockIntent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                mClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mClockIntent);
            }
        });


        startcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startcycle.setVisibility(View.GONE);
                endcycle.setVisibility(View.VISIBLE);
                if (!foregroundServiceRunning()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Intent intent = new Intent(getActivity(), StopWatchService.class);
                        getActivity().startForegroundService(new Intent(getActivity(), StopWatchService.class));
                    }
                    sleep = "";
                    getActivity().registerReceiver(broadcastReceiver, new IntentFilter("com.example.infits.sleep"));
                }
            }
        });

        endcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = sleep;
                endcycle.setVisibility(View.GONE);
                startcycle.setVisibility(View.VISIBLE);
                Intent i = new Intent(getActivity(),StopWatchService.class);
                i.putExtra("status",true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getActivity().getApplicationContext().stopService(new Intent(getActivity(),StopWatchService.class));
                }
                getActivity().unregisterReceiver(broadcastReceiver);
                tvDuration.setText("You slept for " +sleep);
                String url="http://192.168.124.91/infits/sleeptracker.php";
                StringRequest request = new StringRequest(Request.Method.POST,url, response -> {
                    if (response.equals("updated")){
                        Toast.makeText(getActivity(), "Good Morning", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        System.out.println(response);
                        Toast.makeText(getActivity(), "Not working", Toast.LENGTH_SHORT).show();
                    }
                },error -> {
                    Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                })
                {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Calendar cal = Calendar.getInstance();
                        String pat = "dd-MM-yyyy hh:mm:ss";

                        SimpleDateFormat sdf = new SimpleDateFormat(pat);

                        cal.set(Calendar.DAY_OF_WEEK, 7);
                        Map<String,String> data = new HashMap<>();
                        data.put("userID","Azarudeen");
                        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                        data.put("sleeptime", sdf.format(cal.getTime()));
                        System.out.println(sdf.format(cal.getTime()));
                        cal.set(Calendar.DAY_OF_WEEK, 7);
                        data.put("waketime", sdf.format(cal.getTime()));
                        System.out.println(sdf.format(cal.getTime()));
                        data.put("timeslept",time);
                        System.out.println("hi"+time);
                        data.put("goal", "8");
                        return data;
                    }
                };
                Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
                sleep = "";
            }
        });
        return view;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent);
        }
    };

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            sleep = intent.getStringExtra("sleep");
            Log.i("StepTracker","Countdown seconds remaining:" + sleep);
            texttime.setText(sleep);
        }
    }

//    private void runTimer() {

//        final Handler handler = new Handler();
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//
//                int hours = seconds / 3600;
//                int minutes = (seconds % 3600) / 60;
//                int secs = seconds % 60;
//
//                timerTime = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
//
//                texttime.setText(timerTime);
//
//                if(running) {
//                    seconds++;
//                }
//                handler.postDelayed(this, 1000);
//            }
//        });
//    }

    public boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)){
            if (MyService.class.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

}
package com.example.infits;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Locale;

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

        imgback = view.findViewById(R.id.imgback);
        setalarm = view.findViewById(R.id.setalarm);
        startcycle = view.findViewById(R.id.startcycle);
        endcycle = view.findViewById(R.id.endcycle);
        texttime = view.findViewById(R.id.texttime);
        tvDuration = view.findViewById(R.id.tvDuration);

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent intent = new Intent(getActivity(),StopWatchService.class);
                    getActivity().startForegroundService(new Intent(getActivity(),StopWatchService.class));
                }
                sleep = "";
                getActivity().registerReceiver(broadcastReceiver,new IntentFilter("com.example.infits.sleep"));
            }
        });

        endcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endcycle.setVisibility(View.GONE);
                startcycle.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getActivity().getApplicationContext().stopService(new Intent(getActivity(),StopWatchService.class));
                }
                getActivity().unregisterReceiver(broadcastReceiver);
                tvDuration.setText("You slept for " +sleep);
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
}
package com.example.infits;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SleepTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SleepTrackerFragment extends Fragment {

    Button setalarm, startcycle, endcycle;
    ImageButton imgback;
    TextView texttime, tvDuration;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String timerTime;

    private int seconds;
    private boolean running;
    private boolean wasRunning;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SleepTrackerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SleepTrackerFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
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

        //texttime.setText(""+time);

        if(savedInstanceState != null) {
            savedInstanceState.getInt("seconds");
            savedInstanceState.getBoolean("running");
            savedInstanceState.getBoolean("wasRunning");
        }

        runTimer();
        

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
                startcycle.setVisibility(View.INVISIBLE);
                endcycle.setVisibility(View.VISIBLE);

                running = true;

            }

        });


        endcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endcycle.setVisibility(View.INVISIBLE);
                startcycle.setVisibility(View.VISIBLE);

                running = false;

                tvDuration.setText("You slept for " +timerTime);

                seconds = 0;

            }
        });

        return view;

    }


    @Override
    public void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(wasRunning) {
            running = true;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("seconds",seconds);
        outState.putBoolean("running", running);
        outState.putBoolean("wasRunning", wasRunning);
    }

    private void runTimer() {

        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {

                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                timerTime = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);

                texttime.setText(timerTime);

                if(running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);

            }
        });
    }

}
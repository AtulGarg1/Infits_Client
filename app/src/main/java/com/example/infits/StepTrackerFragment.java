package com.example.infits;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class StepTrackerFragment extends Fragment {

    Button setgoal;
    ImageButton imgback;
    TextView steps_label;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public StepTrackerFragment() {

    }

    public static StepTrackerFragment newInstance(String param1, String param2) {
        StepTrackerFragment fragment = new StepTrackerFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step_tracker, container, false);

        steps_label = view.findViewById(R.id.steps_label);
        setgoal = view.findViewById(R.id.setgoal);
        imgback = view.findViewById(R.id.imgback);

        PowerManager powerManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            powerManager = (PowerManager) getActivity().getSystemService(getActivity().POWER_SERVICE);
        }
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();

        setgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.setgoaldialog);
                Intent serviceIntent = new Intent(getActivity(), MyService.class);;
                if (!foregroundServiceRunning()){
                    ContextCompat.startForegroundService(getActivity(), serviceIntent);
                }
                final EditText goal = view.findViewById(R.id.goal);
                Button save = dialog.findViewById(R.id.save_btn_steps);
                save.setOnClickListener(v->{
                    FetchTrackerInfos.previousStep = FetchTrackerInfos.totalSteps;
                    dialog.dismiss();
                });
                dialog.show();
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_stepTrackerFragment_to_dashBoardFragment);
            }
        });
//        final Handler handler = new Handler();
//        final int delay = 1000; // 1000 milliseconds == 1 second
//
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                System.out.println(FetchTrackerInfos.stepsWalked);
//                if (!FetchTrackerInfos.stepsWalked.isEmpty()){
//                        steps_label.setText(FetchTrackerInfos.currentSteps);
//                }
//                handler.postDelayed(this, delay);
//            }
//        }, delay);
        return view;
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent);
        }
    };
    public boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)){
         if (MyService.class.getName().equals(service.service.getClassName())){
             return true;
         }
     }
        return false;
    }
    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            int steps = intent.getIntExtra("steps",0);
            Log.i("StepTracker","Countdown seconds remaining:" + steps);
            steps_label.setText(String.valueOf(steps));
//            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
//            sharedPreferences.edit().putInt("steps",steps).apply();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("com.example.infits"));
        Log.i("Steps","Registered broadcast receiver");
    }
}
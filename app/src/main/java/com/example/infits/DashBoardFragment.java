package com.example.infits;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends Fragment {

    ImageButton sidemenu, notifmenu;
    CardView stepcard, heartcard, watercard, sleepcard, weightcard, caloriecard;
    Button btnsub, btnsub1;
    TextView name,date;
    ImageView profile;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);

        name = view.findViewById(R.id.nameInDash);
        date = view.findViewById(R.id.date);
        profile = view.findViewById(R.id.profile);

        profile.setImageBitmap(DataFromDatabase.profile);

        Date dateToday = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("MMM dd,yyyy");

        stepcard = view.findViewById(R.id.stepcard);
        heartcard = view.findViewById(R.id.heartcard);
        watercard = view.findViewById(R.id.watercard);
        sleepcard = view.findViewById(R.id.sleepcard);
        weightcard = view.findViewById(R.id.weightcard);
        caloriecard = view.findViewById(R.id.caloriecard);

        name.setText(DataFromDatabase.name);
        date.setText(sf.format(dateToday));
        Log.d("Name",DataFromDatabase.name+"");
        Log.d("Name",DataFromDatabase.age+"");
        Log.d("Name",DataFromDatabase.email+"");
        btnsub = view.findViewById(R.id.btnsub);
        btnsub1 = view.findViewById(R.id.btnsub1);

        stepcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_stepTrackerFragment);
            }
        });

        sleepcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_sleepTrackerFragment);
            }
        });

        watercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_waterTrackerFragment);
            }
        });

        weightcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_weightTrackerFragment);
            }
        });

        caloriecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_calorieTrackerFragment);
            }
        });

        /*

        btnsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent isub = new Intent(DashBoard1.this, Subscription1.class);
                startActivity(isub);
            }
        });

        btnsub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent isub = new Intent(DashBoard1.this, Statistics.class);
                startActivity(isub);
            }
        });

         */

        return view;
    }
}
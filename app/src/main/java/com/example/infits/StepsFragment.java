package com.example.infits;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepsFragment extends Fragment {

    CardView stepfrag, heartfrag, waterfrag, sleepfrag, weightfrag;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StepsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StepsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StepsFragment newInstance(String param1, String param2) {
        StepsFragment fragment = new StepsFragment();
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
        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        stepfrag = view.findViewById(R.id.stepfrag);
        heartfrag = view.findViewById(R.id.heartfrag);
        waterfrag = view.findViewById(R.id.waterfrag);
        sleepfrag = view.findViewById(R.id.sleepfrag);
        weightfrag = view.findViewById(R.id.weightfrag);


        final GraphView graph = (GraphView) view.findViewById(R.id.graph);

        ArrayList<String> days = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd ");
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            cal.add(Calendar.DAY_OF_YEAR, 0);
            for(int i = 0; i< 6; i++){
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            days.add(sdf.format(cal.getTime()));
        }
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
            cal.add(Calendar.DAY_OF_YEAR, -1);
            for(int i = 0; i< 6; i++){
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            days.add(sdf.format(cal.getTime()));
        }
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
            cal.add(Calendar.DAY_OF_YEAR, -2);
            for(int i = 0; i< 6; i++){
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            days.add(sdf.format(cal.getTime()));
        }
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
            cal.add(Calendar.DAY_OF_YEAR, -3);
            for(int i = 0; i< 6; i++){
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            days.add(sdf.format(cal.getTime()));
        }
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
            cal.add(Calendar.DAY_OF_YEAR, -4);
            for(int i = 0; i< 6; i++){
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            days.add(sdf.format(cal.getTime()));
        }
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
            cal.add(Calendar.DAY_OF_YEAR, -5);
            for(int i = 0; i< 6; i++){
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            days.add(sdf.format(cal.getTime()));
        }
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            cal.add(Calendar.DAY_OF_YEAR, -6);
            for(int i = 0; i< 6; i++){
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            days.add(sdf.format(cal.getTime()));
        }

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        String[] horizontalLabel = new String[days.size()];
        for (int i = 0;i < horizontalLabel.length;i++){
            horizontalLabel[i] = days.get(i);
        }
        staticLabelsFormatter.setHorizontalLabels(horizontalLabel);
        staticLabelsFormatter.setVerticalLabels(new String[] {"0", "1000", "2000", "3000","4000","5000","6000","7000"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 2),
                new DataPoint(1,5),
                new DataPoint(2, 4),
                new DataPoint(3, 4),
                new DataPoint(4, 8),
                new DataPoint(5, 6),
                new DataPoint(6, 8),

        });
        graph.addSeries(series);


        heartfrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_stepsFragment_to_fragment_Heart);
            }
        });

        waterfrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_stepsFragment_to_waterFragment);
            }
        });

        sleepfrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_stepsFragment_to_sleepFragment);
            }
        });

        weightfrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_stepsFragment_to_weightFragment);
            }
        });
        return view;
    }
}
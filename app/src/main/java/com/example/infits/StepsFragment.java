package com.example.infits;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepsFragment extends Fragment {

    RadioButton customdates_btn, year_btn, month_btn, week_btn;

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

        customdates_btn = view.findViewById(R.id.customdates_btn);
        year_btn = view.findViewById(R.id.year_btn);
        month_btn = view.findViewById(R.id.month_btn);
        week_btn = view.findViewById(R.id.week_btn);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
        MaterialDatePicker materialDatePicker = builder.build();

        customdates_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customdates_btn.setBackgroundResource(R.drawable.switch_on);
                materialDatePicker.show(getFragmentManager(),"DATE_PICKER");

            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Toast.makeText(getContext(),"Selected range: "+ materialDatePicker.getHeaderText(),Toast.LENGTH_SHORT).show();
            }
        });

        year_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                year_btn.setBackgroundResource(R.drawable.switch_on);

            }
        });

        month_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                month_btn.setBackgroundResource(R.drawable.switch_on);

            }
        });

        week_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                week_btn.setBackgroundResource(R.drawable.switch_on);

            }
        });


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


        return view;
    }
}
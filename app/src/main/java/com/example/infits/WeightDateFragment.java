package com.example.infits;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeightDateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeightDateFragment extends Fragment {

    RulerValuePicker rulerValuePicker;
    Button btnadd;
    TextView tv_weight, tv_weight2;
    int cur_weight;
    CalendarView calendarView;
    String curDate;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeightDateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeightDateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeightDateFragment newInstance(String param1, String param2) {
        WeightDateFragment fragment = new WeightDateFragment();
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
        View view = inflater.inflate(R.layout.fragment_weight_date, container, false);

        rulerValuePicker = view.findViewById(R.id.rulerValuePicker);
        btnadd = view.findViewById(R.id.btnadd);
        tv_weight = view.findViewById(R.id.tv_weight);
        tv_weight2 = view.findViewById(R.id.tv_weight2);
        calendarView = view.findViewById(R.id.calendarView);

        rulerValuePicker.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(int selectedValue) {

                tv_weight.setText(selectedValue+" KG");
                cur_weight = selectedValue;

            }

            @Override
            public void onIntermediateValueChange(int selectedValue) {

            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String curDay = String.valueOf(dayOfMonth);
                String curMonth = String.valueOf(month);
                String curYear = String.valueOf(year);
                curDate = curDay+" "+curMonth+" "+curYear;

            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("weight", String.valueOf(cur_weight));
                bundle.putString("weightChangeDate", String.valueOf(curDate));

                getParentFragmentManager().setFragmentResult("weightData", bundle);

                Navigation.findNavController(v).navigate(R.id.action_weightDateFragment_to_weightTrackerFragment);
            }
        });


        return view;
    }
}
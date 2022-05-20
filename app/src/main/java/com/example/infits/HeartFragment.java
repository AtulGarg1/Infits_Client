package com.example.infits;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeartFragment extends Fragment {

    RadioButton customdates_btn, year_btn, month_btn, week_btn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HeartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Heart.
     */
    // TODO: Rename and change types and number of parameters
    public static HeartFragment newInstance(String param1, String param2) {
        HeartFragment fragment = new HeartFragment();
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
        View view = inflater.inflate(R.layout.fragment_heart, container, false);

        customdates_btn = view.findViewById(R.id.customdates_btn);
        year_btn = view.findViewById(R.id.year_btn);
        month_btn = view.findViewById(R.id.month_btn);
        week_btn = view.findViewById(R.id.week_btn);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
        MaterialDatePicker materialDatePicker = builder.build();

        customdates_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customdates_btn.setBackgroundResource(R.drawable.switch_on_heart);
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

                year_btn.setBackgroundResource(R.drawable.switch_on_heart);

            }
        });

        month_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                month_btn.setBackgroundResource(R.drawable.switch_on_heart);

            }
        });

        week_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                week_btn.setBackgroundResource(R.drawable.switch_on_heart);

            }
        });

        return view;
    }
}
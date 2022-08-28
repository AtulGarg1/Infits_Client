package com.example.infits;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeartFragment extends Fragment {


    String customDates;
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

//        customdates_btn = view.findViewById(R.id.customdates_btn);
//        year_btn = view.findViewById(R.id.year_btn);
//        month_btn = view.findViewById(R.id.month_btn);
//
////        week_btn = view.findViewById(R.id.week_btn);
//
//        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.dateRangePicker();
//        MaterialDatePicker materialDatePicker = builder.build();
//
//        customdates_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                customdates_btn.setBackgroundResource(R.drawable.switch_on_heart);
//                materialDatePicker.show(getFragmentManager(),"DATE_PICKER");
//            }
//        });
//
//        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
//            @Override
//            public void onPositiveButtonClick(Object selection) {
//                Toast.makeText(getContext(),"Selected rrange: "+ materialDatePicker.getHeaderText(),Toast.LENGTH_SHORT).show();
//                customDates= materialDatePicker.getHeaderText();
//            }
//        });
//
//        year_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                year_btn.setBackgroundResource(R.drawable.switch_on_heart);
//
//            }
//        });
//
//        month_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                month_btn.setBackgroundResource(R.drawable.switch_on_heart);
//
//            }
//        });


//        week_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                week_btn.setBackgroundResource(R.drawable.switch_on_heart);
//
//            }
//        });

        return view;
    }





}


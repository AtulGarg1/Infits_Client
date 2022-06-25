package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

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
    MCalendarView calendarView;
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
//        tv_weight2 = view.findViewById(R.id.tv_weight2);
        calendarView = view.findViewById(R.id.calendarView);

        String urlMark = String.format("%sweightDate.php",DataFromDatabase.ipConfig);

        StringRequest stringRequestCal = new StringRequest(Request.Method.POST,urlMark,response -> {

            try {
                JSONObject object = new JSONObject(response);
                JSONArray weight = object.getJSONArray("weight");
                ArrayList<String> dates = new ArrayList<>();
                for (int i = 0;i<weight.length();i++){
                    JSONObject date = weight.getJSONObject(i);
                    String dateStr = date.getString("date");
                    dates.add(dateStr);
                    String[] array = dateStr.split("-");
                    System.out.println(dateStr);
                    calendarView.markDate(
                            new DateData(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2])).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.GREEN)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        },error -> {
                Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
//                Log.d("Fragment","clientuserID = " + DataFromDatabase.clientuserID);
                data.put("userID", "Azarudeen");
                return data;
            }
        };

        Volley.newRequestQueue(getContext()).add(stringRequestCal);

        String urlUnMark = String.format("%sunUpdated.php",DataFromDatabase.ipConfig);

        StringRequest stringRequestCalUn = new StringRequest(Request.Method.POST,urlUnMark,response -> {

            try {
                JSONObject object = new JSONObject(response);
                JSONArray weight = object.getJSONArray("dates");
                ArrayList<String> dates = new ArrayList<>();
                for (int i = 0;i<weight.length();i++){
//                    JSONObject date = weight.getJSONObject(i);
                    String dateStr = weight.getString(i);
                    dates.add(dateStr);
                    String[] array = dateStr.split("-");
                    System.out.println(dateStr);
                    calendarView.markDate(
                            new DateData(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2])).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.RED)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        },error -> {
            Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
//                Log.d("Fragment","clientuserID = " + DataFromDatabase.clientuserID);
                data.put("userID", "Azarudeen");
                return data;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequestCalUn);
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


        final DateData[] olddate = {new DateData(2022, 06, 11)};

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                Toast.makeText(getActivity(), String.format("%d-%d", date.getMonth(), date.getDay()), Toast.LENGTH_SHORT).show();

                calendarView.unMarkDate(olddate[0].getYear(),olddate[0].getMonth(),olddate[0].getDay());

                calendarView.markDate(date.getYear(), date.getMonth(), date.getDay()).setMarkedStyle(MarkStyle.BACKGROUND, Color.BLUE);

                olddate[0] = new DateData(date.getYear(),date.getMonth(),date.getDay());
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString("weight", String.valueOf(cur_weight));
//                bundle.putString("weightChangeDate", String.valueOf(curDate));

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Weight",MODE_PRIVATE);

                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                System.out.println(sharedPreferences.getString("Weight","0"));
                myEdit.putString("weight", String.valueOf(cur_weight));
                myEdit.putString("weightChangeDate", String.valueOf(curDate));

                myEdit.apply();

//                getParentFragmentManager().setFragmentResult("weightData", bundle);

                Navigation.findNavController(v).navigate(R.id.action_weightDateFragment_to_weightTrackerFragment);
            }
        });


        return view;
    }
}
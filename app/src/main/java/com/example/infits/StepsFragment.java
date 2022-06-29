package com.example.infits;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.archit.calendardaterangepicker.customviews.CalendarDateRangeManager;
import com.archit.calendardaterangepicker.customviews.CustomDateView;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.archit.calendardaterangepicker.models.CalendarStyleAttributes;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.savvi.rangedatepicker.CalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepsFragment extends Fragment {

    RequestQueue queue;
    String url = "http://192.168.9.1/infits/stepsFragment.php";
    String url1 = "http://192.168.9.1/infits/stepsVolley1.php";
    String url2 = "http://192.168.9.1/infits/stepsVolley2.php";
    String url3 = "http://192.168.9.1/infits/stepsVolley3.php";
    DataFromDatabase dataFromDatabase;

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

        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        LineChart lineChart =  view.findViewById(R.id.graph);
        ArrayList<Entry> NoOfEmp = new ArrayList<>();


        final LineDataSet[] dataSet = {new LineDataSet(NoOfEmp, "Number Of Employees")};
        dataSet[0].setDrawHorizontalHighlightIndicator(false);
        dataSet[0].setDrawVerticalHighlightIndicator(false);
        ArrayList<ILineDataSet> year = new ArrayList<>();
        year.add(dataSet[0]);

        dataSet[0].setColor(Color.parseColor("#FFA381"));
        dataSet[0].setCircleColor(Color.parseColor("#FFA381"));
        Typeface tf = ResourcesCompat.getFont(getContext(),R.font.nats_regular);
        LineData data = new LineData(dataSet[0]);

        XAxis xl = lineChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        lineChart.getAxisRight().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setDrawAxisLine(true);
        yAxisRight.setDrawGridLines(true);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setDrawGridLines(true);

        data.setValueTypeface(tf);

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                e.getData();
                CustomMarkerView customMarkerView = new CustomMarkerView(getContext(),R.layout.mark);
                lineChart.setMarker(customMarkerView);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        dataSet[0].setDrawValues(false);

        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);

        lineChart.setData(data);

        RadioButton week_radioButton = view.findViewById(R.id.week_btn_steps);
        RadioButton month_radioButton = view.findViewById(R.id.month_btn_steps);
        RadioButton year_radioButton = view.findViewById(R.id.year_btn_steps);
        RadioButton custom_radioButton = view.findViewById(R.id.customdates_btn_steps);
        week_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoOfEmp.removeAll(NoOfEmp);
                String url = String.format("%sstepsGraph.php",DataFromDatabase.ipConfig);
                StringRequest stringRequest = new StringRequest(Request.Method.GET,url,response -> {
                    List<String> allNames = new ArrayList<>();
                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response);
                        JSONArray cast = jsonResponse.getJSONArray("steps");
                        for (int i=0; i<cast.length(); i++) {
                            JSONObject actor = cast.getJSONObject(i);
                            String name = actor.getString("steps");
                            String date = actor.getString("date");
                            System.out.println(name+"   "+date);
                            allNames.add(name);
                            NoOfEmp.add(new Entry(i,Float.parseFloat(name)));
                            System.out.println("Points "+NoOfEmp.get(i));
                            dataSet[0] = (LineDataSet) lineChart.getData().getDataSetByIndex(0);

                            dataSet[0].setValues(NoOfEmp);

                            String[] xAxisLables = new String[]{"SUN","MON","TUE","WED","THU","FRI","SAT"};

                            lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLables));
                            dataSet[0].notifyDataSetChanged();
                            lineChart.getData().notifyDataChanged();
                            lineChart.notifyDataSetChanged();
                            lineChart.invalidate();
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                },error -> {
                    Log.d("Data",error.toString().trim());
                });
                Volley.newRequestQueue(getActivity()).add(stringRequest);
            }
        });

        month_radioButton.setOnClickListener(v->{
            NoOfEmp.removeAll(NoOfEmp);
            String url = String.format("%sstepsMonthGraph.php",DataFromDatabase.ipConfig);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url,response -> {
                List<String> allNames = new ArrayList<>();
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    JSONArray cast = jsonResponse.getJSONArray("steps");
                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        String name = actor.getString("steps");
                        String date = actor.getString("date");
                        System.out.println(name+"   "+date);
                        allNames.add(name);
                        NoOfEmp.add(new Entry(i,Float.parseFloat(name)));
                        System.out.println("Points "+NoOfEmp.get(i));
                        dataSet[0] = (LineDataSet) lineChart.getData().getDataSetByIndex(0);

                        dataSet[0].setValues(NoOfEmp);

                        dataSet[0].notifyDataSetChanged();

                        ArrayList<String> mons = new ArrayList<>();

                        for (int j = 0;j < 31;j++){
                            mons.add(String.valueOf(i));
                        }

                        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(mons));

                        lineChart.getData().notifyDataChanged();
                        lineChart.notifyDataSetChanged();
                        lineChart.invalidate();
                    }
                    ArrayList<String> mons = new ArrayList<>();

                    for (int i = 0;i < 31;i++){
                        mons.add(String.valueOf(i));
                    }

                    lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(mons));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            },error -> {
                Log.d("Data",error.toString().trim());
            });
            Volley.newRequestQueue(getActivity()).add(stringRequest);
        });
        year_radioButton.setOnClickListener(v->{
            NoOfEmp.removeAll(NoOfEmp);
            String url = String.format("%sstepsYearGraph.php",DataFromDatabase.ipConfig);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url,response -> {
                List<String> allNames = new ArrayList<>();
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    JSONArray cast = jsonResponse.getJSONArray("steps");
                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        String name = actor.getString("av");
                        System.out.println(name);
                        allNames.add(name);
                        NoOfEmp.add(new Entry(i,Float.parseFloat(name)));
                        System.out.println("Points "+NoOfEmp.get(i));
                        dataSet[0] = (LineDataSet) lineChart.getData().getDataSetByIndex(0);

                        dataSet[0].setValues(NoOfEmp);

                        dataSet[0].notifyDataSetChanged();
                        lineChart.getData().notifyDataChanged();
                        lineChart.notifyDataSetChanged();
                        lineChart.invalidate();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            },error -> {
                Log.d("Data",error.toString().trim());
            });
            Volley.newRequestQueue(getActivity()).add(stringRequest);
        });
        custom_radioButton.setOnClickListener(v->{
            final Dialog dialog = new Dialog(getActivity());
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.calender_dialog);
            final Calendar nextYear = Calendar.getInstance();
            nextYear.add(Calendar.YEAR,10);

            final Calendar lastYear = Calendar.getInstance();
            lastYear.add(Calendar.YEAR, -10);

//            DateRangeCalendarView cal = dialog.findViewById(R.id.cal_for_graph);

//            com.archit.calendardaterangepicker.customviews.CalendarDateRangeManagerImpl

            CalendarPickerView calendarPickerView = dialog.findViewById(R.id.cal_for_graph);
            calendarPickerView.init(lastYear.getTime(), nextYear.getTime(), new SimpleDateFormat("MMMM", Locale.getDefault())).inMode(CalendarPickerView.SelectionMode.RANGE).withSelectedDate(new Date());


            Button done = dialog.findViewById(R.id.done);
            Button cancel = dialog.findViewById(R.id.cancel);

            done.setOnClickListener(vi->{
//                List<Date> dates = calendarPickerView.getSelectedDates();
//                SimpleDateFormat sf = new SimpleDateFormat("MMM dd,yyyy");
//                String from = sf.format(dates.get(0));
//                String to = sf.format(dates.get(dates.size()-1));

                NoOfEmp.removeAll(NoOfEmp);
                String url = String.format("%sstepsYearGraph.php",DataFromDatabase.ipConfig);
                StringRequest stringRequest = new StringRequest(Request.Method.GET,url,response -> {
                    List<String> allNames = new ArrayList<>();
                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response);
                        JSONArray cast = jsonResponse.getJSONArray("steps");
                        for (int i=0; i<cast.length(); i++) {
                            JSONObject actor = cast.getJSONObject(i);
                            String name = actor.getString("av");
                            System.out.println(name);
                            allNames.add(name);
                            NoOfEmp.add(new Entry(i,Float.parseFloat(name)));
                            System.out.println("Points "+NoOfEmp.get(i));
                            dataSet[0] = (LineDataSet) lineChart.getData().getDataSetByIndex(0);

                            dataSet[0].setValues(NoOfEmp);

                            dataSet[0].notifyDataSetChanged();
                            lineChart.getData().notifyDataChanged();
                            lineChart.notifyDataSetChanged();
                            lineChart.invalidate();
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                },error -> {
                    Log.d("Data",error.toString().trim());
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> data = new HashMap<>();
//                        data.put("from",from);
//                        data.put("to",to);
                        return data;
                    }
                };
                Volley.newRequestQueue(getActivity()).add(stringRequest);
                dialog.dismiss();
            });

            cancel.setOnClickListener(vi->{
                dialog.dismiss();
            });

            dialog.show();
        });

//        queue = Volley.newRequestQueue(getContext());
//        Log.d("HeartFragment","before");
//        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
//            if (!response.equals("failure")){
//                Log.d("HeartFragment","success");
//                Log.d("heartFragment response",response);
//
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    JSONObject object0 = jsonArray.getJSONObject(0);
//                    JSONObject object1 = jsonArray.getJSONObject(1);
//                    JSONObject object2 = jsonArray.getJSONObject(2);
//                    JSONObject object3 = jsonArray.getJSONObject(3);
//                    String stepsWeek = object0.getString("stepsSumWeek");
//                    if (stepsWeek.equals("null")){
//                        stepsWeek ="0";
//                    }
//                    weekly.setText(stepsWeek);
//                    String stepsMonth = object1.getString("stepsSumMonth");
//                    if (stepsMonth.equals("null")){
//                        stepsMonth ="0";
//                    }
//                    monthly.setText(stepsMonth);
//                    String stepsDaily = object2.getString("stepsSumDaily");
//                    if (stepsDaily.equals("null")){
//                        stepsDaily ="0";
//                    }
//                    daily.setText(stepsDaily);
//                    String stepsTotal = object3.getString("stepsSumTotal");
//                    if (stepsTotal.equals("null")){
//                        stepsTotal ="0";
//                    }
//                    total.setText(stepsTotal);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            else if (response.equals("failure")){
//                Log.d("HeartFragment","failure");
//                Toast.makeText(getContext(), "heartFragment failed", Toast.LENGTH_SHORT).show();
//            }
//        },error -> {
//        //    Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
//        })
//        {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> data = new HashMap<>();
//                Log.d("Fragment","clientuserID = "+dataFromDatabase.clientuserID);
//                data.put("userID", dataFromDatabase.clientuserID);
//                return data;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//        Log.d("Fragment","at end");
//        ArrayList steps = dates(27,01,2022,22,05,2022);
//        for (int i=0;i<steps.size();i++){
//            Log.d("steps arraylist",steps.get(i).toString());
//        }
        return view;
    }

//    public ArrayList dates(Integer date1,Integer mnth1,Integer year1,Integer date2,Integer mnth2,Integer year2){
//
//        ArrayList steps=new ArrayList<>();
//        GregorianCalendar gc1 = new GregorianCalendar();
//        gc1.set(Calendar.DAY_OF_WEEK,date1);
//        gc1.set(Calendar.MONTH,mnth1);
//        gc1.set(Calendar.YEAR,year1);
//        int numofdayspassed1 = gc1.get(Calendar.DAY_OF_YEAR);
//
//        GregorianCalendar gc2 = new GregorianCalendar();
//        gc2.set(Calendar.DAY_OF_WEEK,date2);
//        gc2.set(Calendar.MONTH,mnth2);
//        gc2.set(Calendar.YEAR,year2);
//        int numofdayspassed2 = gc2.get(Calendar.DAY_OF_YEAR);
//
//        Log.d("number",numofdayspassed2+"  ");
//        Log.d("number",numofdayspassed1+"  ");
//
//        if (numofdayspassed2-numofdayspassed1<=30){
//            int numberofdays = numofdayspassed2-numofdayspassed1;
//
//            queue = Volley.newRequestQueue(getContext());
//            Log.d("Fragment","before");
//            StringRequest stringRequest = new StringRequest(Request.Method.POST,url1, response -> {
//                if (!response.equals("failure")){
//                    Log.d("Fragment","success");
//                    Log.d("Fragment response",response);
//
//                    try {
//                        JSONArray jsonArray = new JSONArray(response);
//                        for (int z=0;z< jsonArray.length();z++){
//                            JSONObject object = jsonArray.getJSONObject(z);
//                            String sum = object.getString("Sum");
//                            String date = object.getString("date");
//                            steps.add(sum);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else if (response.equals("failure")){
//                    Log.d("Fragment","failure");
//                    Toast.makeText(getContext(), "Fragment failed", Toast.LENGTH_SHORT).show();
//                }
//            },error -> {
//                //    Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
//            })
//            {
//                @Nullable
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> data = new HashMap<>();
//                    Log.d("Fragment","clientuserID = "+dataFromDatabase.clientuserID);
//                    data.put("userID", dataFromDatabase.clientuserID);
//                    // '2022-07-03'
//                    data.put("date1", String.valueOf(year1)+"-"+String.valueOf(mnth1)+"-"+String.valueOf(date1));
//                    data.put("date2", String.valueOf(year2)+"-"+String.valueOf(mnth2)+"-"+String.valueOf(date2));
//                    return data;
//                }
//            };
//            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//            requestQueue.add(stringRequest);
//            Log.d("Fragment","at end");
//
//        }else if (numofdayspassed2-numofdayspassed1>30 && year1==year2){
//
//            int numberofmonths = mnth2-mnth1;
//            for (int i=0;i<numberofmonths;i++){
//                Calendar cal = Calendar.getInstance();
//                cal.add(mnth1,i);
//                Log.d("month", i+" "+String.valueOf(mnth1));
//
//                queue = Volley.newRequestQueue(getContext());
//                Log.d("Fragment","before");
//                StringRequest stringRequest = new StringRequest(Request.Method.POST,url2, response -> {
//                    if (!response.equals("failure")){
//                        Log.d("Fragment","success");
//                        Log.d("Fragment response",response);
//
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            JSONObject object = jsonArray.getJSONObject(0);
//                            String sum = object.getString("Sum");
//                            steps.add(sum);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    else if (response.equals("failure")){
//                        Log.d("Fragment","failure");
//                        Toast.makeText(getContext(), "Fragment failed", Toast.LENGTH_SHORT).show();
//                    }
//                },error -> {
//                    //    Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
//                })
//                {
//                    @Nullable
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> data = new HashMap<>();
//                        Log.d("Fragment","clientuserID = "+dataFromDatabase.clientuserID);
//                        data.put("userID", dataFromDatabase.clientuserID);
//                        // '2022-05%'
//                        data.put("date", String.valueOf(year1)+"-"+String.valueOf(mnth1)+"%");
//                        return data;
//                    }
//                };
//                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//                requestQueue.add(stringRequest);
//                Log.d("Fragment","at end");
//            }
//        }else if (year1!=year2){
//
//            int numberofyears = year2-year1;
//            for (int i=0;i<numberofyears;i++){
//                Calendar cal = Calendar.getInstance();
//                cal.add(year1,i);
//                Log.d("year", i+" "+String.valueOf(year1));
//
//                queue = Volley.newRequestQueue(getContext());
//                Log.d("Fragment","before");
//                StringRequest stringRequest = new StringRequest(Request.Method.POST,url3, response -> {
//                    if (!response.equals("failure")){
//                        Log.d("Fragment","success");
//                        Log.d("Fragment response",response);
//
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            JSONObject object = jsonArray.getJSONObject(0);
//                            String sum = object.getString("Sum");
//                            steps.add(sum);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    else if (response.equals("failure")){
//                        Log.d("Fragment","failure");
//                        Toast.makeText(getContext(), "Fragment failed", Toast.LENGTH_SHORT).show();
//                    }
//                },error -> {
//                    //    Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
//                })
//                {
//                    @Nullable
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> data = new HashMap<>();
//                        Log.d("Fragment","clientuserID = "+dataFromDatabase.clientuserID);
//                        data.put("userID", dataFromDatabase.clientuserID);
//                        // '2022%'
//                        data.put("date", String.valueOf(year1)+"%");
//                        return data;
//                    }
//                };
//                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//                requestQueue.add(stringRequest);
//                Log.d("Fragment","at end");
//            }
//        }
//        return steps;
//    }
}
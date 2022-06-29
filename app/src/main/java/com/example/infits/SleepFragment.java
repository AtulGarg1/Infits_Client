package com.example.infits;

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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SleepFragment extends Fragment {

    RequestQueue queue;
    String url = "http://192.168.9.1/infits/sleepFragment.php";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SleepFragment() {

    }

    public static SleepFragment newInstance(String param1, String param2) {
        SleepFragment fragment = new SleepFragment();
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
        View view = inflater.inflate(R.layout.fragment_sleep, container, false);

        LineChart lineChart =  view.findViewById(R.id.graph);
        ArrayList<Entry> NoOfEmp = new ArrayList<>();


        final LineDataSet[] dataSet = {new LineDataSet(NoOfEmp, "Number Of Employees")};
        dataSet[0].setDrawHorizontalHighlightIndicator(false);
        dataSet[0].setDrawVerticalHighlightIndicator(false);
        ArrayList<ILineDataSet> year = new ArrayList<>();
        year.add(dataSet[0]);

        dataSet[0].setColor(Color.parseColor("#9C74F5"));
        dataSet[0].setCircleColor(Color.parseColor("#9C74F5"));
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

        RadioButton week_radioButton = view.findViewById(R.id.week_btn_sleep);
        RadioButton month_radioButton = view.findViewById(R.id.month_btn_sleep);
        RadioButton year_radioButton = view.findViewById(R.id.year_btn_sleep);
        RadioButton custom_radioButton = view.findViewById(R.id.customdates_btn_sleep);
        week_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoOfEmp.removeAll(NoOfEmp);
                String url = "http://192.168.17.91/infits/waterGraph.php";
                StringRequest stringRequest = new StringRequest(Request.Method.GET,url,response -> {
                    List<String> allNames = new ArrayList<>();
                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response);
                        JSONArray cast = jsonResponse.getJSONArray("water");
                        for (int i=0; i<cast.length(); i++) {
                            JSONObject actor = cast.getJSONObject(i);
                            String name = actor.getString("drink");
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
            String url = "http://192.168.17.91/infits/waterMonthGraph.php";
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url,response -> {
                List<String> allNames = new ArrayList<>();
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    JSONArray cast = jsonResponse.getJSONArray("water");
                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        String name = actor.getString("drink");
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
            String url = "http://192.168.17.91/infits/sleepYearGraph.php";
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url,response -> {
                List<String> allNames = new ArrayList<>();
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    JSONArray cast = jsonResponse.getJSONArray("water");
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
            final GraphView graph = (GraphView) view.findViewById(R.id.graph);
            String url = "http://192.168.110.91/infits/stepsGraph.php";
            String from = "2022-09-10";
            String to = "2022-09-17";
            SimpleDateFormat fromTo = new SimpleDateFormat("yyyy-MM-dd");
            String [] days = new String[7];
            float[] dataPoints= new float[7];
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url,response -> {
                graph.removeAllSeries();
                List<String> allNames = new ArrayList<String>();
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    JSONArray cast = jsonResponse.getJSONArray("sleep");

                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        String name = actor.getString("sleep");
                        String date = actor.getString("date");
                        allNames.add(name);
                        Log.d("Length", allNames.get(i));
                        days[i] = date;
                        dataPoints[i] = Float.parseFloat(allNames.get(i))/1000;
                        Log.d("Length", String.valueOf(dataPoints[i]));
                        Log.d("Length",days[i]);
                    }
                    DataPoint[] values = new DataPoint[dataPoints.length];
                    for(int i =0; i<dataPoints.length; i++){
                        if (i==0){
                            DataPoint val = new DataPoint(0, 0);
                            values[i] = val;
                        }
                        else if (i == 6){
                            DataPoint val = new DataPoint(i+1, dataPoints[i]);
                            values[i] = val;
                        }
                        else{
                            DataPoint val = new DataPoint(i, dataPoints[i]);
                            values[i] = val;
                        }
                    }
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(values);
                    StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
                    staticLabelsFormatter.setHorizontalLabels(days);
                    staticLabelsFormatter.setVerticalLabels(new String[] {"0", "1 ", "2 ", "3 ","4 ","5 ","6 ","7 ","8 ","9 ","10"});
                    graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                    graph.getGridLabelRenderer().setNumHorizontalLabels(7);
                    graph.getViewport().setMinX(1);
                    graph.getViewport().setMaxX(7);
                    graph.getViewport().setXAxisBoundsManual(true);
                    series.setDrawDataPoints(true);
                    series.setDataPointsRadius(10);
                    graph.addSeries(series);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            },error -> {
                Log.d("Data",error.toString().trim());
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> data = new HashMap<>();
                    data.put("from",from);
                    data.put("to",to);
                    return data;
                }
            };
            Volley.newRequestQueue(getActivity()).add(stringRequest);
        });


//        queue = Volley.newRequestQueue(getContext());
//        Log.d("Fragment","before");
//        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
//            if (!response.equals("failure")){
//                Log.d("Fragment","success");
//                Log.d("Fragment response",response);
//
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    JSONObject object0 = jsonArray.getJSONObject(0);
//                    JSONObject object1 = jsonArray.getJSONObject(1);
//                    JSONObject object2 = jsonArray.getJSONObject(2);
//                    JSONObject object3 = jsonArray.getJSONObject(3);
//                    String stepsWeek = object0.getString("SumWeek");
//                    if (stepsWeek.equals("null")){
//                        stepsWeek ="0";
//                    }
//                    weekly.setText(stepsWeek);
//                    String stepsMonth = object1.getString("SumMonth");
//                    if (stepsMonth.equals("null")){
//                        stepsMonth ="0";
//                    }
//                    monthly.setText(stepsMonth);
//                    String stepsDaily = object2.getString("SumDaily");
//                    if (stepsDaily.equals("null")){
//                        stepsDaily ="0";
//                    }
//                    daily.setText(stepsDaily);
//                    String stepsTotal = object3.getString("SumTotal");
//                    if (stepsTotal.equals("null")){
//                        stepsTotal ="0";
//                    }
//                    total.setText(stepsTotal);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            else if (response.equals("failure")){
//                Log.d("Fragment","failure");
//                Toast.makeText(getContext(), "Fragment failed", Toast.LENGTH_SHORT).show();
//            }
//        },error -> {
//            Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();})
//        {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> data = new HashMap<>();
//                Log.d("Fragment","clientuserID = "+"Azarudeen");
//                data.put("userID", "Azarudeen");
//                return data;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
        Log.d("Fragment","at end");
        return view;
    }
}
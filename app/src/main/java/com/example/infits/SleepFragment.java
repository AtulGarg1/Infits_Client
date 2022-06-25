package com.example.infits;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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

    TextView daily,monthly,weekly,total;
    RequestQueue queue;
    String url = "http://192.168.9.1/infits/sleepFragment.php";
    DataFromDatabase dataFromDatabase;

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

        daily=view.findViewById(R.id.dailytv2);
        monthly=view.findViewById(R.id.monthlytv2);
        weekly=view.findViewById(R.id.weeklytv2);
        total=view.findViewById(R.id.totaltv2);


        RadioButton week_radioButton = view.findViewById(R.id.week_btn_sleep);
        RadioButton month_radioButton = view.findViewById(R.id.month_btn_sleep);
        RadioButton year_radioButton = view.findViewById(R.id.year_btn_sleep);
        RadioButton custom_radioButton = view.findViewById(R.id.customdates_btn_sleep);
        week_radioButton.setOnClickListener(v->{
            final GraphView graph = (GraphView) view.findViewById(R.id.graph);
            String url = "http://192.168.110.91/infits/sleepGraph.php";
            String from = "";
            String to = "";
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
                        String name = actor.getString("hrsSlept");
                        String date = actor.getString("date");
                        allNames.add(name);
                        Log.d("Length", allNames.get(i));
                        Log.d("Length", String.valueOf(days.length));
                        days[i] = date;
                        dataPoints[i] = Float.parseFloat(allNames.get(i))/5;
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
                    staticLabelsFormatter.setVerticalLabels(new String[] {"0", "1 ", "2 ", "3 ","4 ","5 ","6 ","7 ","8 ","9 ","1 0"});
                    graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                    graph.getGridLabelRenderer().setNumHorizontalLabels(7);
                    graph.getViewport().setMinX(0);
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

                    data.put("option","Month");

                    return data;
                }
            };
            Volley.newRequestQueue(getActivity()).add(stringRequest);
        });
        month_radioButton.setOnClickListener(v->{
            final GraphView graphMonth = (GraphView) view.findViewById(R.id.graph);
            graphMonth.removeAllSeries();
            String url = "http://192.168.110.91/infits/sleepMonthGraph.php";
            String from = "";
            String to = "";
            SimpleDateFormat fromTo = new SimpleDateFormat("yyyy-MM-dd");
            String [] days = new String[31];
            float[] dataPoints= new float[31];
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url,response -> {
                List<String> allNames = new ArrayList<>();
                JSONObject jsonResponse;
                try {
                    jsonResponse = new JSONObject(response);
                    JSONArray cast = jsonResponse.getJSONArray("sleep");
                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        String name = actor.getString("hrsSlept");
                        String date = actor.getString("date");
                        allNames.add(name);
                        System.out.println(date);
                        System.out.println(name);
                        days[i] = date;
                        dataPoints[i] = Float.parseFloat(allNames.get(i))/1000;
                    }

                    DataPoint[] values = new DataPoint[dataPoints.length];
                    for(int i =0; i<dataPoints.length; i++){
                        DataPoint val = new DataPoint(i, dataPoints[i]);
                        values[i] = val;
                    }

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(values);
                    StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphMonth);
                    staticLabelsFormatter.setHorizontalLabels(days);
                    staticLabelsFormatter.setVerticalLabels(new String[] {"0", "1 ", "2 ", "3 ","4 ","5 ","6 ","7 ","8 ","9 ","1 0"});
                    graphMonth.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                    graphMonth.getGridLabelRenderer().setNumHorizontalLabels(7);
                    graphMonth.getViewport().setMinX(1);
                    graphMonth.getViewport().setMaxX(31);
                    graphMonth.getViewport().setXAxisBoundsManual(true);

                    Log.d("length", String.valueOf(dataPoints.length));
                    series.setDrawDataPoints(true);
                    series.setDataPointsRadius(7);
                    graphMonth.addSeries(series);
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

                    data.put("","");

                    return data;
                }
            };
            Volley.newRequestQueue(getActivity()).add(stringRequest);
        });
        year_radioButton.setOnClickListener(v->{
            final GraphView graphMonth = (GraphView) view.findViewById(R.id.graph);
            graphMonth.removeAllSeries();
            String url = "http://192.168.110.91/infits/sleepYearGraph.php";
            String from = "";
            String to = "";
            SimpleDateFormat fromTo = new SimpleDateFormat("yyyy-MM-dd");
            String [] days = new String[12];
            float[] dataPoints= new float[12];
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url,response -> {
                List<String> allNames = new ArrayList<String>();
                try {
                    JSONArray cast = new JSONArray(response);
                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        String name = actor.getString("av");
                        allNames.add(name);
                        dataPoints[i] = Float.parseFloat(allNames.get(i));
                    }

                    DataPoint[] values = new DataPoint[dataPoints.length];
                    for(int i =0; i<dataPoints.length; i++){
                        if (i==0){
                            DataPoint val = new DataPoint(0,0);
                            values[i] = val;
                        }
                        if (i==11){
                            DataPoint val = new DataPoint(i+1 , dataPoints[i]);
                            values[i] = val;
                        }
                        else {
                            DataPoint val = new DataPoint(i , dataPoints[i]);
                            Log.d("Step", String.valueOf(dataPoints.length));
                            values[i] = val;
                            Log.d("vals", String.valueOf(val));
                        }
                    }

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(values);
                    StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphMonth);
                    staticLabelsFormatter.setHorizontalLabels(new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12"});
                    staticLabelsFormatter.setVerticalLabels(new String[] {"0", "1", "2", "3 ","4 ","5 ","6 ","7 ","8 ","9 ","1 0"});
                    graphMonth.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

                    graphMonth.getGridLabelRenderer().setNumHorizontalLabels(6);
                    graphMonth.getViewport().setMinX(0);
                    graphMonth.getViewport().setMaxX(12);
                    graphMonth.getViewport().setXAxisBoundsManual(true);

                    Log.d("length", String.valueOf(dataPoints.length));
                    series.setDrawDataPoints(true);
                    series.setDataPointsRadius(7);
                    graphMonth.addSeries(series);
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

                    data.put("option","Week");

                    return data;
                }
            };
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


        queue = Volley.newRequestQueue(getContext());
        Log.d("Fragment","before");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            if (!response.equals("failure")){
                Log.d("Fragment","success");
                Log.d("Fragment response",response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object0 = jsonArray.getJSONObject(0);
                    JSONObject object1 = jsonArray.getJSONObject(1);
                    JSONObject object2 = jsonArray.getJSONObject(2);
                    JSONObject object3 = jsonArray.getJSONObject(3);
                    String stepsWeek = object0.getString("SumWeek");
                    if (stepsWeek.equals("null")){
                        stepsWeek ="0";
                    }
                    weekly.setText(stepsWeek);
                    String stepsMonth = object1.getString("SumMonth");
                    if (stepsMonth.equals("null")){
                        stepsMonth ="0";
                    }
                    monthly.setText(stepsMonth);
                    String stepsDaily = object2.getString("SumDaily");
                    if (stepsDaily.equals("null")){
                        stepsDaily ="0";
                    }
                    daily.setText(stepsDaily);
                    String stepsTotal = object3.getString("SumTotal");
                    if (stepsTotal.equals("null")){
                        stepsTotal ="0";
                    }
                    total.setText(stepsTotal);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else if (response.equals("failure")){
                Log.d("Fragment","failure");
                Toast.makeText(getContext(), "Fragment failed", Toast.LENGTH_SHORT).show();
            }
        },error -> {
            Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();})
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                Log.d("Fragment","clientuserID = "+"Azarudeen");
                data.put("userID", "Azarudeen");
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        Log.d("Fragment","at end");


        return view;
    }
}
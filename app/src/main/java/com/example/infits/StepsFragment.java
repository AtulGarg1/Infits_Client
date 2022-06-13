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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepsFragment extends Fragment {

    TextView daily,monthly,weekly,total;
    RequestQueue queue;
    String url = "http://192.168.95.1/infits/stepsFragment.php";
    String url1 = "http://192.168.95.1/infits/stepsVolley1.php";
    String url2 = "http://192.168.95.1/infits/stepsVolley2.php";
    String url3 = "http://192.168.95.1/infits/stepsVolley3.php";
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        daily=view.findViewById(R.id.dailytv2);
        monthly=view.findViewById(R.id.monthlytv2);
        weekly=view.findViewById(R.id.weeklytv2);
        total=view.findViewById(R.id.totaltv2);

        RadioButton week_radioButton = view.findViewById(R.id.week_btn_steps);
        RadioButton month_radioButton = view.findViewById(R.id.month_btn_steps);
        RadioButton year_radioButton = view.findViewById(R.id.year_btn_steps);
        RadioButton custom_radioButton = view.findViewById(R.id.customdates_btn_steps);
        week_radioButton.setOnClickListener(v->{
            final GraphView graph = (GraphView) view.findViewById(R.id.graph);
            String url = "http://192.168.72.91/infits/stepsGraph.php";
            String from = "";
            String to = "";
            SimpleDateFormat fromTo = new SimpleDateFormat("yyyy-MM-dd");
            String [] days = new String[7];
            float[] dataPoints= new float[7];
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url, response -> {
                graph.removeAllSeries();
                List<String> allNames = new ArrayList<String>();
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    JSONArray cast = jsonResponse.getJSONArray("steps");

                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        String name = actor.getString("steps");
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
                    staticLabelsFormatter.setVerticalLabels(new String[] {"0", "1000", "2000", "3000","4000","5000","6000","7000","8000","9000","10000"});
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

                    data.put("option","Month");

                    return data;
                }
            };
            Volley.newRequestQueue(getActivity()).add(stringRequest);
        });
        month_radioButton.setOnClickListener(v->{
            final GraphView graphMonth = (GraphView) view.findViewById(R.id.graph);
            graphMonth.removeAllSeries();
            String url = "http://192.168.72.91/infits/stepMonthGraph.php";
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
                    JSONArray cast = jsonResponse.getJSONArray("steps");
                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        String name = actor.getString("steps");
                        String date = actor.getString("date");
                        allNames.add(name);
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
                    staticLabelsFormatter.setVerticalLabels(new String[] {"0", "1000", "2000", "3000","4000","5000","6000","7000","8000","9000","10000"});
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

                    data.put("option","Week");

                    return data;
                }
            };
            Volley.newRequestQueue(getActivity()).add(stringRequest);
        });
        year_radioButton.setOnClickListener(v->{
            final GraphView graphMonth = (GraphView) view.findViewById(R.id.graph);
            graphMonth.removeAllSeries();
            String url = "http://192.168.72.91/infits/stepYearGraph.php";
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
                        dataPoints[i] = Float.parseFloat(allNames.get(i))/1000;
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
                    staticLabelsFormatter.setVerticalLabels(new String[] {"0", "1000", "2000", "3000","4000","5000","6000","7000","8000","9000","10000"});
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
            String url = "http://192.168.72.91/infits/stepsGraph.php";
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
                    JSONArray cast = jsonResponse.getJSONArray("steps");

                    for (int i=0; i<cast.length(); i++) {
                        JSONObject actor = cast.getJSONObject(i);
                        String name = actor.getString("steps");
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
                    staticLabelsFormatter.setVerticalLabels(new String[] {"0", "1000", "2000", "3000","4000","5000",
                            "6000","7000","8000","9000","10000"});
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
        Log.d("HeartFragment","before");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            if (!response.equals("failure")){
                Log.d("HeartFragment","success");
                Log.d("heartFragment response",response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object0 = jsonArray.getJSONObject(0);
                    JSONObject object1 = jsonArray.getJSONObject(1);
                    JSONObject object2 = jsonArray.getJSONObject(2);
                    JSONObject object3 = jsonArray.getJSONObject(3);
                    String stepsWeek = object0.getString("stepsSumWeek");
                    if (stepsWeek.equals("null")){
                        stepsWeek ="0";
                    }
                    weekly.setText(stepsWeek);
                    String stepsMonth = object1.getString("stepsSumMonth");
                    if (stepsMonth.equals("null")){
                        stepsMonth ="0";
                    }
                    monthly.setText(stepsMonth);
                    String stepsDaily = object2.getString("stepsSumDaily");
                    if (stepsDaily.equals("null")){
                        stepsDaily ="0";
                    }
                    daily.setText(stepsDaily);
                    String stepsTotal = object3.getString("stepsSumTotal");
                    if (stepsTotal.equals("null")){
                        stepsTotal ="0";
                    }
                    total.setText(stepsTotal);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (response.equals("failure")){
                Log.d("HeartFragment","failure");
                Toast.makeText(getContext(), "heartFragment failed", Toast.LENGTH_SHORT).show();
            }
        },error -> {
        //    Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                Log.d("Fragment","clientuserID = "+dataFromDatabase.clientuserID);
                data.put("userID", dataFromDatabase.clientuserID);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        Log.d("Fragment","at end");

        ArrayList steps = dates(21,03,2022,22,04,2022);
        for (int i=0;i<steps.size();i++){
            Log.d("steps arraylist",steps.get(i).toString());
        }
        return view;
    }



    public ArrayList dates(Integer date1,Integer mnth1,Integer year1,Integer date2,Integer mnth2,Integer year2){

        ArrayList steps=new ArrayList<>();
        GregorianCalendar gc1 = new GregorianCalendar();
        gc1.set(Calendar.DAY_OF_YEAR,date1);
        gc1.set(Calendar.MONTH,mnth1);
        gc1.set(Calendar.YEAR,year1);
        int numofdayspassed1 = gc1.get(Calendar.DAY_OF_YEAR);

        GregorianCalendar gc2 = new GregorianCalendar();
        gc2.set(Calendar.DAY_OF_YEAR,date2);
        gc2.set(Calendar.MONTH,mnth2);
        gc2.set(Calendar.YEAR,year2);
        int numofdayspassed2 = gc2.get(Calendar.DAY_OF_YEAR);

        if (numofdayspassed2-numofdayspassed1<=30){
            int numberofdays = numofdayspassed2-numofdayspassed1;

                queue = Volley.newRequestQueue(getContext());
                Log.d("Fragment","before");
                StringRequest stringRequest = new StringRequest(Request.Method.POST,url1, response -> {
                    if (!response.equals("failure")){
                        Log.d("Fragment","success");
                        Log.d("Fragment response",response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int z=0;z< jsonArray.length();z++){
                                JSONObject object = jsonArray.getJSONObject(z);
                                String sum = object.getString("sum");
                                String date = object.getString("date");
                                steps.add(sum);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (response.equals("failure")){
                        Log.d("Fragment","failure");
                        Toast.makeText(getContext(), "Fragment failed", Toast.LENGTH_SHORT).show();
                    }
                },error -> {
                    //    Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
                })
                {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        Log.d("Fragment","clientuserID = "+dataFromDatabase.clientuserID);
                        data.put("userID", dataFromDatabase.clientuserID);
                        // '2022-07-03'
                        data.put("date1", String.valueOf(year1)+"-"+String.valueOf(mnth1)+"-"+String.valueOf(date1));
                        data.put("date2", String.valueOf(year2)+"-"+String.valueOf(mnth2)+"-"+String.valueOf(date2));
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
                Log.d("Fragment","at end");

        }else if (numofdayspassed2-numofdayspassed1>30 && year1==year2){

            int numberofmonths = mnth2-mnth1;
            for (int i=0;i<numberofmonths;i++){
                Calendar cal = Calendar.getInstance();
                cal.add(mnth1,i);
                Log.d("month", i+" "+String.valueOf(mnth1));

                queue = Volley.newRequestQueue(getContext());
                Log.d("Fragment","before");
                StringRequest stringRequest = new StringRequest(Request.Method.POST,url2, response -> {
                    if (!response.equals("failure")){
                        Log.d("Fragment","success");
                        Log.d("Fragment response",response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject object = jsonArray.getJSONObject(0);
                            String sum = object.getString("sum");
                            steps.add(sum);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    else if (response.equals("failure")){
                        Log.d("Fragment","failure");
                        Toast.makeText(getContext(), "Fragment failed", Toast.LENGTH_SHORT).show();
                    }
                },error -> {
                    //    Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
                })
                {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        Log.d("Fragment","clientuserID = "+dataFromDatabase.clientuserID);
                        data.put("userID", dataFromDatabase.clientuserID);
                        // '2022-05%'
                        data.put("date", String.valueOf(year1)+"-"+String.valueOf(mnth1)+"%");
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
                Log.d("Fragment","at end");
            }
        }else if (year1!=year2){

            int numberofyears = year2-year1;
            for (int i=0;i<numberofyears;i++){
                Calendar cal = Calendar.getInstance();
                cal.add(year1,i);
                Log.d("year", i+" "+String.valueOf(year1));

                queue = Volley.newRequestQueue(getContext());
                Log.d("Fragment","before");
                StringRequest stringRequest = new StringRequest(Request.Method.POST,url3, response -> {
                    if (!response.equals("failure")){
                        Log.d("Fragment","success");
                        Log.d("Fragment response",response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject object = jsonArray.getJSONObject(0);
                            String sum = object.getString("sum");
                            steps.add(sum);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    else if (response.equals("failure")){
                        Log.d("Fragment","failure");
                        Toast.makeText(getContext(), "Fragment failed", Toast.LENGTH_SHORT).show();
                    }
                },error -> {
                    //    Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
                })
                {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        Log.d("Fragment","clientuserID = "+dataFromDatabase.clientuserID);
                        data.put("userID", dataFromDatabase.clientuserID);
                        // '2022%'
                        data.put("date", String.valueOf(year1)+"%");
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
                Log.d("Fragment","at end");
            }
        }
        return steps;
    }
}
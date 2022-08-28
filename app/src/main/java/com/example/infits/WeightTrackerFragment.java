package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeightTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeightTrackerFragment extends Fragment {


    RulerValuePicker rulerValuePicker;
    Button btnadd;
    TextView tv_weight, tv_weight2;
    int cur_weight;

    String curDate;

    MaterialCalendarView mcv;

    RecyclerView rv,rvh,rvw;
    PickerAdapter adapter,adapter2;

    final ArrayList<String> pinkDateList = new ArrayList<>();
    final ArrayList<String> grayDateList = new ArrayList<>();

    final String DATE_FORMAT = "yyyy-MM-dd";

    int pink = 0;
    int green = 1;
    int blue = 2;

    CardView date_click;

    ImageButton adddet;
    ImageView imgback;
    TextView textbmi, curWeight,date_view;
    public int userWeight;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public WeightTrackerFragment() {

    }

    public static WeightTrackerFragment newInstance(String param1, String param2) {
        WeightTrackerFragment fragment = new WeightTrackerFragment();
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
        View view = inflater.inflate(R.layout.fragment_weight_tracker, container, false);

        SharedPreferences sh = getActivity().getSharedPreferences("Weight",MODE_PRIVATE);

        adddet = view.findViewById(R.id.adddet);
        textbmi = view.findViewById(R.id.bmi);
        imgback = view.findViewById(R.id.imgback);
        curWeight = view.findViewById(R.id.curWeight);
        date_view = view.findViewById(R.id.date);
        date_click = view.findViewById(R.id.date_click);

        curWeight.setText(sh.getString("weight","0"));

        Date dateToday = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        date_view.setText(sdf.format(dateToday));

        date_click.setOnClickListener(v->{
            final Dialog dialog = new Dialog(getActivity());
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.fragment_weight_date);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            mcv = dialog.findViewById(R.id.calendarView);

            rv =  dialog.findViewById(R.id.rv);

            Calendar cal = Calendar.getInstance();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mcv.state().edit()
                        .setMinimumDate(CalendarDay.from(2022, 4, 3))
                        .setMaximumDate(CalendarDay.from(2100, 12, 31))
                        .setCalendarDisplayMode(CalendarMode.MONTHS)
                        .commit();
                mcv.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
            }

            markGreen();
            markRed();

            List<CalendarDay> independent = new ArrayList<>();
            mcv.setWeekDayLabels(new String[]{"S","M","T","W","T","F","S"});
//        mcv.setTitleFormatter(titleFormatter);

            mcv.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Toast.makeText(getContext(),"Selected date "+simpleDateFormat.format(date.getDate()),Toast.LENGTH_LONG).show();
                }
            });

            btnadd = dialog.findViewById(R.id.btnadd);
            tv_weight = dialog.findViewById(R.id.tv_weight);

            btnadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString("weight", String.valueOf(cur_weight));
//                bundle.putString("weightChangeDate", String.valueOf(curDate));

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Weight", MODE_PRIVATE);

                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    System.out.println(sharedPreferences.getString("Weight", "0"));
                    myEdit.putString("weight", String.valueOf(cur_weight));
                    myEdit.putString("weightChangeDate", String.valueOf(curDate));

                    myEdit.apply();

//                getParentFragmentManager().setFragmentResult("weightData", bundle);

                    Navigation.findNavController(v).navigate(R.id.action_weightDateFragment_to_weightTrackerFragment);
                }
            });
            PickerLayoutManager pickerLayoutManager = new PickerLayoutManager(getContext(), PickerLayoutManager.HORIZONTAL, false);
            pickerLayoutManager.setChangeAlpha(true);
            pickerLayoutManager.setScaleDownBy(0.1f);
            pickerLayoutManager.setScaleDownDistance(0.1f);

            adapter = new PickerAdapter(getContext(), getData(200), rv);
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(rv);
            rv.setLayoutManager(pickerLayoutManager);
            rv.setAdapter(adapter);

            pickerLayoutManager.setOnScrollStopListener(new PickerLayoutManager.onScrollStopListener() {
                @Override
                public void selectedView(View view) {
//                Toast.makeText(MainActivity.this, ("Selected value : "+((TextView) view).getText().toString()), Toast.LENGTH_SHORT).show();
                    tv_weight.setText(((TextView) view).getText());
                    if (((TextView) view).getText() == "5"){
//                    ((TextView) view).setPadding(0,0,0,0);
//                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.interval,0,0);
                    }
                    else{
//                    ((TextView) view).setPadding(0,10,0,10);
//                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.lines,0,0);
                    }

                }
            });



            dialog.show();
//            Navigation.findNavController(v).navigate(R.id.action_weightTrackerFragment_to_weightDateFragment);
        });

        RecyclerView pastActivity = view.findViewById(R.id.past_activity);

        ArrayList<String> dates = new ArrayList<>();
        ArrayList<String> datas = new ArrayList<>();

        String url = String.format("%spastActivityWeight.php",DataFromDatabase.ipConfig);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("weight");
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    String data = object.getString("weight");
                    String date = object.getString("date");
                    dates.add(date);
                    datas.add(data);
                    System.out.println(datas.get(i));
                    System.out.println(dates.get(i));
                }
                AdapterForPastActivity ad = new AdapterForPastActivity(getContext(),dates,datas, Color.parseColor("#1FB688"));
                pastActivity.setLayoutManager(new LinearLayoutManager(getContext()));
                pastActivity.setAdapter(ad);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> {
//            Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Error",error.toString());
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                data.put("clientID",DataFromDatabase.clientuserID);
                return data;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);

        adddet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.fragment_bmi);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                RecyclerView rvw = dialog.findViewById(R.id.rvw);
                RecyclerView rvh = dialog.findViewById(R.id.rvh);

                PickerLayoutManager pickerLayoutManager = new PickerLayoutManager(getContext(), PickerLayoutManager.HORIZONTAL, false);
                pickerLayoutManager.setChangeAlpha(true);
                pickerLayoutManager.setScaleDownBy(0.1f);
                pickerLayoutManager.setScaleDownDistance(0.1f);

                PickerLayoutManager pickerLayoutManagerh = new PickerLayoutManager(getContext(), PickerLayoutManager.HORIZONTAL, false);
                pickerLayoutManagerh.setChangeAlpha(true);
                pickerLayoutManagerh.setScaleDownBy(0.1f);
                pickerLayoutManagerh.setScaleDownDistance(0.1f);

                PickerAdapterWeight adapter = new PickerAdapterWeight(getContext(), getData(201), rvw);
                SnapHelper snapHelper = new LinearSnapHelper();
                snapHelper.attachToRecyclerView(rvw);
                rvw.setLayoutManager(pickerLayoutManager);
                rvw.setAdapter(adapter);

                TextView tv_weightbmi = dialog.findViewById(R.id.textView94);

                pickerLayoutManager.setOnScrollStopListener(new PickerLayoutManager.onScrollStopListener() {
                    @Override
                    public void selectedView(View view) {

                        tv_weightbmi.setText(((TextView) view).getText());

//                Toast.makeText(MainActivity.this, ("Selected value : "+((TextView) view).getText().toString()), Toast.LENGTH_SHORT).show();
//                        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.interval_weight,0,0);
                        if (((TextView) view).getText() == "5"){
//                    ((TextView) view).setPadding(0,0,0,0);
//                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.interval,0,0);
                        }
                        else{
//                    ((TextView) view).setPadding(0,10,0,10);
//                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.lines,0,0);
                        }

                    }
                });

                PickerAdapterWeight adapterh = new PickerAdapterWeight(getContext(), getData(201), rvh);
                SnapHelper snapHelperh = new LinearSnapHelper();
                snapHelperh.attachToRecyclerView(rvh);
                rvh.setLayoutManager(pickerLayoutManagerh);
                rvh.setAdapter(adapter);

                TextView tv_heightbmi = dialog.findViewById(R.id.textView43);

                pickerLayoutManagerh.setOnScrollStopListener(new PickerLayoutManager.onScrollStopListener() {
                    @Override
                    public void selectedView(View view) {

                        tv_heightbmi.setText(((TextView) view).getText());

//                Toast.makeText(MainActivity.this, ("Selected value : "+((TextView) view).getText().toString()), Toast.LENGTH_SHORT).show();
//                        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.interval_weight,0,0);
                        if (((TextView) view).getText() == "5"){
//                    ((TextView) view).setPadding(0,0,0,0);
//                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.interval,0,0);
                        }
                        else{
//                    ((TextView) view).setPadding(0,10,0,10);
//                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.lines,0,0);
                        }

                    }
                });


//                Navigation.findNavController(v).navigate(R.id.action_weightTrackerFragment_to_bmiFragment);
            dialog.show();
            }
        });

        getParentFragmentManager().setFragmentResultListener("personalData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String uGender = result.getString("gender");
//                int uAge = Integer.parseInt(result.getString("age"));
                float uHeight = Float.parseFloat(result.getString("height"));
                int uWeight = Integer.parseInt(result.getString("weight"));

                float hsquare = (uHeight/100) * (uHeight/100);
                float bmi = uWeight/hsquare;
                textbmi.setText(String.format("%.2f",bmi));
                //userWeight = Integer.parseInt(uWeight);
            }
        });





        /*

        adddet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(v).navigate(R.id.action_weightTrackerFragment_to_weightDateFragment);

                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.bmidialog);

                dialog.show();
            }
        });

         */

        textbmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_weightTrackerFragment_to_bmiFragment);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_weightTrackerFragment_to_dashBoardFragment);
            }
        });
        return view;
    }

    void setEvent(ArrayList<String> dateList, int color) {

        ArrayList<LocalDate> localDateList = new ArrayList<>();

        for (String string : dateList) {
            LocalDate calendar = getLocalDate(string);
            if (calendar != null) {
                localDateList.add(calendar);
            }
        }

        ArrayList<CalendarDay> datesLeft = new ArrayList<>();
        ArrayList<CalendarDay> datesCenter = new ArrayList<>();
        ArrayList<CalendarDay> datesRight = new ArrayList<>();
        ArrayList<CalendarDay> datesIndependent = new ArrayList<>();

        for (LocalDate localDate : localDateList) {

            boolean right = false;
            boolean left = false;

            for (LocalDate day1 : localDateList) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (localDate.isEqual(day1.plusDays(1))) {
                        left = true;
                    }
                    if (day1.isEqual(localDate.plusDays(1))) {
                        right = true;
                    }
                }
            }

            if (left && right) {
                datesCenter.add(CalendarDay.from(localDate.getYear(),localDate.getMonthValue()-1,localDate.getDayOfMonth()));
            } else if (left) {
                datesLeft.add(CalendarDay.from(localDate.getYear(),localDate.getMonthValue()-1,localDate.getDayOfMonth()));
            } else if (right) {
                datesRight.add(CalendarDay.from(localDate.getYear(),localDate.getMonthValue()-1,localDate.getDayOfMonth()));
            } else {
                datesIndependent.add(CalendarDay.from(localDate.getYear(),localDate.getMonthValue()-1,localDate.getDayOfMonth()));
            }
        }

        if (color == pink) {
            setDecor(datesCenter, R.drawable.p_center);
            setDecor(datesLeft, R.drawable.p_left);
            setDecor(datesRight, R.drawable.p_right);
            setDecor(datesIndependent, R.drawable.p_independent);
        } else if (color == green){
            setDecor(datesCenter, R.drawable.g_center);
            setDecor(datesLeft, R.drawable.g_left);
            setDecor(datesRight, R.drawable.g_right);
            setDecor(datesIndependent, R.drawable.g_independent);
        }
    }

    void setDecor(List<CalendarDay> calendarDayList, int drawable) {
        mcv.addDecorators(new EventDecorator(getActivity()
                , drawable
                , calendarDayList));
    }

    LocalDate getLocalDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        try {
            Date input = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(input);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return LocalDate.of(cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.DAY_OF_MONTH));
            }
        } catch (NullPointerException e) {
            return null;
        } catch (ParseException e) {
            return null;
        }
        return null;
    }

    void markRed(){

        new Thread(() -> {
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
                        setEvent(dates, pink);
                        mcv.invalidateDecorators();
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
        });

    }

    void markGreen(){
        String urlMark = String.format("%sweightDate.php",DataFromDatabase.ipConfig);

        StringRequest stringRequestCal = new StringRequest(Request.Method.POST,urlMark,response -> {

            try {
                JSONObject object = new JSONObject(response);
                JSONArray weight = object.getJSONArray("weight");
                ArrayList<String> dates = new ArrayList<>();
                for (int i = 0;i<weight.length();i++) {
                    JSONObject date = weight.getJSONObject(i);
                    String dateStr = date.getString("date");
                    dates.add(dateStr);
                    String[] array = dateStr.split("-");
                    System.out.println(dateStr);
                    setEvent(dates, green);
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
                data.put("userID", "Azarudeen");
                return data;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequestCal);
    }
    public List<String> getData(int count) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            data.add(String.valueOf(i));
        }
        return data;
    }

}
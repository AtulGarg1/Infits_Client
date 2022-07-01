package com.example.infits;

import static android.content.Context.MODE_PRIVATE;


import org.threeten.bp.LocalDate;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.tistory.dwfox.dwrulerviewlibrary.view.ScrollingValuePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    //    MCalendarView calendarView;
    String curDate;

    MaterialCalendarView mcv;

    final ArrayList<String> pinkDateList = new ArrayList<>();
    final ArrayList<String> grayDateList = new ArrayList<>();

    final String DATE_FORMAT = "yyyy-MM-dd";

    int pink = 0;
    int gray = 1;

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

        View view = inflater.inflate(R.layout.fragment_weight_date, container, false);

        mcv = view.findViewById(R.id.calendarView);

        Calendar cal = Calendar.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mcv.state().edit()
                    .setMinimumDate(CalendarDay.from(2018, 4, 3))
                    .setMaximumDate(CalendarDay.from(2025, 5, 12))
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();
            mcv.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        }
        rulerValuePicker = view.findViewById(R.id.rulerValuePicker);
        btnadd = view.findViewById(R.id.btnadd);
        tv_weight = view.findViewById(R.id.tv_weight);
//        tv_weight2 = view.findViewById(R.id.tv_weight2);
//        calendarView = view.findViewById(R.id.calendarView);
//
//        ScrollingValuePicker myScrollingValuePicker;
//        myScrollingValuePicker = (ScrollingValuePicker) view.findViewById(R.id.scrolla);
//        myScrollingValuePicker.setViewMultipleSize(5);
//        myScrollingValuePicker.setMaxValue(0,100);
//        myScrollingValuePicker.setValueTypeMultiple(5);
//        myScrollingValuePicker.getScrollView().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    myScrollingValuePicker.getScrollView().startScrollerTask();
//                }
//                return false;
//            }
//        });

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
                    setEvent(dates, gray);
//                    calendarView.markDate(
//                            new DateData(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2])).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.GREEN)));
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
//        rulerValuePicker.setValuePickerListener(new RulerValuePickerListener() {
//            @Override
//            public void onValueChange(int selectedValue) {
//
//                tv_weight.setText(selectedValue+" KG");
//                cur_weight = selectedValue;
//
//            }
//
//            @Override
//            public void onIntermediateValueChange(int selectedValue) {
//
//            }
//        });
//
//
//        final DateData[] olddate = {new DateData(2022, 06, 11)};
//
//        calendarView.setOnDateClickListener(new OnDateClickListener() {
//            @Override
//            public void onDateClick(View view, DateData date) {
//                Toast.makeText(getActivity(), String.format("%d-%d", date.getMonth(), date.getDay()), Toast.LENGTH_SHORT).show();
//
//                calendarView.unMarkDate(olddate[0].getYear(),olddate[0].getMonth(),olddate[0].getDay());
//
//                calendarView.markDate(date.getYear(), date.getMonth(), date.getDay()).setMarkedStyle(MarkStyle.BACKGROUND, Color.BLUE);
//
//                olddate[0] = new DateData(date.getYear(),date.getMonth(),date.getDay());
//            }
//        });

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
        return view;
    }
    void setEvent(ArrayList<String> dateList, int color) {

        ArrayList<LocalDate> localDateList = new ArrayList<>();

//        "2019-01-01",
//                "2019-01-03", "2019-01-04", "2019-01-05", "2019-01-06");
//        final List<String> grayDateList = Arrays.asList(
//                "2019-01-09", "2019-01-10", "2019-01-11",
//                "2019-01-24", "2019-01-25", "2019-01-26", "2019-01-27", "2019-01-28", "2019-01-29");


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
        } else {
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
}
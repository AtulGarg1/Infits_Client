package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeightTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeightTrackerFragment extends Fragment {

    CardView date_click;

    ImageButton adddet;
    ImageView imgback;
    TextView textbmi, curWeight,date_view;
    public int userWeight;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeightTrackerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeightTrackerFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
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
            Navigation.findNavController(v).navigate(R.id.action_weightTrackerFragment_to_weightDateFragment);
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
                data.put("","");
                return data;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
        adddet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_weightTrackerFragment_to_bmiFragment);

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
}
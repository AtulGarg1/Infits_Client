package com.example.infits;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WaterTrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WaterTrackerFragment extends Fragment {

    ImageButton addliq, imgback;
    TextView wgoalperc, wgoal3, textViewsleep;
    ImageView waterGoal;
    String liqType, liqAmt;
    float goalWater;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WaterTrackerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WaterTrackerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WaterTrackerFragment newInstance(String param1, String param2) {
        WaterTrackerFragment fragment = new WaterTrackerFragment();
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
        View view = inflater.inflate(R.layout.fragment_water_tracker, container, false);

        addliq = view.findViewById(R.id.addliq);
        imgback = view.findViewById(R.id.imgback);
        wgoalperc = view.findViewById(R.id.wgoalperc);
        wgoal3 = view.findViewById(R.id.wgoal3);

        textViewsleep = view.findViewById(R.id.textviewsleep);

        waterGoal = view.findViewById(R.id.waterGoal);

        addliq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*

                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.fragment_add_liquid);

                dialog.show();

                 */

                Navigation.findNavController(v).navigate(R.id.action_waterTrackerFragment_to_addLiquidFragment);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_waterTrackerFragment_to_dashBoardFragment);
            }
        });


        getParentFragmentManager().setFragmentResultListener("liquidData", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                liqType = result.getString("liquidType");
                liqAmt = result.getString("liquidAmt");
            }
        });

        /*
        public void onClick(DialogInterface dialog,
                                int id) {
                            Dialog f = (Dialog) dialog;
                            //This is the input I can't get text from
                            EditText inputTemp = (EditText) f.findViewById(R.id.search_input_text);
                            query = inputTemp.getText().toString();
                           ...
                        }
         */

        waterGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.watergoaldialog);

                final EditText goal = dialog.findViewById(R.id.goal);
                Button button = dialog.findViewById(R.id.button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String wGoal = goal.getText().toString();

                        //textViewsleep.setText("Goal: "+wGoal);


                        float liqAmount = Float.parseFloat(liqAmt);
                        //textViewsleep.setText("Goal: "+wGoal);


                        float res = (liqAmount/Float.parseFloat(wGoal)) *100;

                        wgoalperc.setText((int)res+ " %");

                        goalWater = Float.parseFloat(wGoal);

                        dialog.dismiss();

                    }
                });

                dialog.show();

                //Navigation.findNavController(v).navigate(R.id.action_waterTrackerFragment_to_addLiquidFragment);
            }
        });

        //textViewsleep.setText("Goal: "+goalWater);


        return view;
    }
}
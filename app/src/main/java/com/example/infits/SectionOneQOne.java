package com.example.infits;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SectionOneQOne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SectionOneQOne extends Fragment {

    Button nextbtn;
    TextView backbtn, emailtv;
    EditText eTextEmail;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SectionOneQOne() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SectionOneQOne.
     */
    // TODO: Rename and change types and number of parameters
    public static SectionOneQOne newInstance(String param1, String param2) {
        SectionOneQOne fragment = new SectionOneQOne();
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
        View view = inflater.inflate(R.layout.fragment_section_one_q_one, container, false);

        nextbtn = view.findViewById(R.id.nextbtn);
        backbtn = view.findViewById(R.id.backbtn);
        eTextEmail = view.findViewById(R.id.eTextEmail);

        emailtv = view.findViewById(R.id.textView80);


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_email = eTextEmail.getText().toString();
                //Toast.makeText(getContext(),user_email, Toast.LENGTH_SHORT).show();

                DataSectionOne.email = user_email;
                DataSectionOne.s1q1 = emailtv.getText().toString();
                if(user_email.equals("")|| user_email.equals(" "))
                    Toast.makeText(getContext(),"Add your email",Toast.LENGTH_SHORT).show();
                else{
                    Consultation.section1+=1;
                    Navigation.findNavController(v).navigate(R.id.action_sectionOneQOne_to_sectionOneQTwo);
                 }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Consultation.section1>0)
                    Consultation.section1-=1;
                Navigation.findNavController(v).navigate(R.id.action_sectionOneQOne_to_consultationFragment);
            }
        });

        return view;
    }
}
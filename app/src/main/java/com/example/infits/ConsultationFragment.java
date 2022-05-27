package com.example.infits;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultationFragment extends Fragment {

    CardView section1, section2, section3, section4, section5, section6;

    Button connectDoc;

    TextView textView58;

    Button generatePDFbtn;


    int pageHeight = 1120;
    int pagewidth = 792;

    Bitmap bmp, scaledbmp;

    private static final int PERMISSION_REQUEST_CODE = 200;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConsultationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsultationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultationFragment newInstance(String param1, String param2) {
        ConsultationFragment fragment = new ConsultationFragment();
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
        View view = inflater.inflate(R.layout.fragment_consultation, container, false);

        section1 = view.findViewById(R.id.section1);
        section2 = view.findViewById(R.id.section2);
        section3 = view.findViewById(R.id.section3);
        section4 = view.findViewById(R.id.section4);
        section5 = view.findViewById(R.id.section5);
        section6 = view.findViewById(R.id.section6);

        connectDoc = view.findViewById(R.id.connectDoc);

        textView58 = view.findViewById(R.id.textView58);

        section1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_consultationFragment_to_sectionOneQOne);
            }
        });

        section2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_consultationFragment_to_section2Q1);
            }
        });

        section3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_consultationFragment_to_section3Q1);
            }
        });

        section4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_consultationFragment_to_section4Q1);
            }
        });

        section5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_consultationFragment_to_section5Q1);
            }
        });

        section6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_consultationFragment_to_section6Q1);
            }
        });

        connectDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String section1data = DataSectionOne.s1q1 +":\n" + DataSectionOne.email +"\n\n" + DataSectionOne.s1q2 +":\n" + DataSectionOne.name +"\n\n" + DataSectionOne.s1q3 +":\n" + DataSectionOne.age +"\n\n"
                        + DataSectionOne.s1q4 +":\n" + DataSectionOne.gender +"\n\n" + DataSectionOne.s1q5 +":\n" + DataSectionOne.hometown +"\n\n" + DataSectionOne.s1q6 +":\n" + DataSectionOne.employment +"\n\n" + DataSectionOne.s1q7 +":\n" + DataSectionOne.duration +"\n\n"
                        + DataSectionOne.s1q8 +":\n" + DataSectionOne.shift +"\n\n";

                String section2part1 = DataSectionTwo.s2q1 +":\n" + DataSectionTwo.height +"\n\n" + DataSectionTwo.s2q2 +":\n" + DataSectionTwo.weight +"\n\n"
                        + DataSectionTwo.s2q3 +":\n" + DataSectionTwo.usualWeight +"\n\n";

                String section2part2 = DataSectionTwo.s2q6 +":\n" + DataSectionTwo.ongoingMed +"\n\n" + DataSectionTwo.s2q7 +":\n" + DataSectionTwo.medication +"\n\n";

                StringBuffer sb1 = new StringBuffer();
                StringBuffer sb2 = new StringBuffer();

                for(String i : DataSectionTwo.diagnosed) {
                    sb1.append(i);
                    sb1.append("\n");
                }
                String diagnosedData = sb1.toString();

                for(String i : DataSectionTwo.familyHistory) {
                    sb1.append(i);
                    sb1.append("\n");
                }
                String famhistoryData = sb2.toString();


                String section3data = DataSectionThree.s3q1 +":\n" + DataSectionThree.gastritis +"\n\n" + DataSectionThree.s3q2 +":\n" + DataSectionThree.constipation +"\n\n"
                        + DataSectionThree.s3q3 +":\n" + DataSectionThree.diarrhoea +"\n\n" + DataSectionThree.s3q4 +":\n" + DataSectionThree.nausea +"\n\n" + DataSectionThree.s3q5 +":\n" + DataSectionThree.vomiting +"\n\n"
                        + DataSectionThree.s3q6 +":\n" + DataSectionThree.appetite +"\n\n" + DataSectionThree.s3q7 +":\n" + DataSectionThree.hairfall +"\n\n" + DataSectionThree.s3q8 +":\n" + DataSectionThree.bloating +"\n\n"
                        + DataSectionThree.s3q9 +":\n" + DataSectionThree.micturition +"\n\n" + DataSectionThree.s3q10 +":\n" + DataSectionThree.headache +"\n\n" + DataSectionThree.s3q11 +":\n" + DataSectionThree.stomachache +"\n\n";

                String section4data = DataSectionFour.s4q1 +":\n" + DataSectionFour.walking +"\n\n" + DataSectionFour.s4q2 +":\n" + DataSectionFour.running +"\n\n"
                        + DataSectionFour.s4q3 +":\n" + DataSectionFour.yoga +"\n\n" + DataSectionFour.s4q4 +":\n" + DataSectionFour.strength +"\n\n" + DataSectionFour.s4q5 +":\n" + DataSectionFour.cardio +"\n\n"
                        + DataSectionFour.s4q6 +":\n" + DataSectionFour.skipping +"\n\n" + DataSectionFour.s4q7 +":\n" + DataSectionFour.activity_duration +"\n\n";

                String section5data = DataSectionFive.s5q1 +":\n" + DataSectionFive.preference +"\n\n" + DataSectionFive.s5q2 +":\n" + DataSectionFive.mealno +"\n\n"
                        + DataSectionFive.s5q3 +":\n" + DataSectionFive.snack +"\n\n" + DataSectionFive.s5q4 +":\n" + DataSectionFive.water +"\n\n" + DataSectionFive.s5q5 +":\n" + DataSectionFive.allergies +"\n\n"
                        + DataSectionFive.s5q6 +":\n" + DataSectionFive.food_allergy +"\n\n" + DataSectionFive.s5q7 +":\n" + DataSectionFive.stress_eat +"\n\n" + DataSectionFive.s5q8 +":\n" + DataSectionFive.stress_food +"\n\n"
                        + DataSectionFive.s5q9 +":\n" + DataSectionFive.daily_food +"\n\n" + DataSectionFive.s5q10 +":\n" + DataSectionFive.sleep_duration +"\n\n" + DataSectionFive.s5q11 +":\n" + DataSectionFive.smoking +"\n\n"
                        + DataSectionFive.s5q12 +":\n" + DataSectionFive.alcohol +"\n\n" + DataSectionFive.s5q13 +":\n" + DataSectionFive.daily_routine +"\n\n";

                String section6data = DataSectionSix.s6q1 +":\n" + DataSectionSix.cereals +"\n\n" + DataSectionSix.s6q2 +":\n" + DataSectionSix.pulses +"\n\n" + DataSectionSix.s6q3 +":\n" + DataSectionSix.milk +"\n\n"
                        + DataSectionSix.s6q4 +":\n" + DataSectionSix.milkprod +"\n\n" + DataSectionSix.s6q5 +":\n" + DataSectionSix.roots +"\n\n" + DataSectionSix.s6q6 +":\n" + DataSectionSix.leafy +"\n\n" + DataSectionSix.s6q7 +":\n" + DataSectionSix.veg_other +"\n\n"
                        + DataSectionSix.s6q8 +":\n" + DataSectionSix.fruits +"\n\n" + DataSectionSix.s6q9 +":\n" + DataSectionSix.fats +"\n\n" + DataSectionSix.s6q10 +":\n" + DataSectionSix.dry_fruits +"\n\n" + DataSectionSix.s6q11 +":\n" + DataSectionSix.sugar +"\n\n"
                        + DataSectionSix.s6q12 +":\n" + DataSectionSix.fastfood +"\n\n" + DataSectionSix.s6q13 +":\n" + DataSectionSix.sweets +"\n\n" + DataSectionSix.s6q14 +":\n" + DataSectionSix.tea +"\n\n";


                String allClientData = section1data + section2part1 + diagnosedData + section2part2 + famhistoryData
                        + section3data + section4data + section5data + section6data;


                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

                generatePDF(allClientData);

            }
        });



        return view;
    }

    private void generatePDF(String clientData) {

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();

        int x = 10, y=25;

        for (String line : clientData.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y+=myPaint.descent()-myPaint.ascent();
        }

        myPdfDocument.finishPage(myPage);

        //String pdffilename = "ClientDetails";

        //String myFilePath = Environment.getExternalStorageDirectory().getPath() + '\\' + pdffilename + ".pdf";
        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/ClientDetails.pdf";
        File myFile = new File(myFilePath);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
            Toast.makeText(getContext(),"PDF Created: " + myFilePath, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(),"ERROR", Toast.LENGTH_SHORT).show();
        }

        myPdfDocument.close();
    }
}
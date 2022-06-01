package com.example.infits;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.Nullable;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultationFragment extends Fragment {

    CardView section1, section2, section3, section4, section5, section6;

    Button connectDoc;

    TextView textView58;

    private static final int STORAGE_CODE = 1000;

    private String filePath = Environment.getExternalStorageDirectory().getPath() + "/Download/" + DataFromDatabase.clientuserID + "_healthform.pdf";
    private File file = new File(filePath);

    public ArrayList<String> questions;
    public ArrayList<String> answers;

    String diagnosedAnswer, famhistoryAnswer;


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

                addQuestions();
                addAnswers();


                String section1data = DataSectionOne.s1q1 +":\n" + DataSectionOne.email +"\n\n" + DataSectionOne.s1q2 +":\n" + DataSectionOne.name +"\n\n" + DataSectionOne.s1q3 +":\n" + DataSectionOne.age +"\n\n"
                        + DataSectionOne.s1q4 +":\n" + DataSectionOne.hometown +"\n\n" + DataSectionOne.s1q5 +":\n" + DataSectionOne.gender +"\n\n" + DataSectionOne.s1q6 +":\n" + DataSectionOne.employment +"\n\n" + DataSectionOne.s1q7 +":\n" + DataSectionOne.duration +"\n\n"
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
                diagnosedAnswer = sb1.toString();
                String diagnosedData = DataSectionTwo.s2q5 + "\n" + sb1.toString();

                for(String i : DataSectionTwo.familyHistory) {
                    sb2.append(i);
                    sb2.append("\n");
                }
                famhistoryAnswer = sb2.toString();
                String famhistoryData = DataSectionTwo.s2q8 + "\n" + sb2.toString();


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

                String section2data = section2part1 + "\n\n" + diagnosedData + section2part2 + famhistoryData + "\n\n";

                String allClientData = section1data + section2part1 + diagnosedData + section2part2 + famhistoryData
                        + section3data + section4data + section5data + section6data;


                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

                generatePDF(allClientData, section1data, section2data, section3data, section4data, section5data, section6data);

                String url="http://192.168.1.14/infits/clientconsultation.php";

                //String tablename = DataFromDatabase.clientuserID;
                String tablename = "clientidconsul";

                StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
                    System.out.println(response);
                    if (response.equals("success")){
                        Toast.makeText(getActivity(), "Details updated", Toast.LENGTH_SHORT).show();
                        Intent id = new Intent(getActivity(), DashBoardMain.class);
                        startActivity(id);
                    }
                    else{
                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                    }
                },error -> {
                    Toast.makeText(getActivity(),error.toString().trim(),Toast.LENGTH_SHORT).show();}){
                    /*
                    @Nullable
                    @Override
                    protected Map<String, ArrayList<>> getParams() throws AuthFailureError {
                        Map<String,ArrayList<>> data = new HashMap<>();

                        data.put("question",questions);
                        data.put("answer",answers);
                        //data.put("tablename",tablename);

                        return data;
                    }

                     */
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                requestQueue.add(stringRequest);


            }
        });


        return view;
    }

    private void generatePDF(String clientData, String section1data, String section2data, String section3data, String section4data, String section5data,String section6data) {

        //String extstoragedir = Environment.getExternalStorageDirectory().toString();
        //String extstoragedir =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + "sample1" + ".pdf";


        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(450, 2300, 1 ).create();
        PdfDocument.Page page1 = pdfDocument.startPage(pageInfo);

        Paint paint = new Paint();

        int x=10, y=25;

        for(String line: clientData.split("\n")) {
            page1.getCanvas().drawText(line,x,y,paint);

            y+=paint.descent()-paint.ascent();
        }

        pdfDocument.finishPage(page1);

        /*

        PdfDocument.PageInfo pageInfo2 = new PdfDocument.PageInfo.Builder(400, 700, 2 ).create();
        PdfDocument.Page page2 = pdfDocument.startPage(pageInfo2);

        for(String line: section2data.split("\n")) {
            page2.getCanvas().drawText(line,x,y,paint);

            y+=paint.descent()-paint.ascent();
        }

        pdfDocument.finishPage(page2);

        PdfDocument.PageInfo pageInfo3 = new PdfDocument.PageInfo.Builder(400, 800, 3 ).create();
        PdfDocument.Page page3 = pdfDocument.startPage(pageInfo3);

        for(String line: section3data.split("\n")) {
            page3.getCanvas().drawText(line,x,y,paint);

            y+=paint.descent()-paint.ascent();
        }

        pdfDocument.finishPage(page3);

        PdfDocument.PageInfo pageInfo4 = new PdfDocument.PageInfo.Builder(400, 800, 4 ).create();
        PdfDocument.Page page4 = pdfDocument.startPage(pageInfo4);

        for(String line: section4data.split("\n")) {
            page4.getCanvas().drawText(line,x,y,paint);

            y+=paint.descent()-paint.ascent();
        }

        pdfDocument.finishPage(page4);

        PdfDocument.PageInfo pageInfo5 = new PdfDocument.PageInfo.Builder(400, 900, 5 ).create();
        PdfDocument.Page page5 = pdfDocument.startPage(pageInfo5);

        for(String line: section5data.split("\n")) {
            page5.getCanvas().drawText(line,x,y,paint);

            y+=paint.descent()-paint.ascent();
        }

        pdfDocument.finishPage(page5);

        PdfDocument.PageInfo pageInfo6 = new PdfDocument.PageInfo.Builder(400, 900, 6 ).create();
        PdfDocument.Page page6 = pdfDocument.startPage(pageInfo6);

        for(String line: section6data.split("\n")) {
            page6.getCanvas().drawText(line,x,y,paint);

            y+=paint.descent()-paint.ascent();
        }

        pdfDocument.finishPage(page6);

         */

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(getContext(), "PDF created", Toast.LENGTH_SHORT).show();

        }
        catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }


        pdfDocument.close();


    }

    public void addQuestions() {

        questions.add(DataSectionOne.s1q1);
        questions.add(DataSectionOne.s1q2);
        questions.add(DataSectionOne.s1q3);
        questions.add(DataSectionOne.s1q4);
        questions.add(DataSectionOne.s1q5);
        questions.add(DataSectionOne.s1q6);
        questions.add(DataSectionOne.s1q7);
        questions.add(DataSectionOne.s1q8);

        questions.add(DataSectionTwo.s2q1);
        questions.add(DataSectionTwo.s2q2);
        questions.add(DataSectionTwo.s2q3);
        questions.add(DataSectionTwo.s2q4);
        questions.add(DataSectionTwo.s2q5);
        questions.add(DataSectionTwo.s2q6);
        questions.add(DataSectionTwo.s2q7);
        questions.add(DataSectionTwo.s2q8);

        questions.add(DataSectionThree.s3q1);
        questions.add(DataSectionThree.s3q2);
        questions.add(DataSectionThree.s3q3);
        questions.add(DataSectionThree.s3q4);
        questions.add(DataSectionThree.s3q5);
        questions.add(DataSectionThree.s3q6);
        questions.add(DataSectionThree.s3q7);
        questions.add(DataSectionThree.s3q8);
        questions.add(DataSectionThree.s3q9);
        questions.add(DataSectionThree.s3q10);
        questions.add(DataSectionThree.s3q11);

        questions.add(DataSectionFour.s4q1);
        questions.add(DataSectionFour.s4q2);
        questions.add(DataSectionFour.s4q3);
        questions.add(DataSectionFour.s4q4);
        questions.add(DataSectionFour.s4q5);
        questions.add(DataSectionFour.s4q6);
        questions.add(DataSectionFour.s4q7);

        questions.add(DataSectionFive.s5q1);
        questions.add(DataSectionFive.s5q2);
        questions.add(DataSectionFive.s5q3);
        questions.add(DataSectionFive.s5q4);
        questions.add(DataSectionFive.s5q5);
        questions.add(DataSectionFive.s5q6);
        questions.add(DataSectionFive.s5q7);
        questions.add(DataSectionFive.s5q8);
        questions.add(DataSectionFive.s5q9);
        questions.add(DataSectionFive.s5q10);
        questions.add(DataSectionFive.s5q11);
        questions.add(DataSectionFive.s5q12);
        questions.add(DataSectionFive.s5q13);

        questions.add(DataSectionSix.s6q1);
        questions.add(DataSectionSix.s6q2);
        questions.add(DataSectionSix.s6q3);
        questions.add(DataSectionSix.s6q4);
        questions.add(DataSectionSix.s6q5);
        questions.add(DataSectionSix.s6q6);
        questions.add(DataSectionSix.s6q7);
        questions.add(DataSectionSix.s6q8);
        questions.add(DataSectionSix.s6q9);
        questions.add(DataSectionSix.s6q10);
        questions.add(DataSectionSix.s6q11);
        questions.add(DataSectionSix.s6q12);
        questions.add(DataSectionSix.s6q13);
        questions.add(DataSectionSix.s6q14);


    }

    public void addAnswers() {

        //email,name,age,gender,hometown,employment,duration,shift
        answers.add(DataSectionOne.email);
        answers.add(DataSectionOne.name);
        answers.add(DataSectionOne.age);
        answers.add(DataSectionOne.gender);
        answers.add(DataSectionOne.hometown);
        answers.add(DataSectionOne.employment);
        answers.add(DataSectionOne.duration);
        answers.add(DataSectionOne.shift);

        //height,weight,usualWeight,ongoingMed,medication;
        answers.add(DataSectionTwo.height);
        answers.add(DataSectionTwo.weight);
        answers.add(DataSectionTwo.usualWeight);
        answers.add("Image");
        answers.add(diagnosedAnswer);
        answers.add(DataSectionTwo.ongoingMed);
        answers.add(DataSectionTwo.medication);
        answers.add(famhistoryAnswer);

        //gastritis,constipation,diarrhoea,nausea,vomiting,appetite,hairfall,bloating,micturition,headache,stomachache;
        answers.add(DataSectionThree.gastritis);
        answers.add(DataSectionThree.constipation);
        answers.add(DataSectionThree.diarrhoea);
        answers.add(DataSectionThree.nausea);
        answers.add(DataSectionThree.vomiting);
        answers.add(DataSectionThree.appetite);
        answers.add(DataSectionThree.hairfall);
        answers.add(DataSectionThree.bloating);
        answers.add(DataSectionThree.micturition);
        answers.add(DataSectionThree.headache);
        answers.add(DataSectionThree.stomachache);

        //walking,running,yoga,strength,cardio,skipping,activity_duration;
        answers.add(DataSectionFour.walking);
        answers.add(DataSectionFour.running);
        answers.add(DataSectionFour.yoga);
        answers.add(DataSectionFour.strength);
        answers.add(DataSectionFour.cardio);
        answers.add(DataSectionFour.skipping);
        answers.add(DataSectionFour.activity_duration);

        //preference,mealno,snack,water,allergies,food_allergy,stress_eat,stress_food,daily_food,
        //            sleep_duration,smoking,alcohol,daily_routine;
        answers.add(DataSectionFive.preference);
        answers.add(DataSectionFive.mealno);
        answers.add(DataSectionFive.snack);
        answers.add(DataSectionFive.water);
        answers.add(DataSectionFive.allergies);
        answers.add(DataSectionFive.food_allergy);
        answers.add(DataSectionFive.stress_eat);
        answers.add(DataSectionFive.stress_food);
        answers.add(DataSectionFive.daily_food);
        answers.add(DataSectionFive.sleep_duration);
        answers.add(DataSectionFive.smoking);
        answers.add(DataSectionFive.alcohol);
        answers.add(DataSectionFive.daily_routine);

        //cereals,pulses,milk,milkprod,roots,leafy,veg_other,fruits,fats,dry_fruits,sugar
        // fastfood,sweets,tea;
        answers.add(DataSectionSix.cereals);
        answers.add(DataSectionSix.pulses);
        answers.add(DataSectionSix.milk);
        answers.add(DataSectionSix.milkprod);
        answers.add(DataSectionSix.roots);
        answers.add(DataSectionSix.leafy);
        answers.add(DataSectionSix.veg_other);
        answers.add(DataSectionSix.fruits);
        answers.add(DataSectionSix.fats);
        answers.add(DataSectionSix.dry_fruits);
        answers.add(DataSectionSix.sugar);
        answers.add(DataSectionSix.fastfood);
        answers.add(DataSectionSix.sweets);
        answers.add(DataSectionSix.tea);


    }

}
package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Consultation extends AppCompatActivity {

    public static int section1=0;
    public static int section2=0;
    public static int section3=0;
    public static int section4=0;
    public static int section5=0;
    public static int section6=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);
        ProgressBar p1=findViewById(R.id.sectionprogress1);
        ProgressBar p2=findViewById(R.id.sectionprogress2);
        ProgressBar p3=findViewById(R.id.sectionprogress3);
        ProgressBar p4=findViewById(R.id.sectionprogress4);
        ProgressBar p5=findViewById(R.id.sectionprogress5);
        ProgressBar p6=findViewById(R.id.sectionprogress6);

        TextView t1=findViewById(R.id.section1perc);
        TextView t2=findViewById(R.id.section2perc);
        TextView t3=findViewById(R.id.section3perc);
        TextView t4=findViewById(R.id.section4perc);
        TextView t5=findViewById(R.id.section5perc);
        TextView t6=findViewById(R.id.section6perc);

        p1.setProgress(section1/8);
        t1.setText(String.valueOf(section1/8*100)+"%");
        p2.setProgress(section2/8);
        t2.setText(String.valueOf(section2/8*100)+"%");
        p3.setProgress(section3/11);
        t3.setText(String.valueOf(section3/11*100)+"%");
        p4.setProgress(section4/7);
        t4.setText(String.valueOf(section4/7*100)+"%");
        p5.setProgress(section5/13);
        t5.setText(String.valueOf(section5/13*100)+"%");
        p6.setProgress(section6/14);
        t6.setText(String.valueOf(section6/14*100)+"%");
    }
}
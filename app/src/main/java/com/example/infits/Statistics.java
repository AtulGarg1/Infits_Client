package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

public class Statistics extends AppCompatActivity {

    ImageButton steps_btn, heart_btn, water_btn, sleep_btn, weight_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        steps_btn = findViewById(R.id.steps_btn);
        heart_btn = findViewById(R.id.heart_btn);
        water_btn = findViewById(R.id.water_btn);
        sleep_btn = findViewById(R.id.sleep_btn);
        weight_btn = findViewById(R.id.weight_btn);



        steps_btn.setOnClickListener(v ->{
            steps_btn.setBackgroundResource(R.drawable.step_stat_selected);
            heart_btn.setBackgroundResource(R.drawable.heart_stat_unselected);
            water_btn.setBackgroundResource(R.drawable.water_stat_unselected);
            sleep_btn.setBackgroundResource(R.drawable.sleep_stat_unselected);
            weight_btn.setBackgroundResource(R.drawable.weight_stat_unselected);
            // if()
            // getSupportFragmentManager().beginTransaction().replace(R.id.client_details_sec,new BlankDietChart()).commit();
            //else
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2,new StepsFragment()).commit();
        });

        heart_btn.setOnClickListener(v ->{
            steps_btn.setBackgroundResource(R.drawable.step_stat_unselected);
            heart_btn.setBackgroundResource(R.drawable.heart_selected);
            water_btn.setBackgroundResource(R.drawable.water_stat_unselected);
            sleep_btn.setBackgroundResource(R.drawable.sleep_stat_unselected);
            weight_btn.setBackgroundResource(R.drawable.weight_stat_unselected);
            // if()
            // getSupportFragmentManager().beginTransaction().replace(R.id.client_details_sec,new BlankDietChart()).commit();
            //else
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2,new HeartFragment()).commit();
        });

        water_btn.setOnClickListener(v ->{
            steps_btn.setBackgroundResource(R.drawable.step_stat_unselected);
            heart_btn.setBackgroundResource(R.drawable.heart_stat_unselected);
            water_btn.setBackgroundResource(R.drawable.water_selected);
            sleep_btn.setBackgroundResource(R.drawable.sleep_stat_unselected);
            weight_btn.setBackgroundResource(R.drawable.weight_stat_unselected);
            // if()
            // getSupportFragmentManager().beginTransaction().replace(R.id.client_details_sec,new BlankDietChart()).commit();
            //else
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2,new WaterFragment()).commit();
        });

        sleep_btn.setOnClickListener(v ->{
            steps_btn.setBackgroundResource(R.drawable.step_stat_unselected);
            heart_btn.setBackgroundResource(R.drawable.heart_stat_unselected);
            water_btn.setBackgroundResource(R.drawable.water_stat_unselected);
            sleep_btn.setBackgroundResource(R.drawable.sleep_selected);
            weight_btn.setBackgroundResource(R.drawable.weight_stat_unselected);
            // if()
            // getSupportFragmentManager().beginTransaction().replace(R.id.client_details_sec,new BlankDietChart()).commit();
            //else
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2,new SleepFragment()).commit();
        });

        weight_btn.setOnClickListener(v ->{
            steps_btn.setBackgroundResource(R.drawable.step_stat_unselected);
            heart_btn.setBackgroundResource(R.drawable.heart_stat_unselected);
            water_btn.setBackgroundResource(R.drawable.water_stat_unselected);
            sleep_btn.setBackgroundResource(R.drawable.sleep_stat_unselected);
            weight_btn.setBackgroundResource(R.drawable.weight_selected);
            // if()
            // getSupportFragmentManager().beginTransaction().replace(R.id.client_details_sec,new BlankDietChart()).commit();
            //else
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2,new WeightFragment()).commit();
        });


    }
}
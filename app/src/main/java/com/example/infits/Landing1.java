package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class Landing1 extends AppCompatActivity {

    ImageButton b;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing1);

        List<Fragment> list=new ArrayList<>();
        list.add(new Landing1fragment());
        list.add(new Landing2Fragment());
        list.add(new Landing3Fragment());

        pager =findViewById(R.id.pager);
        pagerAdapter=new SliderPageAdapter(getSupportFragmentManager(),list);
        pager.setAdapter(pagerAdapter);

//        b=(ImageButton) findViewById(R.id.circleButton);
//
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Landing1.this, Landing2.class);
//                startActivity(intent);
//            }
//        });
    }


}

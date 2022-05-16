package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ResetPassword extends AppCompatActivity {

    ImageButton b1;
    TextView logtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        b1=(ImageButton) findViewById(R.id.imageButton3);
        logtext = (TextView) findViewById(R.id.logtext);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResetPassword.this, Login.class);
                startActivity(i);
            }
        });

        logtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent il = new Intent(ResetPassword.this, Login.class);
                startActivity(il);
            }
        });
    }
}
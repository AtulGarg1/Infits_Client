package com.example.infits;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class UserDataActivity extends AppCompatActivity {

    EditText weight,height,fullname,userage;
    TextView dob;
    Button createacc;
     Calendar mcalendar;
    private int day,month,year;

    String url = String.format("%sregister_client.php",DataFromDatabase.ipConfig);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        height = findViewById(R.id.userheight);
        weight = findViewById(R.id.userweight);
        dob = findViewById(R.id.DOB);
        fullname = findViewById(R.id.fullname);
        createacc = findViewById(R.id.createaccount);
        userage = findViewById(R.id.age);
//        day=mcalendar.get(Calendar.DAY_OF_MONTH);
//        year=mcalendar.get(Calendar.YEAR);
//        month=mcalendar.get(Calendar.MONTH);


        Signup obj = new Signup();
//        String uname=obj.userName.getText().toString();
//        String mail=obj.emailID.getText().toString();
//        String phone=obj.phoneNo.getText().toString();
//        String rf=obj.refer.getText().toString();



        createacc.setOnClickListener(v -> {

                    String fulln = fullname.getText().toString();
                    String date = dob.getText().toString();  // data parameter is not taken in string request
                    String ht = height.getText().toString();
                    String wt = weight.getText().toString();
                    String ageStr = userage.getText().toString();
                    String uname =getIntent().getExtras().getString("username");
                    String mail = getIntent().getExtras().getString("usermail");
                    String phone = getIntent().getExtras().getString("userphone");
                    String rf = getIntent().getExtras().getString("userrf");
                    String pass = getIntent().getExtras().getString("userpass");

//                    Log.d("c=",uname);
//                    Log.d("Usermail=",mail);
//                    Log.d("Userphone=",phone);
//                    Log.d("Userrf=",rf);
//                    Log.d("Userpass=",pass);


                    if(fulln.isEmpty() )
                    {
                        Toast.makeText(UserDataActivity.this, "Please Enter fullname", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else if(ht.isEmpty())
                    {
                        Toast.makeText(UserDataActivity.this, "Please Enter height", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else if(wt.isEmpty())
                    {
                        Toast.makeText(UserDataActivity.this, "Please Enter weight", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else if(ageStr.isEmpty())
                    {
                        Toast.makeText(UserDataActivity.this, "Please Enter age", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else if(date.isEmpty())
                    {
                        Toast.makeText(UserDataActivity.this, "Please Enter DOB", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else {

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                            System.out.println(response);
                            if (response.equals("successinserted")) {
                                Toast.makeText(getApplicationContext(), "Registration completed", Toast.LENGTH_SHORT).show();

                                Intent id = new Intent(UserDataActivity.this, Login.class);
                                startActivity(id);
                            } else {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }
                        }, error -> {
                            Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                        }) {
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Random rnd = new Random();
                                int number = rnd.nextInt(999999);
                                String str1 = Integer.toString(number);
                                Map<String, String> data = new HashMap<>();
                                data.put("userID", uname);
                                data.put("password", pass);
                                data.put("email", mail);
                                data.put("name", fulln);
                                data.put("phone", phone);
                                data.put("age", ageStr); // age is not in ui but present in php and even in database
                                data.put("weight", wt);
                                data.put("height", ht);
                                data.put("verification", "0");
                                data.put("refer", str1);//this referal code is of user who is creating new account
                                return data;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);

                    }

//            generateReferral();

                }
        );

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UserDataActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },

                        year, month, day);

                datePickerDialog.show();
            }
        });





    }





    }
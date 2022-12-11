package com.example.infits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Signup extends AppCompatActivity {

    TextView term, memlog;
    Button signupbtn, fbsignup, googlesignup;
    RadioButton agreeToCondition;




    EditText userName, emailID, password, phoneNo, refer,cnfpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

//        agreeToCondition = findViewById(R.id.agree);
//        term = (TextView) findViewById(R.id.term);
        memlog = (TextView) findViewById(R.id.memlog);
//        signbtn = (Button) findViewById(R.id.signbtn);
//        fullName = findViewById(R.id.new_name);
        userName = findViewById(R.id.new_user_name);
        emailID = findViewById(R.id.new_email);
        password = findViewById(R.id.new_password);
        cnfpassword=findViewById(R.id.confirm_new_password);
        phoneNo = findViewById(R.id.new_phone_number);
        refer = findViewById(R.id.new_referralcode);
        signupbtn = findViewById(R.id.signupbtn);
        fbsignup = findViewById(R.id.fbsignup);
        googlesignup = findViewById(R.id.googlesignup);



//        age = findViewById(R.id.age_background);
//        height = findViewById(R.id.new_height);
//        weight = findViewById(R.id.new_weight);


//        term.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(Signup.this, TermsAndConditions.class);
//                startActivity(it);
//            }
//        });

        memlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ilo = new Intent(Signup.this, Login.class);
                startActivity(ilo);
            }
        });


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = userName.getText().toString();
                String mail = emailID.getText().toString();
                String phone = phoneNo.getText().toString();
                String rf = refer.getText().toString();
                String pass = password.getText().toString();
                String cnfpass = cnfpassword.getText().toString();


                if(uname.isEmpty() )
                {
                    Toast.makeText(Signup.this, "Please Enter username", Toast.LENGTH_SHORT)
                            .show();
                }
                else if(mail.isEmpty()){
                    Toast.makeText(Signup.this, "Please Enter email", Toast.LENGTH_SHORT)
                            .show();

                }
                else if(phone.isEmpty()){
                    Toast.makeText(Signup.this, "Please Enter phone number", Toast.LENGTH_SHORT)
                            .show();

                }
                else if(pass.isEmpty()){
                    Toast.makeText(Signup.this, "Please Enter password", Toast.LENGTH_SHORT)
                            .show();

                }
                else if(!pass.equals(cnfpass))
                {
                    Toast.makeText(Signup.this, "Passwords are not matching", Toast.LENGTH_SHORT)
                            .show();
                }
                else
                {
                    Intent intent=new Intent(Signup.this,UserDataActivity.class);
                    intent.putExtra("username",uname);
                    intent.putExtra("usermail",mail);
                    intent.putExtra("userphone",phone);
                    intent.putExtra("userrf",rf);
                    intent.putExtra("userpass",pass);

                    startActivity(intent);
                }


            }
        });
        //

//        if (agreeToCondition.isSelected()){
//            signbtn.setClickable(true);
//        }


        //here I need to checked
        // every field should be filled properly and not empty and also need to chekc whether password and confirm passoword should be same
//        signupbtn.setOnClickListener(v -> {
//
//            String userID = userName.getText().toString();
//            String passwordStr = password.getText().toString();
//            String emailStr = emailID.getText().toString();
//            String phoneStr = phoneNo.getText().toString();
//            String referalcode=refer.getText().toString();
////            String fullNameStr = fullName.getText().toString();
////            String ageStr = age.getText().toString();
////            String heightStr = height.getText().toString();
////            String weightStr = weight.getText().toString();
//
////            StringRequest stringRequest = new StringRequest(Request.Method.POST,url, response -> {
////                System.out.println(response);
////                if (response.equals("success")){
////                    Toast.makeText(getApplicationContext(), "Registration completed", Toast.LENGTH_SHORT).show();
////                    Intent id = new Intent(getApplicationContext(), Login.class);
////                    startActivity(id);
////                }
////                else{
////                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
////                }
////            },error -> {
////                Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();}){
////                @Nullable
////                @Override
////                protected Map<String, String> getParams() throws AuthFailureError {
////                    Random rnd = new Random();
////                    int number = rnd.nextInt(999999);
////                    String str1 = Integer.toString(number);
////                    Map<String,String> data = new HashMap<>();
////                    data.put("userID",userID);
////                    data.put("password",passwordStr);
////                    data.put("email",emailStr);
////                    data.put("name",fullNameStr);
////                    data.put("phone",phoneStr);
////                    data.put("age",ageStr);
////                    data.put("weight",heightStr);
////                    data.put("height",weightStr);
////                    data.put("verification","0");
////                    data.put("refer",str1);
////                    return data;
////                }
////            };
////            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
////            requestQueue.add(stringRequest);
////
//////            generateReferral();
////
////        });
//    }

//    private void generateReferral() {
//        Random rnd = new Random();
//        int number = rnd.nextInt(999999);
//        String str1 = Integer.toString(number);
////        referalcode=str1;
////        Random random = new Random();
////        StringBuilder referralCode = new StringBuilder(userName.getText().toString().substring(0, 3));
////
////        for(int i = 0; i < 5; i++) {
////            int num = random.nextInt(10);
////            referralCode.append(num);
////        }
////
//        addToReferralTable(String.valueOf(number));
//    }
//
//    private void addToReferralTable(String referralCode) {
////        String referralUrl = String.format("%supdateReferralTable.php",DataFromDatabase.ipConfig);
//        String referralUrl = String.format("%sregister_client.php",DataFromDatabase.ipConfig);
//
//        StringRequest referralRequest = new StringRequest(
//                Request.Method.POST, referralUrl,
//                response -> Log.d("Signup", "addToReferralTable: " + response),
//                error -> Log.e("Signup", "addToReferralTable: " + error.toString())
//        ) {
//            @NotNull
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> data = new HashMap<>();
//
////                data.put("clientID", userName.getText().toString());
//                data.put("refer", referralCode);
////                data.put("activeUsers", "none");
//
//                return data;
//            }
//        };
//        Volley.newRequestQueue(this).add(referralRequest);
//    }
    }
}
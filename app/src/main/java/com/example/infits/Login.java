package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextView reg, fpass;
    Button loginbtn;
    String passwordStr,usernameStr;
    String url = String.format("%slogin_client.php",DataFromDatabase.ipConfig);
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        reg = (TextView) findViewById(R.id.reg);
        fpass = (TextView) findViewById(R.id.fpass);
        loginbtn = (Button) findViewById(R.id.logbtn);

        queue = Volley.newRequestQueue(this);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ireg = new Intent(Login.this, Signup.class);
                startActivity(ireg);
            }
        });

        fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ip = new Intent(Login.this, ResetPassword.class);
                startActivity(ip);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username= findViewById(R.id.username);
                EditText password= findViewById(R.id.password);
                usernameStr = username.getText().toString();
                passwordStr = password.getText().toString();

                loginbtn.setClickable(false);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                    if(response.equals("failure")){
                        Toast.makeText(Login.this,"Login failed",Toast.LENGTH_SHORT).show();
                        loginbtn.setClickable(true);
                    }else{
                        Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_LONG).show();
                        Intent id = new Intent(Login.this, DashBoardMain.class);
                        Log.d("Response Login",response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject object = jsonArray.getJSONObject(0);
                            DataFromDatabase.flag=true;
                            DataFromDatabase.clientuserID  = object.getString("clientuserID");
                            DataFromDatabase.dietitianuserID = object.getString("dietitianuserID");
                            DataFromDatabase.name = object.getString("name");
                            Log.d("name login",DataFromDatabase.name);
                            SharedPreferences loginDetails = getSharedPreferences("loginDetails",MODE_PRIVATE);
                            SharedPreferences.Editor editor = loginDetails.edit();
                            editor.putString("name",object.getString("name"));
                            editor.putString("clientuserID",object.getString("clientuserID"));
                            editor.apply();
                            DataFromDatabase.password = object.getString("password");
                            DataFromDatabase.email = object.getString("email");
                            DataFromDatabase.mobile = object.getString("mobile");
                            DataFromDatabase.profilePhoto = object.getString("profilePhoto");
                            DataFromDatabase.location = object.getString("location");
                            DataFromDatabase.age = object.getString("age");
                            DataFromDatabase.gender  = object.getString("gender");
                            DataFromDatabase.profilePhotoBase = DataFromDatabase.profilePhoto;

                            if (object.getString("verification").equals("0")){
                                DataFromDatabase.proUser = false;
                            }
                            if (object.getString("verification").equals("1")){
                                DataFromDatabase.proUser = true;
                            }
                            System.out.println(DataFromDatabase.proUser+" Prouser");
                            byte[] qrimage = Base64.decode(DataFromDatabase.profilePhoto,0);
                            DataFromDatabase.profile = BitmapFactory.decodeByteArray(qrimage,0,qrimage.length);
                            Log.d("Login Screen","client user id = "+ DataFromDatabase.clientuserID);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(id);
                    }
                }, error -> {
                    Toast.makeText(Login.this,error.toString().trim(),Toast.LENGTH_SHORT).show();
                    loginbtn.setClickable(true);
                }){
                    @Override
                    protected Map<String,String> getParams() throws AuthFailureError{
                        LinkedHashMap<String,String> data = new LinkedHashMap<>();
                        data.put("userID",usernameStr);
                        Log.d("userID",usernameStr);
                        data.put("password",passwordStr);
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });
    }
}
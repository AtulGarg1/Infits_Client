package com.example.infits;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataFromDatabase {
    public static boolean flag= false;
    public static String profilePhoto="default.jpg";
    public static String dietitianuserID,clientuserID,password,name,email,mobile,location,age,gender;
    public static Bitmap profile;
}
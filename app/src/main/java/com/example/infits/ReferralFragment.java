package com.example.infits;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReferralFragment extends Fragment {

    ImageView imgBack;
    ImageButton copy;
    Button share, gotReferred;
    TextView referralTV;
    String passwordStr,usernameStr;
    String url = String.format("%slogin_client.php",DataFromDatabase.ipConfig);
//    String referralCode = "";


    public ReferralFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_referral, container, false);
        usernameStr=DataFromDatabase.clientuserID;
        passwordStr=DataFromDatabase.password;

        hooks(view);

        SharedPreferences getshared= getContext().getSharedPreferences("loginDetails",MODE_PRIVATE);
        String referalcode=getshared.getString("refer","123");
        referralTV.setText(referalcode);

//        getreferal();
        // here I used refer from dashboard fragment



        imgBack.setOnClickListener(v -> requireActivity().onBackPressed());

        copy.setOnClickListener(v -> {
            String textToCopy = DataFromDatabase.referal; /// refer from other command

            if(!textToCopy.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", textToCopy);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_LONG).show();
            }
        });

        // also need to add link of app
        share.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT,DataFromDatabase.referal);
            sendIntent.putExtra(Intent.EXTRA_TEXT,"Here is the link \n https://play.google.com/store/apps/details?id="+"com.dream11.fantasy.cricket.football.kabaddi"+"\n Use this referral code for getting subscription\n"+ DataFromDatabase.referal);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

        gotReferred.setOnClickListener(this::showDialog);
        return view;
    }



    private void showDialog(View view) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.referral_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText referralET = dialog.findViewById(R.id.referralET);
        Button submit = dialog.findViewById(R.id.submit);
        Button cancel = dialog.findViewById(R.id.cancel);

        submit.setOnClickListener(v -> {
            String referralCode = referralET.getText().toString();

            if(!referralCode.isEmpty()) {
                checkReferralTable(referralCode);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void checkReferralTable(String referralCode) {
        String referralUrl = String.format("%scheckReferralTable.php",DataFromDatabase.ipConfig);

        StringRequest referralRequest = new StringRequest(
                Request.Method.POST, referralUrl,
                response -> {
                    Log.d("ReferralFragment", "checkReferralTable: " + response);

                    if(response.equals("found")) {
                        updateReferralTable(referralCode);
                        showSuccessDialog();
                    } else {
                        showFailureDialog();
                    }
                },
                error -> Log.e("ReferralFragment", "checkReferralTable: " + error.toString())
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("referralCode", referralCode);

                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(referralRequest);
    }

    private void showFailureDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.referral_try_again);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView btn = dialog.findViewById(R.id.btn);

        btn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.referral_congratulation);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView btn = dialog.findViewById(R.id.btn);

        btn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void updateReferralTable(String referralCode) {
        String referralUrl = String.format("%supdateReferralTable.php",DataFromDatabase.ipConfig);

        StringRequest referralRequest = new StringRequest(
                Request.Method.POST, referralUrl,
                response -> {
                    Log.d("ReferralFragment", "updateReferralTable: " + response);

                },
                error -> Log.e("ReferralFragment", "updateReferralTable: " + error.toString())
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("referralCode", referralCode);
                data.put("activeUsers", DataFromDatabase.clientuserID); // these are userid of current logged in user
                data.put("clientID", DataFromDatabase.clientuserID); /* no actual need for this but cannot leave null as well */

                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(referralRequest);
    }

// pratik made this two fucntions  these functions are unused becauses I have used sharedprefences to store referalcode and set text accordingy
//    private   void getreferal()
//    {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
//            if(response.equals("failure")){
//                Toast.makeText(getContext(),"Login failed",Toast.LENGTH_SHORT).show();
//
//            }else{
//
//                Log.d("Response Login",response);
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    JSONObject object = jsonArray.getJSONObject(0);
//
//                    DataFromDatabase.name = object.getString("name");
//                    Log.d("name login",DataFromDatabase.name);
//
//
//                    DataFromDatabase.referal  = object.getString("refer");
//
//                    Log.d("referal","referal id got = "+ DataFromDatabase.referal);
//
//
////                    referralTV.setText(DataFromDatabase.referal);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, error -> {
//            Toast.makeText(getContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
//
//        }){
//            @Override
//            protected Map<String,String> getParams() throws AuthFailureError{
//                LinkedHashMap<String,String> data = new LinkedHashMap<>();
//                data.put("userID",usernameStr);
//                data.put("password",passwordStr);
//                return data;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//
//    }




    private void hooks(View view) {
        imgBack = view.findViewById(R.id.img_back);
        copy = view.findViewById(R.id.copy);
        share = view.findViewById(R.id.share);
        gotReferred = view.findViewById(R.id.got_referred);
        referralTV = view.findViewById(R.id.referralTV);
    }
}
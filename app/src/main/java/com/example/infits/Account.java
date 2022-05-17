package com.example.infits;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account extends Fragment {

    String url = "http://192.168.177.91/infits/save.php";

    ActivityResultLauncher<String> photo;

    File file;

    String fileName, path;

    ImageView imgback;
    Button logout,save;
    ImageView male, female,profile_pic;
    String gen = "M";

    Bitmap photoBit;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account.
     */
    // TODO: Rename and change types and number of parameters
    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        imgback = view.findViewById(R.id.backToSettings);
        logout = view.findViewById(R.id.button_logout);
        male = view.findViewById(R.id.gender_male_icon);
        female=view.findViewById(R.id.gender_female_icon);
        profile_pic=view.findViewById(R.id.dp);
        save=view.findViewById(R.id.button_save);
        EditText name=view.findViewById(R.id.name_edt);
        EditText age=view.findViewById(R.id.age_edt);
        EditText email=view.findViewById(R.id.email_edt);
        EditText phone=view.findViewById(R.id.phone_edt);

        profile_pic.setImageBitmap(DataFromDatabase.profile);

        photo = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        profile_pic.setImageURI(result);
                        path = result.getPath();
                        file = new File(result.toString());
                        String filename = path.substring(path.lastIndexOf("/")+1);
                        if (filename.indexOf(".") > 0) {
                            fileName = filename.substring(0, filename.lastIndexOf("."));
                        } else {
                            fileName =  filename;
                        }
                        Log.d("MainClass", "Real Path: " + path);
                        Log.d("MainClass", "Filename With Extension: " + filename);
                        Log.d("MainClass", "File Without Extension: " + fileName);
                        try {
                            photoBit = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver() , result);
                            DataFromDatabase.profile = photoBit;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        profile_pic.setOnClickListener(v->{
            photo.launch("image/*");
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setImageResource(R.drawable.gender_male_selected);
                female.setImageResource(R.drawable.gender_female);
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setImageResource(R.drawable.gender_male);
                female.setImageResource(R.drawable.gender_female_selected);
                gen = "F";
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_account_to_settingsFragment);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.logoutdialog);

                Button yes = dialog.findViewById(R.id.yes_log_out);
                Button no = dialog.findViewById(R.id.no_log_out);

                yes.setOnClickListener(v->{
                    getActivity().finishAffinity();
                    System.exit(0);
                });
                no.setOnClickListener(v->{
                    dialog.dismiss();
                });
                dialog.show();
            }
        });

        save.setOnClickListener(v->{
            String nameStr = name.getText().toString();
            String ageStr = age.getText().toString();
            String emailStr = email.getText().toString();
            String mobile = phone.getText().toString();
            StringRequest request = new StringRequest(Request.Method.POST,url,response -> {
                    if (response.equals("updated")){
                        Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                    }
            },error -> {
                Toast.makeText(getActivity(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String image = getStringOfImage(photoBit);
                    LinkedHashMap<String,String> data = new LinkedHashMap<>();
                    data.put("userID",DataFromDatabase.clientuserID);
                    data.put("email",emailStr);
                    data.put("gender",gen);
                    data.put("age",ageStr);
                    data.put("mobile",mobile);
                    data.put("name",nameStr);
                    data.put("img",image);
                    data.put("nameImg",DataFromDatabase.clientuserID);

                    return data;
                }
            };
            Volley.newRequestQueue(getActivity()).add(request);
        });

        return view;
    }
    public String getStringOfImage(Bitmap bm){
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,bo);
        byte[] imageByte = bo.toByteArray();
        String imageEncode = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return imageEncode;
    }
}
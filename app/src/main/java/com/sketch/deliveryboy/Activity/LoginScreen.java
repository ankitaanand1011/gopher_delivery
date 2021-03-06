package com.sketch.deliveryboy.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sketch.deliveryboy.R;
import com.sketch.deliveryboy.utils.AppController;
import com.sketch.deliveryboy.utils.GPSTracker;
import com.sketch.deliveryboy.utils.GlobalClass;
import com.sketch.deliveryboy.utils.Shared_Preference;
import com.sketch.deliveryboy.utils.WebserviceUrl;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class LoginScreen extends AppCompatActivity {

    String TAG = "login";

    ImageView login_img;
    TextView tv_f_password;
    EditText email_edt, password_edt;
    GlobalClass globalClass;
    Shared_Preference prefrence;
    String device_id;
    String fcm_token;
    ProgressDialog pd;

    double latitude;
    double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        globalClass = (GlobalClass) getApplicationContext();
        prefrence = new Shared_Preference(LoginScreen.this);
        prefrence.loadPrefrence();

        pd=new ProgressDialog(LoginScreen.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.loading));


        device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "device_id: " + device_id);
        globalClass.setDeviceid(device_id);

        // fcm_token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "fcm_token: " + fcm_token);


        email_edt = findViewById(R.id.edt_email);
        password_edt = findViewById(R.id.edt_password);
        login_img = findViewById(R.id.login_img);
        tv_f_password = findViewById(R.id.tv_f_password);

        GPSTracker gps = new GPSTracker(this);
        latitude = gps.getLatitude();
        longitude= gps.getLongitude();

        Log.d(TAG, "latitude: "+latitude);
        Log.d(TAG, "longitude: "+longitude);

        login_img=findViewById(R.id.login_img);
        login_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalClass.isNetworkAvailable()) {
                    if (!email_edt.getText().toString().isEmpty()) {
                        if (isValidEmail(email_edt.getText().toString())) {
                            if (!password_edt.getText().toString().isEmpty()) {

                                String email = email_edt.getText().toString();
                                String password = password_edt.getText().toString();

                                Log.d(TAG, "email: "+email);
                                Log.d(TAG, "onClick: "+password);

                                if(!String.valueOf(latitude).isEmpty()&& !String.valueOf(longitude).isEmpty() ) {
                                    login_url(email, password);
                                }
                            } else {
                                Toasty.warning(LoginScreen.this, "Please enter password.", Toast.LENGTH_SHORT, true).show();
                            }
                        } else {
                            Toasty.warning(LoginScreen.this, "Please enter valid email.", Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.warning(LoginScreen.this, "Please enter email.", Toast.LENGTH_SHORT, true).show();
                    }
                } else {
                    Toasty.info(LoginScreen.this, "Please check your internet connection", Toast.LENGTH_SHORT, true).show();
                }

            }
        });



        tv_f_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this,Forget_Password.class);
                startActivity(intent);
            }
        });


    }
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void login_url(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pd.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebserviceUrl.login, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, "url_hit: " + WebserviceUrl.login);
                Log.d(TAG, "Response: " + response);

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    //JSONObject jObject = new JSONObject(String.valueOf(content));
                    String status = jobj.get("status").toString().replaceAll("\"", "");
                    String message = jobj.get("message").toString().replaceAll("\"", "");


                    Log.d("TAG", "status :\t" + status);
                    Log.d("TAG", "message :\t" + message);

                    if(status.equals("1")) {

                        JsonObject jsonObject = jobj.getAsJsonObject("info");

                        String uid = jsonObject.get("uid").toString().replaceAll("\"", "");
                        String gopher_id = jsonObject.get("gopher_id").toString().replaceAll("\"", "");
                        String name = jsonObject.get("name").toString().replaceAll("\"", "");
                        String mobile = jsonObject.get("mobile").toString().replaceAll("\"", "");
                        String email = jsonObject.get("email").toString().replaceAll("\"", "");
                        String image = jsonObject.get("image").toString().replaceAll("\"", "");
                        String driver_license_id = jsonObject.get("driver_license_id").toString().replaceAll("\"", "");
                        String driver_police_verification = jsonObject.get("driver_police_verification").toString().replaceAll("\"", "");
                        //String image = jsonObject.get("image").toString().replaceAll("\"", "");


                        globalClass.setId(uid);
                        globalClass.setName(name);
                        globalClass.setPhone_number(mobile);
                        globalClass.setEmail(email);
                        globalClass.setProfil_pic(image);
                        globalClass.setLogin_status(true);

                        prefrence.savePrefrence();

                        Intent mainIntent = new Intent(LoginScreen.this, DrawerActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }else{
                        Toasty.error(LoginScreen.this,message, Toast.LENGTH_LONG).show();
                    }
                    pd.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "login Error: " + error.getMessage());
             //   Toast.makeText(LoginScreen.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    pd.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();


                params.put("email", email);
                params.put("password", password);
                params.put("device_id", device_id);
                params.put("fcm_reg_token", "12345");
                params.put("device_type", "android");
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));


                Log.d(TAG, "url_hit: " + WebserviceUrl.login);
                Log.d(TAG, "getParams: " + params);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));


    }



}

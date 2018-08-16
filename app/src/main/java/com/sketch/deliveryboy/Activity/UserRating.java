package com.sketch.deliveryboy.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.sketch.deliveryboy.utils.GlobalClass;
import com.sketch.deliveryboy.utils.Shared_Preference;
import com.sketch.deliveryboy.utils.WebserviceUrl;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class UserRating extends AppCompatActivity {
    RatingBar ratingBar;
    String TAG = "user_rating";
    GlobalClass globalClass;
    TextView submit_tv;
    Shared_Preference prefrence;
    ProgressDialog pd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userrating);


        globalClass = (GlobalClass)getApplicationContext();
        prefrence = new Shared_Preference(UserRating.this);
        prefrence.loadPrefrence();
        pd=new ProgressDialog(UserRating.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");

        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ratingBar = findViewById(R.id.ratingBar);
        submit_tv = findViewById(R.id.submit_tv);

        ratingBar.getRating() ;

        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review_url(ratingBar.getRating(),getIntent().getStringExtra("c_id"));
            }
        });

    }

    private void review_url(final float rating, final String c_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pd.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebserviceUrl.review, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

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

                  /*      JsonObject jsonObject = jobj.getAsJsonObject("info");

                        String uid = jsonObject.get("uid").toString().replaceAll("\"", "");
                        String gopher_id = jsonObject.get("gopher_id").toString().replaceAll("\"", "");
                        String name = jsonObject.get("name").toString().replaceAll("\"", "");
                        String mobile = jsonObject.get("mobile").toString().replaceAll("\"", "");
                        String email = jsonObject.get("email").toString().replaceAll("\"", "");
                        String image = jsonObject.get("image").toString().replaceAll("\"", "");
                        String driver_license_id = jsonObject.get("driver_license_id").toString().replaceAll("\"", "");
                        String driver_police_verification = jsonObject.get("driver_police_verification").toString().replaceAll("\"", "");
                        //String image = jsonObject.get("image").toString().replaceAll("\"", "");
*/

                        Intent mainIntent = new Intent(UserRating.this, DrawerActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }else{

                        Toasty.error(UserRating.this, message, Toast.LENGTH_SHORT, true).show();
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
              //  Toast.makeText(UserRating.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();


                params.put("rating", String.valueOf(rating));
                params.put("customer_id", c_id);
                params.put("id",globalClass.getId() );


                Log.d(TAG, "url_hit: " + WebserviceUrl.review);
                Log.d(TAG, "getParams: " + params);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));


    }
}
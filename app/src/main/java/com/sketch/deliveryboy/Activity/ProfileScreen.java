package com.sketch.deliveryboy.Activity;

import android.app.ProgressDialog;
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
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sketch.deliveryboy.R;
import com.sketch.deliveryboy.utils.AppController;
import com.sketch.deliveryboy.utils.GlobalClass;
import com.sketch.deliveryboy.utils.Shared_Preference;
import com.sketch.deliveryboy.utils.WebserviceUrl;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class ProfileScreen extends AppCompatActivity {

    String TAG = "profile";
    GlobalClass  globalClass ;
    Shared_Preference preference;
    ProgressDialog pd;
    TextView name_tv,txt_phone_no,txt_gopher_id,txt_license,txt_police_verification;
    ImageView imageView2;
    RatingBar ratingBar;
    ImageLoader loader;
    DisplayImageOptions defaultOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        globalClass = (GlobalClass) getApplicationContext();
        preference = new Shared_Preference(ProfileScreen.this);
        preference.loadPrefrence();

        pd=new ProgressDialog(ProfileScreen.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.loading));

        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                //  .showImageOnLoading(R.mipmap.loading_black128px)
                //  .showImageForEmptyUri(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.img_failed)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ProfileScreen.this.getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();

        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        name_tv = findViewById(R.id.name_tv);
        txt_phone_no = findViewById(R.id.txt_phone_no);
        txt_gopher_id = findViewById(R.id.txt_gopher_id);
        txt_license = findViewById(R.id.txt_license);
        txt_police_verification = findViewById(R.id.txt_police_verification);
        imageView2 = findViewById(R.id.imageView2);
        ratingBar = findViewById(R.id.ratingBar);

        if(globalClass.getProfil_pic().isEmpty()){
            imageView2.setImageResource(R.mipmap.no_image);
        }else {

            loader.displayImage(globalClass.getProfil_pic(), imageView2, defaultOptions);
        }

        get_profile_url();
    }

    private void get_profile_url() {
        // Tag used to cancel the request
        pd.show();
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebserviceUrl.get_profile_details, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, "url_hit: " + WebserviceUrl.get_profile_details);
                Log.d(TAG, "Response: " + response);

                Gson gson = new Gson();

                try {

                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    //JSONObject jObject = new JSONObject(String.valueOf(content));
                    String status = jobj.get("status").toString().replaceAll("\"", "");
                    String message = jobj.get("message").toString().replaceAll("\"", "");


                    Log.d("TAG", "status :\t" + status);
                    Log.d("TAG", "message :\t" + message);

                    if (status.equals("1")) {

                        JsonObject jsonObject = jobj.getAsJsonObject("info");

                        String uid = jsonObject.get("uid").toString().replaceAll("\"", "");
                        String gopher_id = jsonObject.get("gopher_id").toString().replaceAll("\"", "");
                        String name = jsonObject.get("name").toString().replaceAll("\"", "");
                        String mobile = jsonObject.get("mobile").toString().replaceAll("\"", "");
                        String emailid = jsonObject.get("emailid").toString().replaceAll("\"", "");
                        String driver_license_id = jsonObject.get("driver_license_id").toString().replaceAll("\"", "");
                        String driver_police_verification = jsonObject.get("driver_police_verification").toString().replaceAll("\"", "");
                        String image = jsonObject.get("image").toString().replaceAll("\"", "");
                        String rating = jsonObject.get("rating").toString().replaceAll("\"", "");


                        Log.d(TAG, "onResponse: "+name);

                        name_tv.setText(name);
                        txt_phone_no.setText(mobile);
                        txt_gopher_id.setText(gopher_id);
                        txt_license.setText(driver_license_id);
                        txt_police_verification.setText(driver_police_verification);
                        ratingBar.setRating(Float.parseFloat(rating));

                        if(image.isEmpty()){
                            imageView2.setImageResource(R.mipmap.no_image);
                        }else {

                            loader.displayImage(image, imageView2, defaultOptions);
                        }
                        globalClass.setId(uid);
                        globalClass.setName(name);
                        globalClass.setPhone_number(mobile);
                        globalClass.setProfil_pic(image);
                        globalClass.setEmail(emailid);


                        preference.savePrefrence();

                    }else{
                        Toasty.error(ProfileScreen.this, message, Toast.LENGTH_LONG, true).show();

                    }
                    pd.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "get_profile_details Error: " + error.getMessage());
                Toast.makeText(ProfileScreen.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();



                params.put("id",globalClass.getId());


                Log.d(TAG, "getParams: " + params);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));


    }
}
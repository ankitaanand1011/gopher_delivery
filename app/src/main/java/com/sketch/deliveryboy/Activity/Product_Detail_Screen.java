package com.sketch.deliveryboy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sketch.deliveryboy.R;
import com.sketch.deliveryboy.utils.AppController;
import com.sketch.deliveryboy.utils.GlobalClass;
import com.sketch.deliveryboy.utils.WebserviceUrl;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class Product_Detail_Screen extends AppCompatActivity {

    String TAG = "product_detail";
    GlobalClass globalClass;
    ImageView img_product;
    TextView tv_order_id,tv_product_name,tv_customer_name,tv_product_qty,tv_product_amount,
            tv_instruction,tv_pick_location,tv_drop_location;

    ImageLoader loader;
    DisplayImageOptions defaultOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        globalClass = (GlobalClass) getApplicationContext();

        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                //  .showImageOnLoading(R.mipmap.loading_black128px)
                //  .showImageForEmptyUri(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.img_failed)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(Product_Detail_Screen.this.getApplicationContext())
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


        tv_order_id = findViewById(R.id.tv_order_id);
        tv_product_name = findViewById(R.id.tv_product_name);
        tv_customer_name = findViewById(R.id.tv_customer_name);
        img_product = findViewById(R.id.img_product);
        tv_product_qty = findViewById(R.id.tv_product_qty);
        tv_product_amount = findViewById(R.id.tv_product_amount);
        tv_instruction = findViewById(R.id.tv_instruction);
        tv_pick_location = findViewById(R.id.tv_pick_location);
        tv_drop_location = findViewById(R.id.tv_drop_location);


        job_accept_url(getIntent().getStringExtra("id"));
    }

    private void job_accept_url(final String id) {
        // Tag used to cancel the request


        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebserviceUrl.job_details, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response: " + response);

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    //JSONObject jObject = new JSONObject(String.valueOf(content));
                    String status = jobj.get("status").toString().replaceAll("\"", "");
                    String message = jobj.get("message").toString().replaceAll("\"", "");


                    Log.d(TAG, "status :\t" + status);
                    Log.d(TAG, "message :\t" + message);

                    if(status.equals("1")) {
                        //   arr_order_all.clear();
                        JsonObject jObject =jobj.getAsJsonObject("info");

                        String id = jObject.get("id").toString().replaceAll("\"", "");
                        String customer_id = jObject.get("customer_id").toString().replaceAll("\"", "");
                        String deliveryboy_id = jObject.get("deliveryboy_id").toString().replaceAll("\"", "");
                        String name = jObject.get("name").toString().replaceAll("\"", "");
                        String otp = jObject.get("name").toString().replaceAll("\"", "");
                        String product_type = jObject.get("product_type").toString().replaceAll("\"", "");
                        String product_quantity = jObject.get("product_quantity").toString().replaceAll("\"", "");
                        String image = jObject.get("image").toString().replaceAll("\"", "");
                        String shop_name = jObject.get("shop_name").toString().replaceAll("\"", "");
                        String instruction = jObject.get("instruction").toString().replaceAll("\"", "");
                        String product_price = jObject.get("product_price").toString().replaceAll("\"", "");
                        String latitute = jObject.get("latitute").toString().replaceAll("\"", "");
                        String longitude = jObject.get("longitude").toString().replaceAll("\"", "");
                        String address = jObject.get("address").toString().replaceAll("\"", "");
                        String shop_address = jObject.get("shop_address").toString().replaceAll("\"", "");
                        String shop_latitude = jObject.get("shop_latitude").toString().replaceAll("\"", "");
                        String shop_longitude = jObject.get("shop_longitude").toString().replaceAll("\"", "");
                        String status1 = jObject.get("status").toString().replaceAll("\"", "");
                        String job_status = jObject.get("job_status").toString().replaceAll("\"", "");
                        String order_placed_on = jObject.get("order_placed_on").toString().replaceAll("\"", "");
                        String fname = jObject.get("fname").toString().replaceAll("\"", "");




                        tv_order_id.setText(id);
                        tv_product_name.setText(name);
                        tv_customer_name.setText(fname);
                        tv_product_qty.setText(product_quantity);
                        tv_product_amount.setText("$ "+product_price);
                        tv_instruction.setText(instruction);
                        tv_pick_location.setText(shop_address);
                        tv_drop_location.setText(address);

                        if(image.equals("")||image.isEmpty()){

                            img_product.setImageResource(R.mipmap.no_image);
                        }else {

                            loader.displayImage(image, img_product, defaultOptions);
                        }

                    } else {

                        Toasty.error(Product_Detail_Screen.this, message, Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "job_accept Error: " + error.getMessage());
                Toasty.error(Product_Detail_Screen.this, error.getMessage(), Toast.LENGTH_LONG).show();
                //   globalClass.dismiss_pd(getActivity());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",globalClass.getId());
                params.put("job_id",id);

                Log.d(TAG, "url_hit: " + WebserviceUrl.job_details);
                Log.d(TAG, "getParams: " + params);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));


    }
}

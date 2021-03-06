package com.sketch.deliveryboy.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.sketch.deliveryboy.R;
import com.sketch.deliveryboy.utils.AppController;
import com.sketch.deliveryboy.utils.GlobalClass;
import com.sketch.deliveryboy.utils.WebserviceUrl;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class JobDetailScreen extends AppCompatActivity {
    String TAG = "view_job";
    TextView accept,tv_title,tv_instruction,tv_pick_location,tv_drop_loc,tv_next,tv_view_map;
    GlobalClass globalClass;
    ProgressDialog pd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewjob);
        Log.d(TAG, "onCreate: job_detail");

        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        globalClass = (GlobalClass) getApplicationContext();

        pd=new ProgressDialog(JobDetailScreen.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.loading));


        accept=findViewById(R.id.accept);
        tv_next=findViewById(R.id.tv_next);
        tv_title=findViewById(R.id.tv_title);
        tv_instruction=findViewById(R.id.tv_instruction);
        tv_pick_location=findViewById(R.id.tv_pickup);
        tv_drop_loc=findViewById(R.id.tv_delivery_location);
        tv_view_map=findViewById(R.id.tv_view_map);

        tv_title.setText(getIntent().getStringExtra("name"));
        tv_instruction.setText(getIntent().getStringExtra("instruction"));
        tv_pick_location.setText(getIntent().getStringExtra("shop_address"));
        tv_drop_loc.setText(getIntent().getStringExtra("address"));

        if(getIntent().getStringExtra("from").equals("accepted_adapter")){

            accept.setVisibility(View.GONE);
            tv_next.setVisibility(View.VISIBLE);

        }else {

            accept.setVisibility(View.VISIBLE);
            tv_next.setVisibility(View.GONE);
        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: gfwehg");
                job_accept_url(getIntent().getStringExtra("id"));

            }
        });

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), JobStatus.class);
                intent.putExtra("id",getIntent().getStringExtra("id"));
                intent.putExtra("customer_id",getIntent().getStringExtra("customer_id"));
                intent.putExtra("name",getIntent().getStringExtra("name"));
                intent.putExtra("instruction",getIntent().getStringExtra("instruction"));
                intent.putExtra("address",getIntent().getStringExtra("address"));
                intent.putExtra("job_status",getIntent().getStringExtra("job_status"));
                intent.putExtra("product_price",getIntent().getStringExtra("product_price"));
                intent.putExtra("product_quantity",getIntent().getStringExtra("product_quantity"));
                intent.putExtra("gopher_earned",getIntent().getStringExtra("gopher_earned"));
                startActivity(intent);
            }
        });

        tv_view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGoogleMapsInstalled()) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,

                            Uri.parse("http://maps.google.com/maps?"
                                    +"saddr="+getIntent().getStringExtra("o_lat")
                                    +","+
                                    getIntent().getStringExtra("o_lng")
                                    +"&daddr="+
                                    getIntent().getStringExtra("d_lat")
                                    +","+
                                    getIntent().getStringExtra("d_lng")));
                    startActivity(intent);
                }else{
                      /*  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
                        context.startActivity(intent);

                        //Finish the activity so they can't circumvent the check
                        ((Activity)context).finish();*/

                }
            }
        });
    }

    private void job_accept_url(final String id) {
        // Tag used to cancel the request


        String tag_string_req = "req_login";
        pd.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebserviceUrl.job_accept, new Response.Listener<String>() {


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

                            String gopher_earned = jObject.get("gopher_earned").toString().replaceAll("\"", "");

                        //    arr_order_all.add(map_ser);


                        Log.d(TAG, "onResponse:mydrtrtjj ");
                        Intent intent=new Intent(getApplicationContext(), JobStatus.class);
                        intent.putExtra("id",id);
                        intent.putExtra("customer_id",customer_id);
                        intent.putExtra("name",name);
                        intent.putExtra("instruction",instruction);
                        intent.putExtra("address",address);
                        intent.putExtra("job_status",job_status);
                        intent.putExtra("product_price",product_price);
                        intent.putExtra("product_quantity",product_quantity);
                        intent.putExtra("gopher_earned",gopher_earned);
                        startActivity(intent);
                        Log.d("mercy", "functions: job_detail "+product_quantity);


                        //  globalClass.dismiss_pd(getActivity());

                    } else {

                        Toasty.error(JobDetailScreen.this, message, Toast.LENGTH_LONG).show();

                    }

                    pd.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "job_accept Error: " + error.getMessage());
                Toasty.error(JobDetailScreen.this, error.getMessage(), Toast.LENGTH_LONG).show();
                //   globalClass.dismiss_pd(getActivity());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",globalClass.getId());
                params.put("job_id",id);

                Log.d(TAG, "url_hit: " + WebserviceUrl.job_accept);
                Log.d(TAG, "getParams: " + params);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));


    }
    public boolean isGoogleMapsInstalled()
    {
        try
        {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

}
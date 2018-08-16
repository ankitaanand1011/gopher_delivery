package com.sketch.deliveryboy.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class JobStatus extends AppCompatActivity {

    String TAG = "job_status";
    TextView updatestatus,tv_status_1,tv_status_2,tv_status_3,
            tv_description,tv_addess,tv_price,tv_view_details,tv_request_for_money;
    Spinner spinner_status;
    ImageView img_back,img_accepted,img_picked_up,img_out_for_delivery;
    String status, job_id,instruction, address, product_price, customer_id,item;
    GlobalClass globalClass;
    ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobstatus);

        intialize();
        functions();
        job_status_url();
    }


    public void intialize(){
        
        globalClass = (GlobalClass) getApplicationContext();
        pd=new ProgressDialog(JobStatus.this);        
        
        img_back = findViewById(R.id.img_back);
        tv_status_1=findViewById(R.id.tv_status_1);
        tv_status_2=findViewById(R.id.tv_status_2);
        tv_status_3=findViewById(R.id.tv_status_3);

        img_accepted=findViewById(R.id.img_accepted);
        img_picked_up=findViewById(R.id.img_picked_up);
        img_out_for_delivery=findViewById(R.id.img_out_for_delivery);

        tv_description=findViewById(R.id.tv_description);
        tv_addess=findViewById(R.id.tv_addess);
        tv_price=findViewById(R.id.tv_price);
        updatestatus=findViewById(R.id.update);
        tv_view_details=findViewById(R.id.tv_view_details);
        tv_request_for_money=findViewById(R.id.tv_request_for_money);
        spinner_status=findViewById(R.id.spinner_status);



    }

    public void functions(){
        
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.loading));

        status =  getIntent().getStringExtra("job_status");
        job_id =  getIntent().getStringExtra("id");
        instruction = getIntent().getStringExtra("instruction");
        address = getIntent().getStringExtra("address");
        product_price = "$ "+getIntent().getStringExtra("product_price");
        customer_id = getIntent().getStringExtra("customer_id");

        tv_description.setText(instruction);
        tv_addess.setText(address);
        tv_price.setText(product_price);


        Log.d(TAG, "functions: "+status);


        List<String> categories = new ArrayList<String>();
        categories.add("On the way");
        categories.add("Picked Up");
        categories.add("Delivered");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_status.setAdapter(dataAdapter);


        spinner_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String item1 = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemSelected: "+item);
                switch (item1) {
                    case "On the way":
                        item = "on_the_way";
                        break;

                    case "Picked Up":
                        item = "picked_up";
                        break;

                    case "Delivered":
                        item = "delivered";
                        break;

                    default:
                        item = "accepted";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        switch (status) {
            case "accepted":
                tv_status_1.setVisibility(View.VISIBLE);
                tv_status_2.setVisibility(View.GONE);
                tv_status_3.setVisibility(View.GONE);

                img_accepted.setImageResource(R.mipmap.status_filled);
                img_out_for_delivery.setImageResource(R.mipmap.status_empty);
                img_picked_up.setImageResource(R.mipmap.status_empty);

                break;

            case "picked_up":
                tv_status_1.setVisibility(View.GONE);
                tv_status_2.setVisibility(View.VISIBLE);
                tv_status_3.setVisibility(View.GONE);

                img_accepted.setImageResource(R.mipmap.status_empty);
                img_out_for_delivery.setImageResource(R.mipmap.status_empty);
                img_picked_up.setImageResource(R.mipmap.status_filled);

                break;
            case "on_the_way":
                tv_status_1.setVisibility(View.GONE);
                tv_status_2.setVisibility(View.GONE);
                tv_status_3.setVisibility(View.VISIBLE);

                img_accepted.setImageResource(R.mipmap.status_empty);
                img_out_for_delivery.setImageResource(R.mipmap.status_filled);
                img_picked_up.setImageResource(R.mipmap.status_empty);

                break;

        }


        updatestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                job_status_update_url();
            }
        });

        tv_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Product_Detail_Screen.class);
                intent.putExtra("id",job_id);
                startActivity(intent);
            }
        });

        tv_request_for_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Request_Money_Screen.class);
                intent.putExtra("id",job_id);
                startActivity(intent);
            }
        });

    }


    private void job_status_update_url() {
        pd.show();
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebserviceUrl.job_status_update, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "url_hit: " + WebserviceUrl.job_status_update);
                Log.d(TAG, "Response: " + response);

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    //JSONObject jObject = new JSONObject(String.valueOf(content));
                    String status = jobj.get("status").toString().replaceAll("\"", "");
                    String message = jobj.get("message").toString().replaceAll("\"", "");
                    String job_status = jobj.get("job_status").toString().replaceAll("\"", "");


                    Log.d("TAG", "status :\t" + status);
                    Log.d("TAG", "message :\t" + message);

                    if (status.equals("1")){
                        switch (job_status) {
                            case "delivered":

                                Intent intent = new Intent(getApplicationContext(), Delivered.class);
                                intent.putExtra("customer_id", customer_id);
                                intent.putExtra("id", job_id);
                                startActivity(intent);
                                finish();

                                break;

                            case "picked_up":
                                tv_status_1.setVisibility(View.GONE);
                                tv_status_2.setVisibility(View.VISIBLE);
                                tv_status_3.setVisibility(View.GONE);

                                img_accepted.setImageResource(R.mipmap.status_empty);
                                img_out_for_delivery.setImageResource(R.mipmap.status_empty);
                                img_picked_up.setImageResource(R.mipmap.status_filled);

                                break;

                            case "on_the_way":

                                tv_status_1.setVisibility(View.GONE);
                                tv_status_2.setVisibility(View.GONE);
                                tv_status_3.setVisibility(View.VISIBLE);

                                img_accepted.setImageResource(R.mipmap.status_empty);
                                img_out_for_delivery.setImageResource(R.mipmap.status_filled);
                                img_picked_up.setImageResource(R.mipmap.status_empty);


                                break;

                            case "accepted":

                                tv_status_1.setVisibility(View.VISIBLE);
                                tv_status_2.setVisibility(View.GONE);
                                tv_status_3.setVisibility(View.GONE);

                                img_accepted.setImageResource(R.mipmap.status_filled);
                                img_out_for_delivery.setImageResource(R.mipmap.status_empty);
                                img_picked_up.setImageResource(R.mipmap.status_empty);


                                break;
                        }
                    }else{

                        Toasty.error(JobStatus.this, message, Toast.LENGTH_LONG, true).show();




                    }
                    pd.dismiss();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "job_status_update Error: " + error.getMessage());
                Toast.makeText(JobStatus.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();


                params.put("job_id",job_id);
                params.put("job_status",item);
                params.put("id",globalClass.getId());


                Log.d(TAG, "getParams: " + params);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));


    }

    private void job_status_url() {
        pd.show();
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebserviceUrl.job_status, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "url_hit: " + WebserviceUrl.job_status_update);
                Log.d(TAG, "Response: " + response);

                Gson gson = new Gson();

                try {


                    JsonObject jobj = gson.fromJson(response, JsonObject.class);
                    //JSONObject jObject = new JSONObject(String.valueOf(content));
                    String status = jobj.get("status").toString().replaceAll("\"", "");
                    String message = jobj.get("message").toString().replaceAll("\"", "");
                    String job_status = jobj.get("job_status").toString().replaceAll("\"", "");


                    Log.d("TAG", "status :\t" + status);
                    Log.d("TAG", "message :\t" + message);

                    if (status.equals("1")){
                        switch (job_status) {
                            case "delivered":

                                Intent intent = new Intent(getApplicationContext(), Delivered.class);
                                intent.putExtra("customer_id", customer_id);
                                intent.putExtra("id", job_id);
                                startActivity(intent);
                                finish();

                                break;

                            case "picked_up":
                                tv_status_1.setVisibility(View.GONE);
                                tv_status_2.setVisibility(View.VISIBLE);
                                tv_status_3.setVisibility(View.GONE);

                                img_accepted.setImageResource(R.mipmap.status_empty);
                                img_out_for_delivery.setImageResource(R.mipmap.status_empty);
                                img_picked_up.setImageResource(R.mipmap.status_filled);

                                break;

                            case "on_the_way":

                                tv_status_1.setVisibility(View.GONE);
                                tv_status_2.setVisibility(View.GONE);
                                tv_status_3.setVisibility(View.VISIBLE);

                                img_accepted.setImageResource(R.mipmap.status_empty);
                                img_out_for_delivery.setImageResource(R.mipmap.status_filled);
                                img_picked_up.setImageResource(R.mipmap.status_empty);


                                break;

                            case "accepted":

                                tv_status_1.setVisibility(View.VISIBLE);
                                tv_status_2.setVisibility(View.GONE);
                                tv_status_3.setVisibility(View.GONE);

                                img_accepted.setImageResource(R.mipmap.status_filled);
                                img_out_for_delivery.setImageResource(R.mipmap.status_empty);
                                img_picked_up.setImageResource(R.mipmap.status_empty);


                                break;
                        }

                    }else{

                        Toasty.error(JobStatus.this, message, Toast.LENGTH_LONG, true).show();




                    }
                    pd.dismiss();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "job_status Error: " + error.getMessage());
                Toast.makeText(JobStatus.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();


                params.put("job_id",job_id);
             //   params.put("job_status",item);
                params.put("id",globalClass.getId());


                Log.d(TAG, "getParams: " + params);
                Log.d(TAG, "url_hit: " + WebserviceUrl.job_status);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));


    }
}
package com.sketch.deliveryboy.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sketch.deliveryboy.Adapter.AdapterCanceled;
import com.sketch.deliveryboy.Adapter.AdapterHistory;
import com.sketch.deliveryboy.R;
import com.sketch.deliveryboy.utils.AppController;
import com.sketch.deliveryboy.utils.GlobalClass;
import com.sketch.deliveryboy.utils.WebserviceUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Frag_Cancel_Job extends Fragment {

    String TAG = "canceled";
    RecyclerView rv_cancel;
    ArrayList<HashMap<String,String>> arr_cancel;
    GlobalClass globalClass;
    ProgressDialog pd;
    AdapterCanceled adapterCanceled;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_cancel, container, false);

        globalClass = (GlobalClass) getActivity().getApplicationContext();

        pd=new ProgressDialog(getActivity());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.loading));

        arr_cancel = new ArrayList<>();


        rv_cancel = view.findViewById(R.id.rv_cancel);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rv_cancel.setLayoutManager(mLayoutManager);


        job_cancel_url();
        return view;
    }

    private void job_cancel_url() {
        // Tag used to cancel the request


        String tag_string_req = "req_login";

        pd.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebserviceUrl.job_cancel, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                //   Log.d(TAG, "url_hit: " + WebserviceUrl.order_history);
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

                        arr_cancel.clear();

                        JsonArray jsonArray =jobj.getAsJsonArray("job_info");
                        for(int i=0; i<jsonArray.size();i++) {

                            JsonObject jObject = (JsonObject) jsonArray.get(i);

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
                            String mobile = jObject.get("mobile").toString().replaceAll("\"", "");


                            HashMap<String, String> map_ser = new HashMap<String, String>();
                            map_ser.put("id", id);
                            map_ser.put("customer_id", customer_id);
                            map_ser.put("deliveryboy_id", deliveryboy_id);
                            map_ser.put("name", name);
                            map_ser.put("otp", otp);
                            map_ser.put("product_type", product_type);
                            map_ser.put("product_quantity", product_quantity);
                            map_ser.put("image", image);
                            map_ser.put("shop_name", shop_name);
                            map_ser.put("instruction", instruction);
                            map_ser.put("product_price", product_price);
                            map_ser.put("latitute", latitute);
                            map_ser.put("longitude", longitude);
                            map_ser.put("address", address);
                            map_ser.put("shop_address", shop_address);
                            map_ser.put("shop_latitude", shop_latitude);
                            map_ser.put("shop_longitude", shop_longitude);
                            map_ser.put("status1", status1);
                            map_ser.put("job_status", job_status);
                            map_ser.put("order_placed_on", order_placed_on);
                            map_ser.put("fname", fname);
                            map_ser.put("mobile", mobile);

                            arr_cancel.add(map_ser);


                        }

                        Log.d(TAG, "onResponse: p_arr:  "+arr_cancel);
                        adapterCanceled = new AdapterCanceled(getActivity(), arr_cancel);
                        rv_cancel.setAdapter(adapterCanceled);
                        adapterCanceled.notifyDataSetChanged();

                        pd.dismiss();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "order_history Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",globalClass.getId());

                Log.d(TAG, "getParams: " + params);
                Log.d(TAG, "url_hit: " + WebserviceUrl.job_cancel);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));


    }
}

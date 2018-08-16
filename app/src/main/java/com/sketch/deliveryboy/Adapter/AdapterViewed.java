package com.sketch.deliveryboy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sketch.deliveryboy.Activity.JobDetailScreen;
import com.sketch.deliveryboy.R;
import com.sketch.deliveryboy.utils.AppController;
import com.sketch.deliveryboy.utils.GlobalClass;
import com.sketch.deliveryboy.utils.WebserviceUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class AdapterViewed extends BaseAdapter {
    String TAG = "adapter_viewed";

    GlobalClass global;
    Context context;
    TextView title,description,addess,view_details,navigation;
    ArrayList<String> order_id;
    LayoutInflater inflater;

      ArrayList<HashMap<String,String>> arr_order_viewed;



    public AdapterViewed(Context c, ArrayList<HashMap<String,String>> arr_order_viewed) {

        context = c;
        this.arr_order_viewed = arr_order_viewed;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        global = (GlobalClass)context.getApplicationContext();


    }

    @Override
    public int getCount() {
        return arr_order_viewed.size();
    }

    @Override
    public Object getItem(int i) {
        return arr_order_viewed.get(i);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View view1=null;

        //   inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view1 == null) {

            view1 = inflater.inflate(R.layout.message_single_row, null, true);

            title = view1.findViewById(R.id.title);
            description = view1.findViewById(R.id.description);
            addess = view1.findViewById(R.id.addess);
            view_details = view1.findViewById(R.id.view_details);
            navigation = view1.findViewById(R.id.navigation);

            title.setText(arr_order_viewed.get(position).get("name"));
            description.setText(arr_order_viewed.get(position).get("instruction"));
            addess.setText(arr_order_viewed.get(position).get("address"));
            //addess.setText(arr_order_viewed.get(position).get("address"));



            view_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    job_viewed_url(arr_order_viewed.get(position).get("id"));
                }
            });

        }else{
            Log.d("TAG", "getView: else");
        }

        return view1;
    }

    private void job_viewed_url(final String id) {
        // Tag used to cancel the request


        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebserviceUrl.job_viewed, new Response.Listener<String>() {


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



                        //    arr_order_all.add(map_ser);

                        Intent intent = new Intent(context, JobDetailScreen.class);
                        intent.putExtra("id",id);
                        intent.putExtra("name",name);
                        intent.putExtra("instruction",instruction);
                        intent.putExtra("address",address);
                        intent.putExtra("shop_address",shop_address);
                        intent.putExtra("from","viewed_adapter");

                        context.startActivity(intent);



                        //  globalClass.dismiss_pd(getActivity());

                    } else {

                        Toasty.error(context, message, Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override

            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "job_viewed Error: " + error.getMessage());
                Toasty.error(context, error.getMessage(), Toast.LENGTH_LONG).show();
                //   globalClass.dismiss_pd(getActivity());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id",global.getId());
                params.put("job_id",id);

                Log.d(TAG, "url_hit: " + WebserviceUrl.job_viewed);
                Log.d(TAG, "getParams: " + params);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));


    }


}
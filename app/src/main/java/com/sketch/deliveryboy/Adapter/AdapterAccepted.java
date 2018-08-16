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
import com.sketch.deliveryboy.Activity.JobStatus;
import com.sketch.deliveryboy.R;
import com.sketch.deliveryboy.utils.AppController;
import com.sketch.deliveryboy.utils.GlobalClass;
import com.sketch.deliveryboy.utils.WebserviceUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class AdapterAccepted extends BaseAdapter {

    String TAG = "adapter_viewed";
    GlobalClass global;

    Context context;
    TextView title,description,addess,view_details,navigation;
    ArrayList<String> order_id;
    LayoutInflater inflater;

      ArrayList<HashMap<String,String>> arr_order_all;



    public AdapterAccepted(Context c, ArrayList<HashMap<String,String>> arr_order_all) {

        context = c;
        this.arr_order_all = arr_order_all;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        global = (GlobalClass)context.getApplicationContext();

    }

    @Override
    public int getCount() {
        return arr_order_all.size();
    }

    @Override
    public Object getItem(int i) {
        return arr_order_all.get(i);
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

            title.setText(arr_order_all.get(position).get("name"));
            description.setText(arr_order_all.get(position).get("instruction"));
            addess.setText(arr_order_all.get(position).get("address"));
            //addess.setText(arr_order_all.get(position).get("address"));



            view_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JobDetailScreen.class);


                    intent.putExtra("id",arr_order_all.get(position).get("id"));
                    intent.putExtra("customer_id",arr_order_all.get(position).get("customer_id"));
                    intent.putExtra("name",arr_order_all.get(position).get("name"));
                    intent.putExtra("instruction",arr_order_all.get(position).get("instruction"));
                    intent.putExtra("address",arr_order_all.get(position).get("address"));
                    intent.putExtra("shop_address",arr_order_all.get(position).get("shop_address"));
                    intent.putExtra("job_status",arr_order_all.get(position).get("job_status"));
                    intent.putExtra("product_price",arr_order_all.get(position).get("product_price"));
                    intent.putExtra("from","accepted_adapter");

                    context.startActivity(intent);

                }
            });


        }else{
            Log.d("TAG", "getView: else");
        }

        return view1;
    }




}
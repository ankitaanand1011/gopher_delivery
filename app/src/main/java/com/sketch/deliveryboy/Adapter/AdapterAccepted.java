package com.sketch.deliveryboy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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



public class AdapterAccepted extends BaseAdapter {

    String TAG = "adapter_viewed";
    GlobalClass global;

    Context context;
    TextView title,description,addess,view_details,navigation,tv_is_money_requested;
    LinearLayout rl_view_detail;
    ArrayList<String> order_id;
    LayoutInflater inflater;

      ArrayList<HashMap<String,String>> arr_order_accepted;
    double o_lat, o_lng, d_lat, d_lng;
    String s_lat,s_lng,lat,lng;
    String is_request_for_money;

    public AdapterAccepted(Context c, ArrayList<HashMap<String,String>> arr_order_accepted) {

        context = c;
        this.arr_order_accepted = arr_order_accepted;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        global = (GlobalClass)context.getApplicationContext();

    }

    @Override
    public int getCount() {
        return arr_order_accepted.size();
    }

    @Override
    public Object getItem(int i) {
        return arr_order_accepted.get(i);
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
            tv_is_money_requested = view1.findViewById(R.id.tv_is_money_requested);
            rl_view_detail = view1.findViewById(R.id.rl_view_detail);

            title.setText(arr_order_accepted.get(position).get("name"));
            description.setText(arr_order_accepted.get(position).get("instruction"));
            addess.setText(arr_order_accepted.get(position).get("address"));
            //addess.setText(arr_order_accepted.get(position).get("address"));

            rl_view_detail.setVisibility(View.VISIBLE);


            is_request_for_money = arr_order_accepted.get(position).get("is_request_for_money");
            switch (is_request_for_money) {
                case "1":
                    tv_is_money_requested.setVisibility(View.VISIBLE);
                    tv_is_money_requested.setText("Money Request Pending");
                    tv_is_money_requested.setTextColor(context.getResources().getColor(R.color.red));
                    rl_view_detail.setVisibility(View.GONE);

                    break;
                case "2":
                    tv_is_money_requested.setVisibility(View.VISIBLE);
                    tv_is_money_requested.setText("Money Request Accepted by " + arr_order_accepted.get(position).get("fname"));
                    tv_is_money_requested.setTextColor(context.getResources().getColor(R.color.green));
                    rl_view_detail.setVisibility(View.VISIBLE);
                    break;

                default:
                    tv_is_money_requested.setVisibility(View.GONE);
                  //  tv_is_money_requested.setText("");
                    rl_view_detail.setVisibility(View.VISIBLE);

                    break;
            }

            s_lat = arr_order_accepted.get(position).get("shop_latitude");
            s_lng = arr_order_accepted.get(position).get("shop_longitude");
            lat = arr_order_accepted.get(position).get("latitute");
            lng = arr_order_accepted.get(position).get("longitude");

        /*    o_lat = Double.parseDouble(s_lat);
            o_lng = Double.parseDouble(s_lng);
            d_lat = Double.parseDouble(lat);
            d_lng = Double.parseDouble(lng);
*/

            navigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isGoogleMapsInstalled()) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,

                                Uri.parse("http://maps.google.com/maps?" +
                                        "saddr="+s_lat+","+s_lng+"&daddr="+lat+","+lng));
                        context.startActivity(intent);
                    }else{
                      /*  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
                        context.startActivity(intent);

                        //Finish the activity so they can't circumvent the check
                        ((Activity)context).finish();*/

                    }
                }
            });



            view_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JobDetailScreen.class);


                    intent.putExtra("id",arr_order_accepted.get(position).get("id"));
                    intent.putExtra("customer_id",arr_order_accepted.get(position).get("customer_id"));
                    intent.putExtra("name",arr_order_accepted.get(position).get("name"));
                    intent.putExtra("instruction",arr_order_accepted.get(position).get("instruction"));
                    intent.putExtra("address",arr_order_accepted.get(position).get("address"));
                    intent.putExtra("shop_address",arr_order_accepted.get(position).get("shop_address"));
                    intent.putExtra("job_status",arr_order_accepted.get(position).get("job_status"));
                    intent.putExtra("product_price",arr_order_accepted.get(position).get("product_price"));
                    intent.putExtra("product_quantity",arr_order_accepted.get(position).get("product_quantity"));
                    intent.putExtra("gopher_earned",arr_order_accepted.get(position).get("gopher_earned"));

                    intent.putExtra("o_lat",s_lat);
                    intent.putExtra("o_lng",s_lng);
                    intent.putExtra("d_lat",lat);
                    intent.putExtra("d_lng",lng);
                    intent.putExtra("from","accepted_adapter");

                    context.startActivity(intent);

                }
            });


        }else{
            Log.d("TAG", "getView: else");
        }

        return view1;
    }

    public boolean isGoogleMapsInstalled()
    {
        try
        {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }



}
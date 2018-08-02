package com.sketch.deliveryboy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sketch.deliveryboy.Activity.JobDetailScreen;
import com.sketch.deliveryboy.R;

import java.util.ArrayList;
import java.util.HashMap;


public class AdapterViewed extends BaseAdapter {
    Context context;
    TextView title,description,addess,view_details,navigation;
    //Global_Class global_class;
    ArrayList<String> order_id;
    LayoutInflater inflater;

      ArrayList<HashMap<String,String>> arr_order_viewed;



    public AdapterViewed(Context c, ArrayList<HashMap<String,String>> arr_order_viewed) {

        context = c;
        this.arr_order_viewed = arr_order_viewed;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /*global = (GlobalClass)mContext.getApplicationContext();
        orderDetailORMS=orderDetailsORM;*/


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
                    Intent intent = new Intent(context, JobDetailScreen.class);
                    //intent.putExtra("id",list_names.get(position).get("id"));
                    //Log.d("tag", "onClick: "+list_names.get(position).get("id"));
                    context.startActivity(intent);
                }
            });

        }else{
            Log.d("TAG", "getView: else");
        }

        return view1;
    }



}
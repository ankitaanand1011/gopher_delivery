package com.sketch.deliveryboy.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sketch.deliveryboy.R;
import com.sketch.deliveryboy.utils.GlobalClass;

import java.util.ArrayList;
import java.util.HashMap;


public class AdapterJobList extends RecyclerView.Adapter<AdapterJobList.ViewHolder> {

    Context mContext;
    ArrayList<HashMap<String,String>> arr_order_job_list;

    LayoutInflater inflater;
    ImageLoader loader;

    String TAG = "adapterapplycoupon";

    DisplayImageOptions defaultOptions;
    GlobalClass globalClass;
    String is_request_for_money,status;

    public AdapterJobList(Context c, ArrayList<HashMap<String,String>> arr_order_job_list ) {
        this.inflater = LayoutInflater.from(c);
        mContext = c;
        this.arr_order_job_list = arr_order_job_list;

        globalClass = (GlobalClass)mContext.getApplicationContext();

        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                //  .showImageOnLoading(R.mipmap.loading_black128px)
                //  .showImageForEmptyUri(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.no_image)
                //  .showImageOnFail(R.mipmap.img_failed)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext.getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();

       // loader.destroy();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.joblist_single_row, parent, false);
        return new AdapterJobList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.tv_order_id.setText(arr_order_job_list.get(position).get("id"));
        holder.tv_customer_name.setText(arr_order_job_list.get(position).get("fname"));
        holder.tv_product_name.setText(arr_order_job_list.get(position).get("name"));
        holder.tv_order_date.setText(arr_order_job_list.get(position).get("order_placed_on"));
        holder.tv_price.setText("$ "+arr_order_job_list.get(position).get("product_price"));
        holder.tv_qty.setText(arr_order_job_list.get(position).get("product_quantity"));
        holder.tv_address.setText(arr_order_job_list.get(position).get("address"));

        is_request_for_money = arr_order_job_list.get(position).get("is_request_for_money");

        switch (is_request_for_money) {
            case "1":

                holder.tv_status.setText("Money Request Pending");
                holder.tv_status.setTypeface(holder.tv_status.getTypeface(), Typeface.BOLD);
                holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.red));
                break;
            case "2":

                holder.tv_status.setText("Money Request Accepted by " + arr_order_job_list.get(position).get("fname"));
                holder.tv_status.setTypeface(holder.tv_status.getTypeface(), Typeface.BOLD);
                holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.green));

                break;
            default:

                status = arr_order_job_list.get(position).get("status1");

                switch (status) {
                    case "order_placed":
                        holder.tv_status.setText("Placed");
                        holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.orange));

                        break;
                    case "order_accepted":
                        holder.tv_status.setText("Accepted");
                        holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.purple));

                        break;
                    case "order_out_for_delivery":
                        holder.tv_status.setText("Out For Delivery");
                        holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.track_blue));

                        break;
                    case "order_completed":
                        holder.tv_status.setText("Completed");
                        holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.green));

                        break;
                    case "order_cancelled":
                        holder.tv_status.setText("Cancelled");
                        holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.red));

                        break;
                    default:
                        holder.tv_status.setText("");
                        break;
                }
                break;
        }

        loader.displayImage(arr_order_job_list.get(position).get("image"), holder.img_product , defaultOptions);

        /*  holder.tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = arr_order_history.get(position).get("id");
                String discount_amount = arr_order_history.get(position).get("discount_amount");
               // verify_coupon(id,discount_amount);
            }
        });*/


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arr_order_job_list.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_order_date,tv_address,tv_qty,tv_price,tv_product_name,tv_customer_name,tv_order_id,tv_status;
        ImageView img_product;

        ViewHolder(View itemView) {
            super(itemView);



            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_customer_name = itemView.findViewById(R.id.tv_customer_name);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_order_date = itemView.findViewById(R.id.tv_order_date);
            img_product = itemView.findViewById(R.id.img_product);
            tv_status = itemView.findViewById(R.id.tv_status);





        }



    }

}


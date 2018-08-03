package com.sketch.deliveryboy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sketch.deliveryboy.R;


public class JobStatus extends AppCompatActivity {
    TextView updatestatus,tv_status_1,tv_status_2,tv_status_3,
            tv_description,tv_addess,tv_price,tv_view_details,tv_request_for_money;
    int value =1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobstatus);

        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_status_1=findViewById(R.id.tv_status_1);
        tv_status_2=findViewById(R.id.tv_status_2);
        tv_status_3=findViewById(R.id.tv_status_3);
        tv_description=findViewById(R.id.tv_description);
        tv_addess=findViewById(R.id.tv_addess);
        tv_price=findViewById(R.id.tv_price);
        updatestatus=findViewById(R.id.update);
        tv_view_details=findViewById(R.id.tv_view_details);
        tv_request_for_money=findViewById(R.id.tv_request_for_money);


        if(getIntent().getStringExtra("job_status").equals("accepted")){
            tv_status_1.setVisibility(View.VISIBLE);
            tv_status_2.setVisibility(View.GONE);
            tv_status_3.setVisibility(View.GONE);
        }else {
            tv_status_1.setVisibility(View.GONE);
            tv_status_2.setVisibility(View.GONE);
            tv_status_3.setVisibility(View.GONE);

        }
        if(value==1){

        }
        tv_description.setText(getIntent().getStringExtra("instruction"));
        tv_addess.setText(getIntent().getStringExtra("address"));
        tv_price.setText(getIntent().getStringExtra("product_price"));
        updatestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Delivered.class);
                startActivity(intent);
            }
        });

        tv_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Product_Detail_Screen.class);
                intent.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });

        tv_request_for_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Request_Money_Screen.class);
                intent.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });

    }
}
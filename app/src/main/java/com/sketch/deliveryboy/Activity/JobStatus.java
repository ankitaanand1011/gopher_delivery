package com.sketch.deliveryboy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sketch.deliveryboy.R;


public class JobStatus extends AppCompatActivity {
    TextView updatestatus,tv_status_1,tv_status_2,tv_status_3,tv_description,tv_addess,tv_price;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jobstatus);

        tv_status_1=findViewById(R.id.tv_status_1);
        tv_status_2=findViewById(R.id.tv_status_2);
        tv_status_3=findViewById(R.id.tv_status_3);
        tv_description=findViewById(R.id.tv_description);
        tv_addess=findViewById(R.id.tv_addess);
        tv_price=findViewById(R.id.tv_price);
        updatestatus=findViewById(R.id.update);
        updatestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Delivered.class);
                startActivity(intent);
            }
        });

    }
}
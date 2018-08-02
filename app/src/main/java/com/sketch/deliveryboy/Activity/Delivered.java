package com.sketch.deliveryboy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sketch.deliveryboy.R;



public class Delivered extends AppCompatActivity {
    TextView submit;
    ImageView profileImg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivered);
     //   profileImg=findViewById(R.id.profile);

        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewst=new Intent(getApplicationContext(), userrating.class);
                startActivity(viewst);
            }
        });
        /*profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Delivered.this, ProfileScreen.class);
                startActivity(intent);
            }
        });

*/
    }
}
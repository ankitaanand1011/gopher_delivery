package com.sketch.deliveryboy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.sketch.deliveryboy.R;

/**
 * Created by developer on 30/6/18.
 */

public class userrating extends AppCompatActivity {
    ImageView profileImg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userrating);
    /*    profileImg=findViewById(R.id.profile);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userrating.this, ProfileScreen.class);
                startActivity(intent);
            }
        });
*/
    }
}
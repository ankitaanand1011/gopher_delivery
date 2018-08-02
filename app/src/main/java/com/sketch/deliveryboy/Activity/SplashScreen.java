package com.sketch.deliveryboy.Activity;

/**
 * Created by developer on 7/5/18.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.sketch.deliveryboy.R;
import com.sketch.deliveryboy.utils.GlobalClass;
import com.sketch.deliveryboy.utils.Shared_Preference;


public class  SplashScreen extends AppCompatActivity {
    GlobalClass globalClass;
    ProgressDialog pd;
    Shared_Preference prefrence;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        globalClass = (GlobalClass) getApplicationContext();
        prefrence = new Shared_Preference(SplashScreen.this);
        prefrence.loadPrefrence();



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if(globalClass.getLogin_status().equals(true)){
                    Intent intent = new Intent(SplashScreen.this, DrawerActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);
    }


}

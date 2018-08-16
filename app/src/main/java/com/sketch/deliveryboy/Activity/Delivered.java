package com.sketch.deliveryboy.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sketch.deliveryboy.R;
import com.sketch.deliveryboy.utils.GlobalClass;
import com.sketch.deliveryboy.utils.Shared_Preference;
import com.sketch.deliveryboy.utils.WebserviceUrl;
import com.williamww.silkysignature.views.SignaturePad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import es.dmoral.toasty.Toasty;


public class Delivered extends AppCompatActivity {
    String TAG = "delivered";
    TextView submit;
    private SignaturePad mSignaturePad;
    ImageView img_clear;
    GlobalClass globalClass;
    Shared_Preference prefrence;
    ProgressDialog pd;
    File photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivered);

        globalClass = (GlobalClass)getApplicationContext();
        prefrence = new Shared_Preference(Delivered.this);
        prefrence.loadPrefrence();
        pd=new ProgressDialog(Delivered.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Loading...");


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Delivered.this,
                    android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Delivered.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
            }else {
                if (checkForPermission(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 124)) {
                    //     cameraPreview.addView(preview);
                }
            }
        }




        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        submit=findViewById(R.id.submit);
        img_clear=findViewById(R.id.img_clear);
        mSignaturePad =  findViewById(R.id.signature_pad);


        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                //  Toast.makeText(MainActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                submit.setEnabled(true);
                img_clear.setEnabled(true);

            }

            @Override
            public void onClear() {
                submit.setEnabled(false);
                img_clear.setEnabled(false);

            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_signature_url();
            }
        });

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });

    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream os;
        try {
            os = new FileOutputStream(photo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {



        File folder = new File(Environment.getExternalStorageDirectory() + "/Delivery_boy_Gopher");
        boolean success = false;
        if (!folder.exists()) {
            folder.mkdir();

        } else {
            // Do something else on failure

        }
        try {
             photo = new File(folder, String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);

            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return success;
    }

    public void add_signature_url(){

        pd.show();

        String url = WebserviceUrl.add_signature;
        AsyncHttpClient cl = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("id",globalClass.getId());
        params.put("customer_id",getIntent().getStringExtra("customer_id"));
        params.put("job_id",getIntent().getStringExtra("id"));


        try{
            params.put("signature_image", photo);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        Log.d(TAG , "URL "+url);
        Log.d(TAG , "params "+params.toString());


        int DEFAULT_TIMEOUT = 15 * 1000;
        cl.setMaxRetriesAndTimeout(3 , DEFAULT_TIMEOUT);
        cl.post(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {
                    Log.d(TAG, "add_signature- " + response.toString());
                    try {

                        //JSONObject result = response.getJSONObject("result");

                        int status = response.getInt("status");
                        String message = response.getString("message");

                        if (status == 1) {

                            // Log.d(TAG, "name: "+name)



                            Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                            addJpgSignatureToGallery(signatureBitmap);
                            mSignaturePad.clear();
                            Intent viewst=new Intent(getApplicationContext(), UserRating.class);
                            viewst.putExtra("c_id",getIntent().getStringExtra("customer_id"));

                            startActivity(viewst);



                            Toasty.success(Delivered.this, message, Toast.LENGTH_SHORT, true).show();

                        } else{


                            Toasty.warning(Delivered.this, message, Toast.LENGTH_SHORT, true).show();
                        }

                        pd.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                // pd.dismiss();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {

                Log.d(TAG+"Failed: ", ""+statusCode);
                Log.d(TAG+"Error : ", "" + throwable);
                Log.e(TAG, String.valueOf(throwable instanceof ConnectTimeoutException));
                Toasty.error(Delivered.this,"Something went wrong.",Toast.LENGTH_LONG).show();
                pd.dismiss();

                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);

    }

    public boolean checkForPermission(final String[] permissions, final int permRequestCode) {

        final List<String> permissionsNeeded = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            final String perm = permissions[i];
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {

                    Log.d("permisssion", "not granted");


                    if (shouldShowRequestPermissionRationale(permissions[i])) {

                        Log.d("if", "if");
                        permissionsNeeded.add(perm);

                    } else {
                        // add the request.
                        Log.d("else", "else");
                        permissionsNeeded.add(perm);
                    }

                }
            }
        }

        if (permissionsNeeded.size() > 0) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // go ahead and request permissions
                requestPermissions(permissionsNeeded.toArray(new String[permissionsNeeded.size()]), permRequestCode);
            }
            return false;
        } else {
            // no permission need to be asked so all good...we have them all.
            return true;
        }

    }
}
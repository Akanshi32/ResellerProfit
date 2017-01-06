package com.vansh.resellerprofit.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.utility.Consts;
import com.vansh.resellerprofit.utility.DialogUtil;
import com.vansh.resellerprofit.utility.Preferences;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class CompanyProfile extends AppCompatActivity {
    EditText ed1,ed2,ed3,ed4;
    Button b1,b2;
    ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);


        imageview = (ImageView)findViewById(R.id.complogo);
        ed1=(EditText)findViewById(R.id.companyname);
        ed2=(EditText)findViewById(R.id.comaddress);
        ed3=(EditText)findViewById(R.id.vattin);
        ed4=(EditText)findViewById(R.id.csttin);

        b1=(Button)findViewById(R.id.comsave);
        b2=(Button)findViewById(R.id.addimage);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ed1.getText().toString().isEmpty() || ed2.getText().toString().isEmpty() || ed3.getText().toString().isEmpty() || ed4.getText().toString().isEmpty()) {
                    DialogUtil.createDialog("Please Fill All the information!", CompanyProfile.this, new DialogUtil.OnPositiveButtonClick() {
                        @Override
                        public void onClick() {
                            finish();
                        }
                    });


                }
                else {
                    String name = ed1.getText().toString();
                    String addr = ed2.getText().toString();
                    String vat = ed3.getText().toString();
                    String cst = ed4.getText().toString();

                    Preferences.setPrefs(Consts.Name, name, CompanyProfile.this);
                    Preferences.setPrefs(Consts.Address, addr, CompanyProfile.this);
                    Preferences.setPrefs(Consts.VAT, vat, CompanyProfile.this);
                    Preferences.setPrefs(Consts.CST, cst, CompanyProfile.this);

                    startActivity(new Intent(CompanyProfile.this, MainActivity.class));
                    Toast.makeText(CompanyProfile.this, "Saved!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code



            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
              case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageview.setImageURI(selectedImage);
                    String path=getRealPathFromURI(this,selectedImage);

                    Preferences.setPrefs(Consts.PATH,path,CompanyProfile.this);


                }
                break;
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



}

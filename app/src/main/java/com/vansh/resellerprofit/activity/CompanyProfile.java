package com.vansh.resellerprofit.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vansh.resellerprofit.R;

public class CompanyProfile extends AppCompatActivity {
    EditText ed1,ed2,ed3,ed4;
    Button b1;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "companyName";
    public static final String Address = "companyAddress";
    public static final String VAT = "VAT";
    public static final String CST = "CST";

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        ed1=(EditText)findViewById(R.id.companyname);
        ed2=(EditText)findViewById(R.id.comaddress);
        ed3=(EditText)findViewById(R.id.vattin);
        ed4=(EditText)findViewById(R.id.csttin);

        b1=(Button)findViewById(R.id.comsave);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name  = ed1.getText().toString();
                String addr  = ed2.getText().toString();
                String vat  = ed3.getText().toString();
                String cst  = ed4.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(Name, name);
                editor.putString(Address, addr);
                editor.putString(VAT, vat);
                editor.putString(CST, cst);
                editor.apply();

                Toast.makeText(CompanyProfile.this,"Saved", Toast.LENGTH_LONG).show();
                Intent it = new Intent(CompanyProfile.this,MainActivity.class);
                startActivity(it);

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

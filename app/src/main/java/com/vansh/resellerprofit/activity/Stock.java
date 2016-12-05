package com.vansh.resellerprofit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vansh.resellerprofit.R;

public class Stock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


}

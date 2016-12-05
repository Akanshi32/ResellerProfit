package com.vansh.resellerprofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Sold extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sold);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

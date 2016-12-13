package com.vansh.resellerprofit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vansh.resellerprofit.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BillActivity extends AppCompatActivity {

    TextView date,time;
    Button bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        date = (TextView) findViewById(R.id.date_bill);
        time = (TextView) findViewById(R.id.time_bill);
        bill = (Button) findViewById(R.id.generate);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        date.setText(currentDate);

        Time now = new Time();
        now.setToNow();
        String sTime = now.format("%H:%M:%S");
        time.setText(sTime);

        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
package com.vansh.resellerprofit.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.model.ProfitResponse;
import com.vansh.resellerprofit.rest.ApiClient;
import com.vansh.resellerprofit.rest.ApiInterface;
import com.vansh.resellerprofit.utility.DialogUtil;


import net.danlew.android.joda.JodaTimeAndroid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarActivity extends AppCompatActivity {


    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Toolbar toptoolbar;
    String date1;
    String month1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        JodaTimeAndroid.init(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        toptoolbar = (Toolbar) findViewById(R.id.top_toolbar);
        setSupportActionBar(toptoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toptoolbar.setTitle("");
        final ApiInterface apiService1 =
                ApiClient.getClient(this).create(ApiInterface.class);

        Button month = (Button) findViewById(R.id.month);
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date dat=new Date();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                final String nowAsIS = df.format(dat);


                Call<ProfitResponse> call = apiService1.profitResponse(nowAsIS);

                call.enqueue(new Callback<ProfitResponse>() {
                    @Override
                    public void onResponse(Call<ProfitResponse> call, final Response<ProfitResponse> response) {

                        DialogUtil.createDialog("Your profit for the current Month: " + response.body().getProfit().toString(), CalendarActivity.this, new DialogUtil.OnPositiveButtonClick() {
                            @Override
                            public void onClick() {
                            }
                        });


                    }


                    @Override
                    public void onFailure(Call<ProfitResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("Error", t.toString());
                    }
                });
            }
        });

        final CompactCalendarView compactCalendarView = (CompactCalendarView) toolbar.findViewById(R.id.compactcalendar_view);
        final TextView calendarTV = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        final ApiInterface apiService =
                ApiClient.getClient(this).create(ApiInterface.class);




        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                dialog.show();
                final String da=dateClicked.toString();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                df.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                final String nowAsISO = df.format(dateClicked);

                Call<ProfitResponse> call = apiService.profitDaily(nowAsISO);
                Call<ProfitResponse> call1 = apiService.profitResponse(nowAsISO);

                call.enqueue(new Callback<ProfitResponse>() {
                    @Override
                    public void onResponse(Call<ProfitResponse> call, final Response<ProfitResponse> response) {
                        date1=response.body().getProfit().toString();


                    }


                    @Override
                    public void onFailure(Call<ProfitResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("Error", t.toString());
                    }
                });

                call1.enqueue(new Callback<ProfitResponse>() {
                    @Override
                    public void onResponse(Call<ProfitResponse> call, final Response<ProfitResponse> response) {


                        month1=response.body().getProfit().toString();
                        dialog.hide();
                        compactCalendarView.showCalendarWithAnimation();
                        DialogUtil.createDialog("Selected Date: "+da+"\nProfit For This DATE is: " + date1+ "\nProfit For MONTH Belonging To This Date: "+ month1 , CalendarActivity.this, new DialogUtil.OnPositiveButtonClick() {
                            @Override
                            public void onClick() {
                            }
                        });

                    }


                    @Override
                    public void onFailure(Call<ProfitResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("Error", t.toString());
                    }
                });

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                calendarTV.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });
        compactCalendarView.showCalendar();
        calendarTV.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));


    }
    public void onBackPressed() {
        finish();
    }
}
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
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.model.ProfitResponse;
import com.vansh.resellerprofit.rest.ApiClient;
import com.vansh.resellerprofit.rest.ApiInterface;


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
    TextView pro;
    private Toolbar toptoolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        pro=(TextView)findViewById(R.id.pro);
        JodaTimeAndroid.init(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        pro=(TextView)findViewById(R.id.pro);

        toptoolbar = (Toolbar) findViewById(R.id.top_toolbar);
        setSupportActionBar(toptoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toptoolbar.setTitle("");



        final CompactCalendarView compactCalendarView = (CompactCalendarView) toolbar.findViewById(R.id.compactcalendar_view);
        final TextView calendarTV = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        final ApiInterface apiService =
                ApiClient.getClient(this).create(ApiInterface.class);

        calendarTV.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

               final String ook=dateClicked.toString();
                Log.i("ds",ook);
                dialog.show();

                TimeZone tz = TimeZone.getTimeZone("UTC");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
                df.setTimeZone(tz);
                String nowAsISO = df.format(dateClicked);

                Call<ProfitResponse> call = apiService.profitDaily(nowAsISO);

                call.enqueue(new Callback<ProfitResponse>() {
                    @Override
                    public void onResponse(Call<ProfitResponse> call, final Response<ProfitResponse> response) {

                                dialog.hide();
                                pro.setText(response.body().getProfit().toString());


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

    }


}
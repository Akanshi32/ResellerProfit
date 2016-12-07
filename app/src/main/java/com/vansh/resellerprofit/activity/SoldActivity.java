package com.vansh.resellerprofit.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.adapter.SoldViewAdapter;
import com.vansh.resellerprofit.adapter.StockAdapter;
import com.vansh.resellerprofit.model.*;
import com.vansh.resellerprofit.rest.ApiClient;
import com.vansh.resellerprofit.rest.ApiInterface;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SoldActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sold);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sold_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiInterface apiService =
                ApiClient.getClient(this).create(ApiInterface.class);

        Call<SoldViewResponse> call = apiService.soldViewResonse(new HashMap<String, String>());
        call.enqueue(new Callback<SoldViewResponse>() {
            @Override
            public void onResponse(Call<SoldViewResponse> call, Response<SoldViewResponse> response) {
                List<com.vansh.resellerprofit.model.Sold> sold = response.body().getSold();
                recyclerView.setAdapter(new SoldViewAdapter(sold, R.layout.list_item_sold, getApplicationContext()));
            }




            @Override
            public void onFailure(Call<SoldViewResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("Error", t.toString());
            }
        });

    }
    }


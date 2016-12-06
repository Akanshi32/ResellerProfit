package com.vansh.resellerprofit.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.adapter.StockAdapter;
import com.vansh.resellerprofit.model.StockResponse;
import com.vansh.resellerprofit.rest.ApiClient;
import com.vansh.resellerprofit.rest.ApiInterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Stock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.stock_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiInterface apiService =
                ApiClient.getClient(this).create(ApiInterface.class);

        Call<StockResponse> call = apiService.stockResponse(new HashMap<String, String>());
        call.enqueue(new Callback<StockResponse>() {
            @Override
            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {
                int statusCode = response.code();
                List<com.vansh.resellerprofit.model.Stock> stock = response.body().getStock();

                recyclerView.setAdapter(new StockAdapter(stock, R.layout.list_item_stock, getApplicationContext()));
                            }

            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("Error", t.toString());
            }
        });

    }


}

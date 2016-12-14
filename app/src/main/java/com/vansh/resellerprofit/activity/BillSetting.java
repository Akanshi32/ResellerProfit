package com.vansh.resellerprofit.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.adapter.SpinnerSoldAdapter;
import com.vansh.resellerprofit.adapter.SpinnerStockAdapter;
import com.vansh.resellerprofit.model.*;
import com.vansh.resellerprofit.rest.ApiClient;
import com.vansh.resellerprofit.rest.ApiInterface;
import com.vansh.resellerprofit.utility.DialogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillSetting extends AppCompatActivity {

    final ArrayList<String> list = new ArrayList<String>();

    @Bind(R.id.sub)
    Button sub;
    @Bind(R.id.selectt)
    Button select;
    @Bind(R.id.company_name)
    EditText comname;
    @Bind(R.id.c_address)
    EditText address;
    @Bind(R.id.cst_tin)
    EditText cst;
    @Bind(R.id.vat_tin)
    EditText vattin;
    @Bind(R.id.product_i)
    EditText sold;
    static int TIME_OUT = 2000;
    int width,height;
    ArrayAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        final BillSettingRequest soldRequest = new BillSettingRequest();
        final ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                openDialogSelect();

                Intent intent = getIntent();
                String stringData= intent.getStringExtra("name");
                sold.setText(stringData);
                list.add(sold.getText().toString());
            }
        });


        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                soldRequest.setCompanyName(comname.getText().toString());
                soldRequest.setAddress(address.getText().toString());
                soldRequest.setCstTin(cst.getText().toString());
                soldRequest.setVatTin(vattin.getText().toString());
                soldRequest.setSoldId(list);


                Call<BillSettingRequest> call = apiInterface.billsetrequest(soldRequest);

                final ProgressDialog dialog = new ProgressDialog(BillSetting.this);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("Congrats! You Just Sold An Item.");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                call.enqueue(new Callback<BillSettingRequest>() {
                    @Override
                    public void onResponse(Call<BillSettingRequest> call, Response<BillSettingRequest> response) {

                        dialog.hide();

                    }

                    @Override
                    public void onFailure(Call<BillSettingRequest> call, Throwable t) {
                        dialog.hide();
                        DialogUtil.createDialog("Oops! Please check your internet connection!", BillSetting.this, new DialogUtil.OnPositiveButtonClick() {
                            @Override
                            public void onClick() {
                            }
                        });
                    }
                });


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(BillSetting.this, BillActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, TIME_OUT);

            }
        });

    }

    public void openDialogSelect(){

        Dialog dialog3 = new Dialog(this);
        dialog3.setContentView(R.layout.dialog_sold);

        dialog3.setTitle("Sold Items");
        dialog3.getWindow().setLayout(width*90/100, height*100/100);
        dialog3.show();


        final RecyclerView recyclerView = (RecyclerView) dialog3.findViewById(R.id.sold_recycler_view_spinner);
        LinearLayoutManager layoutManager = new LinearLayoutManager(dialog3.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        ApiInterface apiService =
                ApiClient.getClient(this).create(ApiInterface.class);

        Call<SoldViewResponse> call = apiService.soldViewResonse(new HashMap<String, String>());



        call.enqueue(new Callback<SoldViewResponse>() {
            @Override
            public void onResponse(Call<SoldViewResponse> call, Response<SoldViewResponse> response) {
                int statusCode = response.code();
                List<com.vansh.resellerprofit.model.Sold> sold = response.body().getSold();
                recyclerView.setAdapter(new SpinnerSoldAdapter(sold, R.layout.list_item_stock_spinner, getApplicationContext()));
            }


            @Override
            public void onFailure(Call<SoldViewResponse> call, Throwable t) {
                DialogUtil.createDialog("Oops! Please check your internet connection!", BillSetting.this, new DialogUtil.OnPositiveButtonClick() {
                    @Override
                    public void onClick() {
                    }
                });

                // Log error here since request failed
                Log.e("Error", t.toString());
            }
        });


    }

    }


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
import android.view.WindowManager;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillSetting extends AppCompatActivity {

    final ArrayList<String> list = new ArrayList<String>();
    String prodid;
    @Bind(R.id.sub)
    Button sub;
    @Bind(R.id.add)
    Button add;
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
    TextView sold;

    @Bind(R.id.vat)
    EditText vat;
    @Bind(R.id.custname)
    EditText custnam;
    @Bind(R.id.custmob)
    EditText custmob;

    @Bind(R.id.custadd)
    EditText custadd;
    @Bind(R.id.custemail)
    EditText custemail;
    @Bind(R.id.paymethod)
    EditText paymethod;


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


            }
        });

        Intent intent = getIntent();
        String stringData= intent.getStringExtra("name");
        sold.setText(stringData);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.add(sold.getText().toString());
                sold.setText("");
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

                Integer myNum = Integer.parseInt(vat.getText().toString());
                soldRequest.setVatPercent(myNum);
                Long myNum1 = Long.parseLong(custmob.getText().toString());
                soldRequest.setCustomerMobile(myNum1);

                soldRequest.setCustomerName(custnam.getText().toString());
                soldRequest.setCustomerEmail(custemail.getText().toString());
                soldRequest.setAddress(custadd.getText().toString());

                soldRequest.setPaymentMethod(paymethod.getText().toString());


                Call<BillSettingResponse> call = apiInterface.billsetrequest(soldRequest);

                final ProgressDialog dialog = new ProgressDialog(BillSetting.this);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("Congrats! You Just Sold An Item.");
                dialog.setIndeterminate(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                call.enqueue(new Callback<BillSettingResponse>() {
                    @Override
                    public void onResponse(Call<BillSettingResponse> call, Response<BillSettingResponse> response) {

                        dialog.hide();
                        Data dat=new Data();
                        Data data=response.body().getData();
                        prodid=data.getId().toString();
                        Log.i("DFfdfd",prodid);
                        {Intent it = new Intent(BillSetting.this, PdfCreateActivity.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            it.putExtra("prodid",prodid);
                            startActivity(it);}

                    }

                    @Override
                    public void onFailure(Call<BillSettingResponse> call, Throwable t) {
                        dialog.hide();
                        DialogUtil.createDialog("Oops! Please check your internet connection!", BillSetting.this, new DialogUtil.OnPositiveButtonClick() {
                            @Override
                            public void onClick() {
                            }
                        });
                    }
                });

            }
        });

    }

    public void openDialogSelect(){

        Dialog dialog3 = new Dialog(this);
        dialog3.setContentView(R.layout.dialog_sold);

        dialog3.setTitle("Sold Items");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog3.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog3.getWindow().setLayout(lp.width, lp.height);
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


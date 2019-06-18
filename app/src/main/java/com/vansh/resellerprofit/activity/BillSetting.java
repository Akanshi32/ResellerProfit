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
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.adapter.SpinnerSoldAdapter;
import com.vansh.resellerprofit.adapter.SpinnerStockAdapter;
import com.vansh.resellerprofit.model.*;
import com.vansh.resellerprofit.rest.ApiClient;
import com.vansh.resellerprofit.rest.ApiInterface;
import com.vansh.resellerprofit.utility.Consts;
import com.vansh.resellerprofit.utility.DialogUtil;
import com.vansh.resellerprofit.utility.Preferences;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillSetting extends AppCompatActivity {

    final ArrayList<String> list = new ArrayList<String>();
    String prodid;
    @BindView(R.id.sub)
    Button sub;
    String cc;
    public String codeFormat,codeContent,c;
    EditText productid;
    EditText imeiresult;

    @BindView(R.id.imei)
    EditText imei;
    @BindView(R.id.selectt)
    Button select;

    @BindView(R.id.product_i)
    TextView sold;

    @BindView(R.id.vat)
    EditText vat;

    @BindView(R.id.imeiscan)
    Button imeiscan;

    @BindView(R.id.custname)
    EditText custnam;
    @BindView(R.id.custmob)
    EditText custmob;

    @BindView(R.id.custemail)
    EditText custemail;
    @BindView(R.id.paymethod)
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

        imei.setText(Preferences.getPrefs(Consts.IMEI,BillSetting.this));
        final String imeiii=imei.getText().toString();
        Preferences.setPrefs(Consts.IMEI,imeiii,BillSetting.this);

        imeiscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cc="imei";
                scanNow(view);
            }
        });


        final BillSettingRequest soldRequest = new BillSettingRequest();
        final ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSelect();

            }
        });

        Intent intent = getIntent();
        String stringData= intent.getStringExtra("id");
        String nameid= intent.getStringExtra("name");
        sold.setText(nameid);

        if (sold.getText().toString().equals(""))
        {sold.setText(Preferences.getPrefs(Consts.SAVEDID,BillSetting.this));
            list.add(Preferences.getPrefs(Consts.IDE,BillSetting.this));
        }
        else
        {list.add(stringData);
            }


        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sold.getText().toString().isEmpty() || vat.getText().toString().isEmpty() || custnam.getText().toString().isEmpty() || custemail.getText().toString().isEmpty()||paymethod.getText().toString().isEmpty()||custmob.getText().toString().isEmpty()||imei.getText().toString().isEmpty()) {
                    DialogUtil.createDialog("Please Fill All the information!", BillSetting.this, new DialogUtil.OnPositiveButtonClick() {
                        @Override
                        public void onClick() {
                            finish();
                        }
                    });


                }
                else {
                    soldRequest.setCompanyName(Preferences.getPrefs(Consts.Name, BillSetting.this));
                    soldRequest.setAddress(Preferences.getPrefs(Consts.Address, BillSetting.this));
                    soldRequest.setCstTin(Preferences.getPrefs(Consts.CST, BillSetting.this));
                    soldRequest.setVatTin(Preferences.getPrefs(Consts.VAT, BillSetting.this));
                    soldRequest.setSoldId(list);

                    Integer myNum = Integer.parseInt(vat.getText().toString());
                    soldRequest.setVatPercent(myNum);
                    Long myNum1 = Long.parseLong(custmob.getText().toString());
                    soldRequest.setCustomerMobile(myNum1);

                    soldRequest.setCustomerName(custnam.getText().toString());
                    soldRequest.setCustomerEmail(custemail.getText().toString());
                    soldRequest.setPaymentMethod(paymethod.getText().toString());


                    Call<BillSettingResponse> call = apiInterface.billsetrequest(soldRequest);

                    final ProgressDialog dialog = new ProgressDialog(BillSetting.this);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setMessage("Creating Bill...");
                    dialog.setIndeterminate(true);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    call.enqueue(new Callback<BillSettingResponse>() {
                        @Override
                        public void onResponse(Call<BillSettingResponse> call, Response<BillSettingResponse> response) {

                            dialog.hide();
                            Data data = response.body().getData();
                            prodid = data.getId().toString();


                            {
                                Intent it = new Intent(BillSetting.this, PdfCreateActivity.class);
                                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                it.putExtra("prodid", prodid);
                                startActivity(it);
                            }

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
            }
        });

    }

    public void openDialogSelect(){

        Dialog dialog3 = new Dialog(this,R.style.Dialog);
        dialog3.setContentView(R.layout.dialog_sold);

        dialog3.setTitle("\nSold Items");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog3.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog3.getWindow().setLayout(lp.width, lp.height);
        dialog3.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        dialog3.show();


        final RecyclerView recyclerView = (RecyclerView) dialog3.findViewById(R.id.sold_recycler_view_spinner);
        LinearLayoutManager layoutManager = new LinearLayoutManager(dialog3.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
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


    public void scanNow(View view){

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt(this.getString(R.string.scan_bar_code));
        integrator.setResultDisplayDuration(0);
        integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result


            // display it on screen
            if(cc.equals("imei"))
            {   codeContent = scanningResult.getContents();
                codeFormat = scanningResult.getFormatName();
                imei.setText(codeContent);}



        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}


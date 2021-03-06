package com.vansh.resellerprofit.activity;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scwang.wave.MultiWaveHeader;
import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.adapter.SpinnerStockAdapter;
import com.vansh.resellerprofit.model.Data;
import com.vansh.resellerprofit.model.Message;
import com.vansh.resellerprofit.model.ProfitResponse;
import com.vansh.resellerprofit.model.SoldRequest;
import com.vansh.resellerprofit.model.SoldResponse;
import com.vansh.resellerprofit.model.StockRequest;
import com.vansh.resellerprofit.model.StockResponse;
import com.vansh.resellerprofit.rest.ApiClient;
import com.vansh.resellerprofit.rest.ApiInterface;
import com.vansh.resellerprofit.utility.Consts;
import com.vansh.resellerprofit.utility.DialogUtil;
import com.vansh.resellerprofit.utility.Preferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressDialog mProgressDialog;

    String ok;
    String stringQuan;
    String stringData;
    String stringid;
    ProgressDialog dialog;

    int foo;
    int goo;
    String cc="skf";
    String imeii;
    String savedid;
    @BindView(R.id.stock)
    EditText _stock;
    @BindView(R.id.imei)
    EditText imei;
    @BindView(R.id.sellingprice)
    EditText _sellingprice;
    @BindView(R.id.select)
    Button _selectbutt;
    @BindView(R.id.add)
    TextView _add;
    @BindView(R.id.imeiscan)
    Button imeiscan;
    @BindView(R.id.selected)
    TextView _selected;
    /*@BindView(R.id.profit_tv)
    TextView _profit;
    @BindView(R.id.show)
     Button showprofit*/;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("dd - MM - yyyy", Locale.getDefault());


    static int TIME_OUT=2000;
    int width,height;
    Button cancel,submit;
    ImageView addButton;
    public String codeFormat,codeContent,c;
    EditText productid;
    EditText imeiresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA,Manifest.permission.SEND_SMS};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;
        width = size.x;



        _selectbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSelect();
            }
        });


        Intent intent = getIntent();
        stringData= intent.getStringExtra("name");
        stringQuan= intent.getStringExtra("quan");
        _selected.setText(stringData);

       imeiscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cc="imei";
                scanNow(view);
            }
        });






        final SoldRequest soldRequest = new SoldRequest();
        final ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);


        _add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                imeii = imei.getText().toString();
                savedid = _selected.getText().toString();

                Preferences.setPrefs(Consts.IMEI, imeii, MainActivity.this);
                Preferences.setPrefs(Consts.SAVEDID, savedid, MainActivity.this);

                if (imei.getText().toString().isEmpty() || _selected.getText().toString().isEmpty() || _sellingprice.getText().toString().isEmpty() || _stock.getText().toString().isEmpty()) {
                    DialogUtil.createDialog("Please Fill All the information!", MainActivity.this, new DialogUtil.OnPositiveButtonClick() {
                        @Override
                        public void onClick() {
                            finish();
                        }
                    });


                } else {
                    foo = Integer.parseInt(_stock.getText().toString());
                    goo = Integer.parseInt(stringQuan);


                    if (foo > goo) {
                        DialogUtil.createDialog("You Don't Have Enough Stock", MainActivity.this, new DialogUtil.OnPositiveButtonClick() {
                            @Override
                            public void onClick() {
                                finish();
                            }
                        });
                    } else {
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        soldRequest.setItemId(_selected.getText().toString());

                        String text1 = _stock.getText().toString();
                        int number = Integer.parseInt(text1);
                        soldRequest.setQuantity(number);

                        soldRequest.setSellingPrice(_sellingprice.getText().toString());


                        Call<SoldResponse> call = apiInterface.Sold(soldRequest);

                        try {
                            dialog = new ProgressDialog(MainActivity.this);
                            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            dialog.setMessage("Congrats! You Just Sold An Item.");
                            dialog.setIndeterminate(true);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        call.enqueue(new Callback<SoldResponse>() {
                            @Override
                            public void onResponse(Call<SoldResponse> call, Response<SoldResponse> response) {


                                Message message = response.body().getMessage();
                                stringid = message.getId().toString();
                                Preferences.setPrefs(Consts.IDE, stringid, MainActivity.this);


                                dialog.hide();


                                {
                                    Intent i = new Intent(MainActivity.this, BillSetting.class);
                                    startActivity(i);
                                    finish();
                                }

                            }

                            @Override
                            public void onFailure(Call<SoldResponse> call, Throwable t) {
                                dialog.hide();
                                DialogUtil.createDialog("Oops! Please check your internet connection!", MainActivity.this, new DialogUtil.OnPositiveButtonClick() {
                                    @Override
                                    public void onClick() {
                                    }
                                });
                            }
                        });

                    }
                }
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e57373")));


        // Setup drawer view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        LinearLayout header = (LinearLayout) headerview.findViewById(R.id.header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(it);
            }
        });




    }


    public void openDialogSelect(){
        final Dialog dialog = new Dialog(this,R.style.Dialog);
        dialog.setContentView(R.layout.dialog_productid);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(lp.width, lp.height);
        dialog.show();


        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.stock_recycler_view_spinner);
        LinearLayoutManager layoutManager = new LinearLayoutManager(dialog.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

            ApiInterface apiService =
                ApiClient.getClient(this).create(ApiInterface.class);

        Call<StockResponse> call = apiService.stockResponse(new HashMap<String, String>());

        final ProgressDialog dialog1 = new ProgressDialog(MainActivity.this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setMessage("Select The Item From Your Stock");
        dialog1.setIndeterminate(true);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.show();

        call.enqueue(new Callback<StockResponse>() {
            @Override
            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {
                int statusCode = response.code();
                List<com.vansh.resellerprofit.model.Stock> stock = response.body().getStock();
                recyclerView.setAdapter(new SpinnerStockAdapter(stock, R.layout.list_item_stock_spinner, getApplicationContext()));
                dialog1.hide();
                 }


            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {
                dialog1.hide();
                DialogUtil.createDialog("Oops! Please check your internet connection!", MainActivity.this, new DialogUtil.OnPositiveButtonClick() {
                    @Override
                    public void onClick() {
                    }
                });

                // Log error here since request failed
                Log.e("Error", t.toString());
            }
        });


    }


    public void openDialog(){
        final Dialog dialog = new Dialog(this,R.style.Dialog);
        dialog.setContentView(R.layout.dialog);

        Button cancel=(Button) dialog.findViewById(R.id.dialog_cancel);
        Button submit=(Button) dialog.findViewById(R.id.dialog_submit);
        Button scan=(Button) dialog.findViewById(R.id.btn_scan_now);
        productid=(EditText) dialog.findViewById(R.id.productid);

        final EditText stock=(EditText) dialog.findViewById(R.id.stock);
        final EditText costprice=(EditText) dialog.findViewById(R.id.costprice);
        final EditText invoiceno=(EditText) dialog.findViewById(R.id.invoiceno);
        final EditText vendorname=(EditText) dialog.findViewById(R.id.vendorid);


        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(lp.width, lp.height);
        dialog.show();

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cc="pro";
                scanNow(v);
            }
        });



        final Button btDate = (Button) dialog.findViewById(R.id.purchase_date);

        final java.util.Calendar myCalendar = java.util.Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(java.util.Calendar.YEAR, year);
                myCalendar.set(java.util.Calendar.MONTH, monthOfYear);
                myCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                btDate.setText(dateFormatForMonth.format(myCalendar.getTime()));
            }

        };

        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(MainActivity.this, dateDialog, myCalendar
                        .get(java.util.Calendar.YEAR), myCalendar.get(java.util.Calendar.MONTH),
                        myCalendar.get(java.util.Calendar.DAY_OF_MONTH)).show();
            }
        });

        final StockRequest stockRequest = new StockRequest();
        final ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productid.getText().toString().isEmpty() || costprice.getText().toString().isEmpty() || stock.getText().toString().isEmpty()) {
                    DialogUtil.createDialog("Please Fill All the information!", MainActivity.this, new DialogUtil.OnPositiveButtonClick() {
                        @Override
                        public void onClick() {
                            finish();
                        }
                    });

                }
                else {
                    stockRequest.setItemId(productid.getText().toString());
                    stockRequest.setInvoiceNo(invoiceno.getText().toString());
                    stockRequest.setVendorName(vendorname.getText().toString());
                    String text = stock.getText().toString();
                    int number = Integer.parseInt(text);
                    stockRequest.setStock(number);
                    stockRequest.setCostPrice(costprice.getText().toString());
                    stockRequest.setPurchaseDate(ISO8601Utils.format(myCalendar.getTime()));


                    Call<StockRequest> call = apiInterface.Stock(stockRequest);

                    final ProgressDialog dialog2 = new ProgressDialog(MainActivity.this);
                    dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog2.setMessage("Adding the item to your inventory");
                    dialog2.setIndeterminate(true);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.show();


                    call.enqueue(new Callback<StockRequest>() {
                        @Override
                        public void onResponse(Call<StockRequest> call, Response<StockRequest> response) {
                            dialog2.hide();


                        }

                        @Override
                        public void onFailure(Call<StockRequest> call, Throwable t) {
                            dialog2.hide();
                            DialogUtil.createDialog("Oops! Please check your internet connection!", MainActivity.this, new DialogUtil.OnPositiveButtonClick() {
                                @Override
                                public void onClick() {
                                }
                            });
                        }
                    });

                }

                dialog.dismiss();
            }
        });




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
           }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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
            if(cc.equals("pro"))
            {   codeContent = scanningResult.getContents();
                codeFormat = scanningResult.getFormatName();
                productid.setText(codeContent);
                }



        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d("CDA", "onBackPressed Called");
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent it = new Intent(MainActivity.this, CompanyProfile.class);
            startActivity(it);
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();


        if (id == R.id.nav_home) {

            // Handle the camera action

        } else if (id == R.id.nav_stock) {
            Intent it = new Intent(MainActivity.this, Stock.class);
            startActivity(it);
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        } else if (id == R.id.nav_sold) {
            Intent it = new Intent(MainActivity.this, SoldActivity.class);
            startActivity(it);
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        } else if (id == R.id.nav_calendar) {
            Intent it = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(it);
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        } else if (id == R.id.nav_contact) {
            Intent it = new Intent(MainActivity.this, ContactUs.class);
            startActivity(it);
        }

        else if (id == R.id.nav_pdf) {
            Intent it = new Intent(MainActivity.this, BillSetting.class);
            startActivity(it);
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


}

package com.vansh.resellerprofit.activity;

import android.Manifest;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.adapter.StockAdapter;
import com.vansh.resellerprofit.model.SoldRequest;
import com.vansh.resellerprofit.model.StockRequest;
import com.vansh.resellerprofit.model.StockResponse;
import com.vansh.resellerprofit.rest.ApiClient;
import com.vansh.resellerprofit.rest.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.stock)
    EditText _stock;
    @Bind(R.id.sellingprice)
    EditText _sellingprice;
    @Bind(R.id.spinner)
    Spinner _spinner;
    @Bind(R.id.add)
    Button _add;


    ArrayAdapter<String> adapterBusinessType;


    int width,height;
    Button cancel,submit;
    ImageView addButton;
    public String codeFormat,codeContent,c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA};

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



        //TO CHECK THIS PART

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.stock_recycler_view_spinner);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiInterface apiService =
                ApiClient.getClient(this).create(ApiInterface.class);

        Call<StockResponse> call = apiService.stockResponse(new HashMap<String, String>());
        call.enqueue(new Callback<StockResponse>() {
            @Override
            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {
                int statusCode = response.code();
                List<com.vansh.resellerprofit.model.Stock> stock = response.body().getStock();

                String[] stockArr = new String[stock.size()];
                stockArr = stock.toArray(stockArr);
                String[] arraySpinner =stockArr;

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, arraySpinner);
                _spinner.setAdapter(adapter);

          /*      recyclerView.setAdapter(new StockAdapter(stock, R.layout.list_item_stock, getApplicationContext()));

                adapterBusinessType = new ArrayAdapter<String>(this,
                        R.layout.list_item_stock_spinner,);
                adapterBusinessType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _spinner.setAdapter(adapterBusinessType);*/


            }

            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("Error", t.toString());
            }
        });


        _spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Item number: " + position, Toast.LENGTH_LONG).show();
            }
        });








        //TILL HERE






        final SoldRequest soldRequest = new SoldRequest();
        final ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);



        _add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                soldRequest.setItemId(_spinner.getSelectedItem().toString());
                String text1 = _stock.getText().toString();
                int number = Integer.parseInt(text1);
                soldRequest.setQuantity(number);

                soldRequest.setSellingPrice(_sellingprice.getText().toString());


                Call<SoldRequest> call = apiInterface.Sold(soldRequest);


                call.enqueue(new Callback<SoldRequest>() {
                    @Override
                    public void onResponse(Call<SoldRequest> call, Response<SoldRequest> response) {



                    }

                    @Override
                    public void onFailure(Call<SoldRequest> call, Throwable t) {
                    }
                });


                Snackbar.make(view, "Congrats on selling the product", Snackbar.LENGTH_LONG);

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Enter Product Details", Snackbar.LENGTH_LONG);
                openDialog();
            }
        });
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#d50000")));


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




    public void openDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        Button cancel=(Button) dialog.findViewById(R.id.dialog_cancel);
        Button submit=(Button) dialog.findViewById(R.id.dialog_submit);
        Button scan=(Button) dialog.findViewById(R.id.btn_scan_now);
        final EditText productid=(EditText) dialog.findViewById(R.id.productid);

        final EditText stock=(EditText) dialog.findViewById(R.id.stock);
        final EditText costprice=(EditText) dialog.findViewById(R.id.costprice);


        dialog.setTitle("Product Details");
        dialog.getWindow().setLayout(width*90/100, height*65/100);
        dialog.show();
        productid.setText(codeContent);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                scanNow(v);
            }
        });

        final StockRequest stockRequest = new StockRequest();
        final ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                stockRequest.setItemId(productid.getText().toString());
                String text = stock.getText().toString();
                int number = Integer.parseInt(text);
                stockRequest.setStock(number);
                stockRequest.setCostPrice(costprice.getText().toString());


                Call<StockRequest> call = apiInterface.Stock(stockRequest);


                call.enqueue(new Callback<StockRequest>() {
                    @Override
                    public void onResponse(Call<StockRequest> call, Response<StockRequest> response) {



                    }

                    @Override
                    public void onFailure(Call<StockRequest> call, Throwable t) {
                    }
                });


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
            codeContent = scanningResult.getContents();
            codeFormat = scanningResult.getFormatName();

            // display it on screen
            openDialog();



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
            super.onBackPressed();
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

        } else if (id == R.id.nav_sold) {
            Intent it = new Intent(MainActivity.this, Sold.class);
            startActivity(it);

        } else if (id == R.id.nav_calendar) {
            Intent it = new Intent(MainActivity.this, Calendar.class);
            startActivity(it);

        } else if (id == R.id.nav_faq) {
            Intent it = new Intent(MainActivity.this, FaqActivity.class);
            startActivity(it);

        } else if (id == R.id.nav_contact) {
            Intent it = new Intent(MainActivity.this, ContactUs.class);
            startActivity(it);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

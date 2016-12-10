package com.vansh.resellerprofit.rest;

import com.vansh.resellerprofit.model.Expense;
import com.vansh.resellerprofit.model.ExpenseRequest;
import com.vansh.resellerprofit.model.LoginResponse;
import com.vansh.resellerprofit.model.ProfitResponse;
import com.vansh.resellerprofit.model.SignUpRequest;
import com.vansh.resellerprofit.model.SoldRequest;
import com.vansh.resellerprofit.model.SoldViewResponse;
import com.vansh.resellerprofit.model.StockRequest;
import com.vansh.resellerprofit.model.StockResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface ApiInterface {
    @POST("/signup")
    Call<SignUpRequest> SignUp(@Body SignUpRequest signUpRequest);

    @GET("/login")
    Call<LoginResponse> getResponse(@QueryMap Map<String, String> params);

    @POST("/stock")
    Call<StockRequest> Stock(@Body StockRequest stockRequest);

    @GET("/stock")
    Call<StockResponse> stockResponse(@QueryMap Map<String, String> params);

     @POST("/sold")
    Call<SoldRequest> Sold(@Body SoldRequest soldRequest);


    @GET("/sold")
    Call<SoldViewResponse> soldViewResonse(@QueryMap Map<String, String> params);

    @POST("/expense")
    Call<ExpenseRequest> expense(@Body ExpenseRequest expenseRequest);


    @GET("/expense")
    Call<Expense> expenseresponse(@QueryMap Map<String, String> params);



    @GET("/profit/month")
    Call<ProfitResponse> profitResponse(@Query("date") String date);


}
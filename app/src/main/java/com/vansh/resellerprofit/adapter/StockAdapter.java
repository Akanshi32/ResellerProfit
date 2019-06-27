package com.vansh.resellerprofit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vansh.resellerprofit.model.Stock;

import java.util.List;

import com.vansh.resellerprofit.R;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.MovieViewHolder> {

    private List<Stock> stock;
    private int rowLayout;
    private Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout stockLayout;
        TextView productid;
        TextView stock;
        TextView costprice;
        TextView rating;


        public MovieViewHolder(View v) {
            super(v);
            stockLayout = (RelativeLayout) v.findViewById(R.id.stock_layout);
            productid = (TextView) v.findViewById(R.id.productid);
            stock = (TextView) v.findViewById(R.id.stock);
            costprice = (TextView) v.findViewById(R.id.costprice);
            rating = (TextView) v.findViewById(R.id.rating);
        }
    }

    public StockAdapter(List<Stock> stock, int rowLayout, Context context) {
        this.stock = stock;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public StockAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.productid.setText(stock.get(position).getItemId());
        holder.stock.setText("₹" + stock.get(position).getCostPrice().toString()+ "/unit");
        holder.rating.setText(stock.get(position).getStock().toString());


    }


    @Override
    public int getItemCount() {
        return stock.size();
    }
}
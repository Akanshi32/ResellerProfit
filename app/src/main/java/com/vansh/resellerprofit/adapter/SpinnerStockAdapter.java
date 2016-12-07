package com.vansh.resellerprofit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.model.Stock;

import java.util.List;

public class SpinnerStockAdapter extends RecyclerView.Adapter<SpinnerStockAdapter.MovieViewHolder> {

    private List<Stock> stock;
    private int rowLayout;
    private Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout stockLayout;
        TextView productid;



        public MovieViewHolder(View v) {
            super(v);
            stockLayout = (LinearLayout) v.findViewById(R.id.stock_layout1);
            productid = (TextView) v.findViewById(R.id.text15);

        }
    }

    public SpinnerStockAdapter(List<Stock> stock, int rowLayout, Context context) {
        this.stock = stock;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public SpinnerStockAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.productid.setText(stock.get(position).getItemId());



    }


    @Override
    public int getItemCount() {
        return stock.size();
    }
}
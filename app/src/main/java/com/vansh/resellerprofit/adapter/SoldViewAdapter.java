package com.vansh.resellerprofit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.model.Sold;
import com.vansh.resellerprofit.model.Stock;

import java.util.List;

public class SoldViewAdapter extends RecyclerView.Adapter<SoldViewAdapter.MovieViewHolder> {

    private List<Sold> sold;
    private int rowLayout;
    private Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout stockLayout;
        TextView productid;
        TextView stock;
        TextView costprice;
        TextView rating;


        public MovieViewHolder(View v) {
            super(v);
            stockLayout = (LinearLayout) v.findViewById(R.id.stock_layout);
            productid = (TextView) v.findViewById(R.id.productid);
            stock = (TextView) v.findViewById(R.id.stock);
            costprice = (TextView) v.findViewById(R.id.costprice);
            rating = (TextView) v.findViewById(R.id.rating);
        }
    }

    public SoldViewAdapter(List<Sold> sold, int rowLayout, Context context) {
        this.sold = sold;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public SoldViewAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.productid.setText(sold.get(position).getItemId());
        holder.stock.setText("₹" + sold.get(position).getSellingPrice().toString()+ "/unit");
        holder.rating.setText(sold.get(position).getQuantity().toString());


    }


    @Override
    public int getItemCount() {
        return sold.size();
    }
}
package com.vansh.resellerprofit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.activity.MainActivity;
import com.vansh.resellerprofit.model.Bill;
import com.vansh.resellerprofit.model.SoldId;

import java.util.List;

public class SoldIdAdapter extends RecyclerView.Adapter<SoldIdAdapter.MovieViewHolder> {

    private List<SoldId> stock;
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

    public SoldIdAdapter(List<SoldId> stock, int rowLayout, Context context) {
        this.stock = stock;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public SoldIdAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        holder.productid.setText(stock.get(position).getSellingPrice());


        holder.stockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name",stock.get(position).getItemId());
                context.startActivity(intent);

            }
        });




    }


    @Override
    public int getItemCount() {
        return stock.size();
    }
}
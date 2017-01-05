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
import com.vansh.resellerprofit.activity.BillSetting;
import com.vansh.resellerprofit.activity.MainActivity;
import com.vansh.resellerprofit.model.Sold;
import com.vansh.resellerprofit.model.Stock;

import java.util.ArrayList;
import java.util.List;

public class SpinnerSoldAdapter extends RecyclerView.Adapter<SpinnerSoldAdapter.MovieViewHolder> {
    private List<Sold> sold;
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

    public SpinnerSoldAdapter(List<Sold> sold, int rowLayout, Context context) {
        this.sold = sold;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public SpinnerSoldAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new SpinnerSoldAdapter.MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final SpinnerSoldAdapter.MovieViewHolder holder, final int position) {
        holder.productid.setText(sold.get(position).getItemId());
        holder.stockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, BillSetting.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name",sold.get(position).getItemId());
                intent.putExtra("id",sold.get(position).getId());
                context.startActivity(intent);

                /*ArrayList<String> myList = new ArrayList<String>();
                myList.add(sold.get(position).getId());

                intent.putExtra("mylist", myList);*/

            }
        });




    }


    @Override
    public int getItemCount() {
        return sold.size();
    }
}
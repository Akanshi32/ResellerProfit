package com.vansh.resellerprofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockResponse {
    @SerializedName("stock")
    @Expose
    private List<Stock> stock = null;

    /**
     *
     * @return
     * The stock
     */
    public List<Stock> getStock() {
        return stock;
    }

    /**
     *
     * @param stock
     * The stock
     */
    public void setStock(List<Stock> stock) {
        this.stock = stock;
    }

}
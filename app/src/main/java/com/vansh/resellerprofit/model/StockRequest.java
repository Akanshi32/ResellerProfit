package com.vansh.resellerprofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockRequest {

    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("stock")
    @Expose
    private Integer stock;
    @SerializedName("cost_price")
    @Expose
    private String costPrice;

    /**
     *
     * @return
     * The itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     *
     * @param itemId
     * The item_id
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     *
     * @return
     * The stock
     */
    public Integer getStock() {
        return stock;
    }

    /**
     *
     * @param stock
     * The stock
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     *
     * @return
     * The costPrice
     */
    public String getCostPrice() {
        return costPrice;
    }

    /**
     *
     * @param costPrice
     * The cost_price
     */
    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

}
package com.vansh.resellerprofit.model;

/**
 * Created by vansh on 06-Dec-16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stock {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("stock")
    @Expose
    private Integer stock;
    @SerializedName("cost_price")
    @Expose
    private Integer costPrice;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("created_on")
    @Expose
    private String createdOn;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The _id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

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
    public Integer getCostPrice() {
        return costPrice;
    }

    /**
     *
     * @param costPrice
     * The cost_price
     */
    public void setCostPrice(Integer costPrice) {
        this.costPrice = costPrice;
    }

    /**
     *
     * @return
     * The v
     */
    public Integer getV() {
        return v;
    }

    /**
     *
     * @param v
     * The __v
     */
    public void setV(Integer v) {
        this.v = v;
    }

    /**
     *
     * @return
     * The createdOn
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     *
     * @param createdOn
     * The created_on
     */
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

}
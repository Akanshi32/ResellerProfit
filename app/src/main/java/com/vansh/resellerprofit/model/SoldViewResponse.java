package com.vansh.resellerprofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vansh.resellerprofit.activity.SoldActivity;

import java.util.List;

public class SoldViewResponse {

    @SerializedName("sold")
    @Expose
    private List<Sold> sold = null;

    /**
     *
     * @return
     * The sold
     */
    public List<Sold> getSold() {
        return sold;
    }

    /**
     *
     * @param sold
     * The sold
     */
    public void setSold(List<Sold> sold) {
        this.sold = sold;
    }

}
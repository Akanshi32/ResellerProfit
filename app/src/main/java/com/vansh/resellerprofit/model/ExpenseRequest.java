package com.vansh.resellerprofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseRequest {

    @SerializedName("expense")
    @Expose
    private Integer expense;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("created_on")
    @Expose
    private String createdOn;

    /**
     *
     * @return
     * The expense
     */
    public Integer getExpense() {
        return expense;
    }

    /**
     *
     * @param expense
     * The expense
     */
    public void setExpense(Integer expense) {
        this.expense = expense;
    }

    /**
     *
     * @return
     * The desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     *
     * @param desc
     * The desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
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
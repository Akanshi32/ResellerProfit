package com.vansh.resellerprofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfitResponse {

    @SerializedName("stock")
    @Expose
    private List<Stock> stock = null;
    @SerializedName("sold")
    @Expose
    private List<Sold> sold = null;
    @SerializedName("expense")
    @Expose
    private List<Expense> expense = null;
    @SerializedName("profit")
    @Expose
    private Integer profit;

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

    /**
     *
     * @return
     * The expense
     */
    public List<Expense> getExpense() {
        return expense;
    }

    /**
     *
     * @param expense
     * The expense
     */
    public void setExpense(List<Expense> expense) {
        this.expense = expense;
    }

    /**
     *
     * @return
     * The profit
     */
    public Integer getProfit() {
        return profit;
    }

    /**
     *
     * @param profit
     * The profit
     */
    public void setProfit(Integer profit) {
        this.profit = profit;
    }

}
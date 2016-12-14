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
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("purchase_date")
    @Expose
    private String purchaseDate;
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;

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

    /**
     *
     * @return
     * The invoiceNo
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     *
     * @param invoiceNo
     * The invoice_no
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    /**
     *
     * @return
     * The purchaseDate
     */
    public String getPurchaseDate() {
        return purchaseDate;
    }

    /**
     *
     * @param purchaseDate
     * The purchase_date
     */
    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     *
     * @return
     * The vendorName
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     *
     * @param vendorName
     * The vendor_name
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

}
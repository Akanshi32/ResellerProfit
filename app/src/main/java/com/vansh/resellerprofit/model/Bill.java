package com.vansh.resellerprofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Bill {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("vat_percent")
    @Expose
    private Float vatPercent;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("vat_tin")
    @Expose
    private String vatTin;
    @SerializedName("cst_tin")
    @Expose
    private String cstTin;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_mobile")
    @Expose
    private String customerMobile;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("sold_id")
    @Expose
    private List<SoldId> soldId = null;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("selling_price")
    @Expose
    private Integer sellingPrice;
    @SerializedName("vat_amount")
    @Expose
    private Float vatAmount;
    @SerializedName("total_sp")
    @Expose
    private Float totalSp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Float getVatPercent() {
        return vatPercent;
    }

    public void setVatPercent(Float vatPercent) {
        this.vatPercent = vatPercent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVatTin() {
        return vatTin;
    }

    public void setVatTin(String vatTin) {
        this.vatTin = vatTin;
    }

    public String getCstTin() {
        return cstTin;
    }

    public void setCstTin(String cstTin) {
        this.cstTin = cstTin;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<SoldId> getSoldId() {
        return soldId;
    }

    public void setSoldId(List<SoldId> soldId) {
        this.soldId = soldId;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Integer getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Integer sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Float getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(Float vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Float getTotalSp() {
        return totalSp;
    }

    public void setTotalSp(Float totalSp) {
        this.totalSp = totalSp;
    }

}
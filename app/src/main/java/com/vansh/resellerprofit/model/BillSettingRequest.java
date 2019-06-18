package com.vansh.resellerprofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillSettingRequest {


    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("vat_percent")
    @Expose
    private Integer vatPercent;
    @SerializedName("sold_id")
    @Expose
    private List<String> soldId = null;
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
    private Long customerMobile;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getVatPercent() {
        return vatPercent;
    }

    public void setVatPercent(Integer vatPercent) {
        this.vatPercent = vatPercent;
    }

    public List<String> getSoldId() {
        return soldId;
    }

    public void setSoldId(List<String> soldId) {
        this.soldId = soldId;
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

    public Long getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(Long customerMobile) {
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

}
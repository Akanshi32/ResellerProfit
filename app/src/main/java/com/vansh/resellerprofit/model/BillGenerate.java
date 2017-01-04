package com.vansh.resellerprofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillGenerate {


    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("bills")
    @Expose
    private Bill bills = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Bill getBills() {
        return bills;
    }

    public void setBills(Bill bills) {
        this.bills = bills;
    }

}
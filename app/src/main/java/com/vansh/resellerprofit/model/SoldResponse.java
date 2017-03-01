package com.vansh.resellerprofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SoldResponse {


    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private Message message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
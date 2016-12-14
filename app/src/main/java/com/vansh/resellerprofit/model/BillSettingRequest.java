package com.vansh.resellerprofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillSettingRequest {


    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("vat_tin")
    @Expose
    private String vatTin;
    @SerializedName("cst_tin")
    @Expose
    private String cstTin;
    @SerializedName("sold_id")
    @Expose
    private List<String> soldId = null;

    /**
     *
     * @return
     * The companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     *
     * @param companyName
     * The company_name
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     *
     * @return
     * The address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     * The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     * The vatTin
     */
    public String getVatTin() {
        return vatTin;
    }

    /**
     *
     * @param vatTin
     * The vat_tin
     */
    public void setVatTin(String vatTin) {
        this.vatTin = vatTin;
    }

    /**
     *
     * @return
     * The cstTin
     */
    public String getCstTin() {
        return cstTin;
    }

    /**
     *
     * @param cstTin
     * The cst_tin
     */
    public void setCstTin(String cstTin) {
        this.cstTin = cstTin;
    }

    /**
     *
     * @return
     * The soldId
     */
    public List<String> getSoldId() {
        return soldId;
    }

    /**
     *
     * @param soldId
     * The sold_id
     */
    public void setSoldId(List<String> soldId) {
        this.soldId = soldId;
    }

}
package com.thirumathikart.customer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaytmModel {
    @SerializedName("MID")
    @Expose
    private String mID;
    @SerializedName("ORDER_ID")
    @Expose
    private String oRDERID;
    @SerializedName("CUST_ID")
    @Expose
    private String cUSTID;
    @SerializedName("MOBILE_NO")
    @Expose
    private String mOBILENO;
    @SerializedName("EMAIL")
    @Expose
    private String eMAIL;
    @SerializedName("CHANNEL_ID")
    @Expose
    private String cHANNELID;
    @SerializedName("TXN_AMOUNT")
    @Expose
    private String tXNAMOUNT;
    @SerializedName("WEBSITE")
    @Expose
    private String wEBSITE;
    @SerializedName("INDUSTRY_TYPE_ID")
    @Expose
    private String iNDUSTRYTYPEID;
    @SerializedName("CALLBACK_URL")
    @Expose
    private String cALLBACKURL;
    @SerializedName("CHECKSUMHASH")
    @Expose
    private String checksum;

    public String getMID() {
        return mID;
    }

    public void setMID(String mID) {
        this.mID = mID;
    }

    public String getORDERID() {
        return oRDERID;
    }

    public void setORDERID(String oRDERID) {
        this.oRDERID = oRDERID;
    }

    public String getCUSTID() {
        return cUSTID;
    }

    public void setCUSTID(String cUSTID) {
        this.cUSTID = cUSTID;
    }

    public String getMOBILENO() {
        return mOBILENO;
    }

    public void setMOBILENO(String mOBILENO) {
        this.mOBILENO = mOBILENO;
    }

    public String getEMAIL() {
        return eMAIL;
    }

    public void setEMAIL(String eMAIL) {
        this.eMAIL = eMAIL;
    }

    public String getCHANNELID() {
        return cHANNELID;
    }

    public void setCHANNELID(String cHANNELID) {
        this.cHANNELID = cHANNELID;
    }

    public String getTXNAMOUNT() {
        return tXNAMOUNT;
    }

    public void setTXNAMOUNT(String tXNAMOUNT) {
        this.tXNAMOUNT = tXNAMOUNT;
    }

    public String getWEBSITE() {
        return wEBSITE;
    }

    public void setWEBSITE(String wEBSITE) {
        this.wEBSITE = wEBSITE;
    }

    public String getINDUSTRYTYPEID() {
        return iNDUSTRYTYPEID;
    }

    public void setINDUSTRYTYPEID(String iNDUSTRYTYPEID) {
        this.iNDUSTRYTYPEID = iNDUSTRYTYPEID;
    }

    public String getCALLBACKURL() {
        return cALLBACKURL;
    }

    public void setCALLBACKURL(String cALLBACKURL) {
        this.cALLBACKURL = cALLBACKURL;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

}

package com.meest.videomvvmmodule.model;

import com.google.gson.annotations.Expose;

public class DiamondPurchaseCashFree {

    @Expose
    private Data data;
    @Expose
    private String message;
    @Expose
    private boolean status;
    @Expose
    private String order_id;
    @Expose
    private String mid;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class Data {

        @Expose
        public Head head;

        @Expose
        public Body body;

        public Head getHead() {
            return head;
        }

        public void setHead(Head head) {
            this.head = head;
        }

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }
    }

    public static class Head {
        @Expose
        public String responseTimestamp;
        @Expose
        public String version;
        @Expose
        public String signature;

        public String getResponseTimestamp() {
            return responseTimestamp;
        }

        public void setResponseTimestamp(String responseTimestamp) {
            this.responseTimestamp = responseTimestamp;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

    public static class Body {
        @Expose
        public String txnToken;
        @Expose
        public Boolean isPromoCodeValid;
        @Expose
        public Boolean authenticated;
        @Expose
        public ResultInfo resultInfo;

        public String getTxnToken() {
            return txnToken;
        }

        public void setTxnToken(String txnToken) {
            this.txnToken = txnToken;
        }

        public Boolean getPromoCodeValid() {
            return isPromoCodeValid;
        }

        public void setPromoCodeValid(Boolean promoCodeValid) {
            isPromoCodeValid = promoCodeValid;
        }

        public Boolean getAuthenticated() {
            return authenticated;
        }

        public void setAuthenticated(Boolean authenticated) {
            this.authenticated = authenticated;
        }

        public ResultInfo getResultInfo() {
            return resultInfo;
        }

        public void setResultInfo(ResultInfo resultInfo) {
            this.resultInfo = resultInfo;
        }
    }

    public static class ResultInfo {
        @Expose
        public String resultStatus;
        @Expose
        public String resultCode;
        @Expose
        public String resultMsg;

        public String getResultStatus() {
            return resultStatus;
        }

        public void setResultStatus(String resultStatus) {
            this.resultStatus = resultStatus;
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
}

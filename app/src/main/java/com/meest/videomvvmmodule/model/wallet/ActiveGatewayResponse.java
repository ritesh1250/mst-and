package com.meest.videomvvmmodule.model.wallet;

import com.google.gson.annotations.Expose;

public class ActiveGatewayResponse {

    @Expose
    private String gateway_id, gateway_name, status;

    public String getGateway_id() {
        return gateway_id;
    }

    public void setGateway_id(String gateway_id) {
        this.gateway_id = gateway_id;
    }

    public String getGateway_name() {
        return gateway_name;
    }

    public void setGateway_name(String gateway_name) {
        this.gateway_name = gateway_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

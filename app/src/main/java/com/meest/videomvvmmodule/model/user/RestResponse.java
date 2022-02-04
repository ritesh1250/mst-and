
package com.meest.videomvvmmodule.model.user;


import com.google.gson.annotations.Expose;

public class RestResponse {

    @Expose
    private String message;
    @Expose
    private Boolean status;
    @Expose
    private String client_id;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}

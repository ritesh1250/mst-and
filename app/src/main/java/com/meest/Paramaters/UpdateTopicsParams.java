package com.meest.Paramaters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateTopicsParams {
    @SerializedName("topic")
    @Expose
    private List<String> updatedTopic = null;

    public List<String> getUpdatedTopic() {
        return updatedTopic;
    }

    public void setUpdatedTopic(List<String> updatedTopic) {
        this.updatedTopic = updatedTopic;
    }
}

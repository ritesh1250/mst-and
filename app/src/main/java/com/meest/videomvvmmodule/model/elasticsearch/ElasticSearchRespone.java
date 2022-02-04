package com.meest.videomvvmmodule.model.elasticsearch;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ElasticSearchRespone {

    @SerializedName("took")
    @Expose
    private Integer took;
    @SerializedName("timed_out")
    @Expose
    private Boolean timedOut;

    @SerializedName("hits")
    @Expose
    private Hits hits;

    public Integer getTook() {
        return took;
    }

    public void setTook(Integer took) {
        this.took = took;
    }

    public Boolean getTimedOut() {
        return timedOut;
    }

    public void setTimedOut(Boolean timedOut) {
        this.timedOut = timedOut;
    }


    public Hits getHits() {
        return hits;
    }

    public void setHits(Hits hits) {
        this.hits = hits;
    }

}
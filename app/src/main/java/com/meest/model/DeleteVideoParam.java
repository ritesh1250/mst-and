package com.meest.model;

import java.util.List;

public class DeleteVideoParam {

    private List<String> ids;
    private Boolean svs;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public Boolean getSvs() {
        return svs;
    }

    public void setSvs(Boolean svs) {
        this.svs = svs;
    }
}

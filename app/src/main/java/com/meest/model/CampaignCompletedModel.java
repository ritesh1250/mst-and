package com.meest.model;

public class CampaignCompletedModel {

    String txtAdName,tvDateData,tvStatusCompleted,tvAmt,txt_data,tvHurry,tvAdLineTxt;

    public CampaignCompletedModel() {
    }

    public CampaignCompletedModel(String txtAdName, String tvDateData, String tvStatusCompleted, String tvAmt, String txt_data, String tvHurry, String tvAdLineTxt) {
        this.txtAdName = txtAdName;
        this.tvDateData = tvDateData;
        this.tvStatusCompleted = tvStatusCompleted;
        this.tvAmt = tvAmt;
        this.txt_data = txt_data;
        this.tvHurry = tvHurry;
        this.tvAdLineTxt = tvAdLineTxt;
    }

    public String getTxtAdName() {
        return txtAdName;
    }

    public void setTxtAdName(String txtAdName) {
        this.txtAdName = txtAdName;
    }

    public String getTvDateData() {
        return tvDateData;
    }

    public void setTvDateData(String tvDateData) {
        this.tvDateData = tvDateData;
    }

    public String getTvStatusCompleted() {
        return tvStatusCompleted;
    }

    public void setTvStatusCompleted(String tvStatusCompleted) {
        this.tvStatusCompleted = tvStatusCompleted;
    }

    public String getTvAmt() {
        return tvAmt;
    }

    public void setTvAmt(String tvAmt) {
        this.tvAmt = tvAmt;
    }

    public String getTxt_data() {
        return txt_data;
    }

    public void setTxt_data(String txt_data) {
        this.txt_data = txt_data;
    }

    public String getTvHurry() {
        return tvHurry;
    }

    public void setTvHurry(String tvHurry) {
        this.tvHurry = tvHurry;
    }

    public String getTvAdLineTxt() {
        return tvAdLineTxt;
    }

    public void setTvAdLineTxt(String tvAdLineTxt) {
        this.tvAdLineTxt = tvAdLineTxt;
    }
}

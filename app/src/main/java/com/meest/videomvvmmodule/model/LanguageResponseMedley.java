package com.meest.videomvvmmodule.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class LanguageResponseMedley {
    @Expose
    private String msg;
    @Expose
    private Boolean status;
    @Expose
    private List<Data> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        @Expose
        String languageText;
        @Expose
        String translatedText;

        public String getLanguageText() {
            return languageText;
        }

        public void setLanguageText(String languageText) {
            this.languageText = languageText;
        }

        public String getTranslatedText() {
            return translatedText;
        }

        public void setTranslatedText(String translatedText) {
            this.translatedText = translatedText;
        }
    }
}

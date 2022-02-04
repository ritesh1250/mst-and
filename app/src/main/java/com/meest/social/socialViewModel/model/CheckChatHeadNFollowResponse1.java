package com.meest.social.socialViewModel.model;

public class CheckChatHeadNFollowResponse1 {
    int code;
    Data data;
    boolean success;
    String errorMessage;


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public class Data{
        String data;
        String chatHeadId;
        Boolean isBlock = false;

        public Boolean getBlock() {
            return isBlock;
        }

        public void setBlock(Boolean block) {
            isBlock = block;
        }

        public String getData() {
           return data;
       }

       public void setData(String data) {
           this.data = data;
       }

       public String getChatHeadId() {
           return chatHeadId;
       }

       public void setChatHeadId(String chatHeadId) {
           this.chatHeadId = chatHeadId;
       }
   }
}

package com.meest.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetDocsMediaChatsResponse {

    boolean success;
    String errorMessage;

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {

        @SerializedName("lastWeek")
        @Expose
        private ArrayList<LastWeek> lastWeek = null;

        @SerializedName("lastMonth")
        @Expose
        private ArrayList<LastWeek> lastMonth = null;

        @SerializedName("all")
        @Expose
        private ArrayList<LastWeek> all = null;

        public ArrayList<LastWeek> getLastMonth() {
            return lastMonth;
        }

        public void setLastMonth(ArrayList<LastWeek> lastMonth) {
            this.lastMonth = lastMonth;
        }

        public ArrayList<LastWeek> getAll() {
            return all;
        }

        public void setAll(ArrayList<LastWeek> all) {
            this.all = all;
        }

        public ArrayList<LastWeek> getLastWeek() {
            return lastWeek;
        }

        public void setLastWeek(ArrayList<LastWeek> lastWeek) {
            this.lastWeek = lastWeek;
        }

        public class LastWeek {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("toUserId")
            @Expose
            private String toUserId;
            @SerializedName("msg")
            @Expose
            private String msg;
            @SerializedName("userId")
            @Expose
            private String userId;
            @SerializedName("chatHeadId")
            @Expose
            private String chatHeadId;
            @SerializedName("attachment")
            @Expose
            private Boolean attachment;
            @SerializedName("attachmentType")
            @Expose
            private String attachmentType;
            @SerializedName("fileURL")
            @Expose
            private String fileURL;
            @SerializedName("thumbnail")
            @Expose
            private Object thumbnail;
            @SerializedName("status")
            @Expose
            private Boolean status;

            @SerializedName("read")
            @Expose
            private Boolean read;
            @SerializedName("roomId")
            @Expose
            private String roomId;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;

            @SerializedName("jsonData")
            @Expose
            private JsonData jsonData;

            public JsonData getJsonData() {
                return jsonData;
            }

            public void setJsonData(JsonData jsonData) {
                this.jsonData = jsonData;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getToUserId() {
                return toUserId;
            }

            public void setToUserId(String toUserId) {
                this.toUserId = toUserId;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getChatHeadId() {
                return chatHeadId;
            }

            public void setChatHeadId(String chatHeadId) {
                this.chatHeadId = chatHeadId;
            }

            public Boolean getAttachment() {
                return attachment;
            }

            public void setAttachment(Boolean attachment) {
                this.attachment = attachment;
            }

            public String getAttachmentType() {
                return attachmentType;
            }

            public void setAttachmentType(String attachmentType) {
                this.attachmentType = attachmentType;
            }

            public String getFileURL() {
                return fileURL;
            }

            public void setFileURL(String fileURL) {
                this.fileURL = fileURL;
            }

            public Object getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(Object thumbnail) {
                this.thumbnail = thumbnail;
            }

            public Boolean getStatus() {
                return status;
            }

            public void setStatus(Boolean status) {
                this.status = status;
            }


            public Boolean getRead() {
                return read;
            }

            public void setRead(Boolean read) {
                this.read = read;
            }

            public String getRoomId() {
                return roomId;
            }

            public void setRoomId(String roomId) {
                this.roomId = roomId;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }



        }

    }
    public class JsonData
    {
        private String extension;

        private String filename;

        public String getExtension ()
        {
            return extension;
        }

        public void setExtension (String extension)
        {
            this.extension = extension;
        }

        public String getFilename ()
        {
            return filename;
        }

        public void setFilename (String filename)
        {
            this.filename = filename;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [extension = "+extension+", filename = "+filename+"]";
        }
    }
}

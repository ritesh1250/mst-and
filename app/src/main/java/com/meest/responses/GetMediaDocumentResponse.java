
package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMediaDocumentResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }



    public class Datum {

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



    }


}

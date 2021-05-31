package gachon.mp.livre_bottom_navigation;

import com.google.firebase.Timestamp;

public class NotiInfo {
    private Timestamp uploadTime;
    private String category, msgTitle, msgContent, sender, receiver;

    public NotiInfo(Timestamp uploadTime, String category, String msgTitle, String msgContent, String sender){

        this.uploadTime = uploadTime;
        this.category = category;
        this.msgTitle = msgTitle;
        this.msgContent = msgContent;
        this.sender = sender;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}

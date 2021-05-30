package gachon.mp.livre_bottom_navigation;

import com.google.firebase.Timestamp;

public class NotiInfo {
    private Timestamp uploadTime;
    private String detail, msgTitle, msgContent, user_sent;

    public NotiInfo(Timestamp uploadTime, String detail, String msgTitle, String msgContent, String user_sent){

        this.uploadTime = uploadTime;
        this.detail = detail;
        this.msgTitle = msgTitle;
        this.msgContent = msgContent;
        this.user_sent = user_sent;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public String getUser_sent() {
        return user_sent;
    }

    public void setUser_sent(String user_sent) {
        this.user_sent = user_sent;
    }
}

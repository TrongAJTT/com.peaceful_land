package com.example.peaceful_land.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class EmailDetails {
    // Class data members
    private String toEmail;
    private String subject;
    private String msgBody;
    private String attachment;

    public EmailDetails(String toEmail, String subject, String msgBody, String attachment) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.msgBody = msgBody;
        this.attachment = attachment;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}

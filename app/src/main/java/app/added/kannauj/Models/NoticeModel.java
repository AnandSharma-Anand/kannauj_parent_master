package app.added.kannauj.Models;

import java.util.Date;

public class NoticeModel {

    String id,subject,detail;
    Date date;

    public NoticeModel(String id, String subject, String detail, Date date) {
        this.id = id;
        this.subject = subject;
        this.detail = detail;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

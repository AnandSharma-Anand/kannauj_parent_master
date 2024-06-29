package app.added.kannauj.Models;

public class FeedNoticeModel {

    String subject, details;

    public FeedNoticeModel(String subject, String details) {
        this.subject = subject;
        this.details = details;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}

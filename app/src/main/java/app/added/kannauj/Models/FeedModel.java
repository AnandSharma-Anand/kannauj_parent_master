package app.added.kannauj.Models;

import java.util.Date;

public class FeedModel {

    String id,type,noticeSubject,noticeDetail,galleryEventName,galleryEventDescription,galleryImageName
            ,galleryImageDescription,studentName,holidayName,holidayDetail,notificationMessage, dueAmt, subject, chapter, topic, assignmentHeading, assignmentDetail;
    Date date,holidayStartDate,holidayEndDate;

    public FeedModel(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNoticeSubject() {
        return noticeSubject;
    }

    public void setNoticeSubject(String noticeSubject) {
        this.noticeSubject = noticeSubject;
    }

    public String getNoticeDetail() {
        return noticeDetail;
    }

    public void setNoticeDetail(String noticeDetail) {
        this.noticeDetail = noticeDetail;
    }

    public String getGalleryEventName() {
        return galleryEventName;
    }

    public void setGalleryEventName(String galleryEventName) {
        this.galleryEventName = galleryEventName;
    }

    public String getGalleryEventDescription() {
        return galleryEventDescription;
    }

    public void setGalleryEventDescription(String galleryEventDescription) {
        this.galleryEventDescription = galleryEventDescription;
    }

    public String getGalleryImageName() {
        return galleryImageName;
    }

    public void setGalleryImageName(String galleryImageName) {
        this.galleryImageName = galleryImageName;
    }

    public String getGalleryImageDescription() {
        return galleryImageDescription;
    }

    public void setGalleryImageDescription(String galleryImageDescription) {
        this.galleryImageDescription = galleryImageDescription;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getHolidayDetail() {
        return holidayDetail;
    }

    public void setHolidayDetail(String holidayDetail) {
        this.holidayDetail = holidayDetail;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getHolidayStartDate() {
        return holidayStartDate;
    }

    public void setHolidayStartDate(Date holidayStartDate) {
        this.holidayStartDate = holidayStartDate;
    }

    public Date getHolidayEndDate() {
        return holidayEndDate;
    }

    public void setHolidayEndDate(Date holidayEndDate) {
        this.holidayEndDate = holidayEndDate;
    }

    public String getDueAmt() {
        return dueAmt;
    }

    public void setDueAmt(String dueAmt) {
        this.dueAmt = dueAmt;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAssignmentHeading() {
        return assignmentHeading;
    }

    public void setAssignmentHeading(String assignmentHeading) {
        this.assignmentHeading = assignmentHeading;
    }

    public String getAssignmentDetail() {
        return assignmentDetail;
    }

    public void setAssignmentDetail(String assignmentDetail) {
        this.assignmentDetail = assignmentDetail;
    }
}

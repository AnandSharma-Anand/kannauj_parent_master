package app.added.kannauj.Models;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class AssignmentModel implements Parcelable {

    private String id;
    private String subject;
    private String chapterName;
    private String topicName;
    private String assignmentHeading;
    private String assignmentDetail;
    private String assignmentImagePath;
    private String answerUploaded;
    private Date issueDate;
    private Date endDate;
    private List<ImageNameModel> imagelist;

    public AssignmentModel() {
    }

    protected AssignmentModel(Parcel in) {
        id = in.readString();
        subject = in.readString();
        chapterName = in.readString();
        topicName = in.readString();
        assignmentHeading = in.readString();
        assignmentDetail = in.readString();
        answerUploaded = in.readString();
        assignmentImagePath = in.readString();
        in.readTypedList(imagelist, ImageNameModel.CREATOR);

    }

    public static final Creator<AssignmentModel> CREATOR = new Creator<AssignmentModel>() {
        @Override
        public AssignmentModel createFromParcel(Parcel in) {
            return new AssignmentModel(in);
        }

        @Override
        public AssignmentModel[] newArray(int size) {
            return new AssignmentModel[size];
        }
    };

    public String getAnswerUploaded() {
        return answerUploaded;
    }

    public void setAnswerUploaded(String answerUploaded) {
        this.answerUploaded = answerUploaded;
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

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chatpterName) {
        this.chapterName = chatpterName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getAssignmentHeading() {
        return assignmentHeading;
    }

    public void setAssignmentHeading(String asiignmentHeading) {
        this.assignmentHeading = asiignmentHeading;
    }

    public String getAssignmentDetail() {
        return assignmentDetail;
    }

    public void setAssignmentDetail(String assignmentDetail) {
        this.assignmentDetail = assignmentDetail;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAssignmentImagePath() {
        return assignmentImagePath;
    }

    public void setAssignmentImagePath(String assignmentImagePath) {
        this.assignmentImagePath = assignmentImagePath;
    }


    public List<ImageNameModel> getImagelist() {
        return imagelist;
    }

    public void setImagelist(List<ImageNameModel> imagelist) {
        this.imagelist = imagelist;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(subject);
        parcel.writeString(chapterName);
        parcel.writeString(topicName);
        parcel.writeString(assignmentHeading);
        parcel.writeString(assignmentDetail);
        parcel.writeString(assignmentImagePath);
        parcel.writeString(answerUploaded);
        parcel.writeTypedList(imagelist);

    }
}


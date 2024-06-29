package app.added.kannauj.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeTableModel implements Parcelable {

    String period,subject,startTime, endTime;

    public TimeTableModel(String period,String subject, String startTime, String endTime) {
        this.period = period;
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    protected TimeTableModel(Parcel in) {
        period = in.readString();
        subject = in.readString();
        startTime = in.readString();
        endTime = in.readString();
    }

    public static final Creator<TimeTableModel> CREATOR = new Creator<TimeTableModel>() {
        @Override
        public TimeTableModel createFromParcel(Parcel in) {
            return new TimeTableModel(in);
        }

        @Override
        public TimeTableModel[] newArray(int size) {
            return new TimeTableModel[size];
        }
    };

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(period);
        parcel.writeString(subject);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
    }
}

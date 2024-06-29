package app.added.kannauj.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class FeeModel implements Serializable, Parcelable {

    public static final Creator<FeeModel> CREATOR = new Creator<FeeModel>() {
        @Override
        public FeeModel createFromParcel(Parcel in) {
            return new FeeModel(in);
        }

        @Override
        public FeeModel[] newArray(int size) {
            return new FeeModel[size];
        }
    };
    String monthName;
    String[] admissionAmount, admissionPaid, admissionDue;
    int feePriority;


    public FeeModel(String monthName, String[] admissionAmount, String[] admissionPaid, String[] admissionDue, int feePriority) {
        this.monthName = monthName;
        this.admissionAmount = admissionAmount;
        this.admissionPaid = admissionPaid;
        this.admissionDue = admissionDue;
    }

    protected FeeModel(Parcel in) {
        monthName = in.readString();
        admissionAmount = in.createStringArray();
        admissionPaid = in.createStringArray();
        admissionDue = in.createStringArray();
        feePriority = in.readInt();
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String[] getAdmissionAmount() {
        return admissionAmount;
    }

    public void setAdmissionAmount(String[] admissionAmount) {
        this.admissionAmount = admissionAmount;
    }

    public String[] getAdmissionPaid() {
        return admissionPaid;
    }

    public void setAdmissionPaid(String[] admissionPaid) {
        this.admissionPaid = admissionPaid;
    }

    public String[] getAdmissionDue() {
        return admissionDue;
    }

    public void setAdmissionDue(String[] admissionDue) {
        this.admissionDue = admissionDue;
    }


    public int getFeePriority() {
        return feePriority;
    }

    public void setFeePriority(int feePriority) {
        this.feePriority = feePriority;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(monthName);
        parcel.writeStringArray(admissionAmount);
        parcel.writeStringArray(admissionPaid);
        parcel.writeStringArray(admissionDue);
        parcel.writeInt(feePriority);
    }
}

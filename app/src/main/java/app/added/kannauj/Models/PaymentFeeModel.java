package app.added.kannauj.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PaymentFeeModel implements Serializable, Parcelable {

    public static final Creator<PaymentFeeModel> CREATOR = new Creator<PaymentFeeModel>() {
        @Override
        public PaymentFeeModel createFromParcel(Parcel in) {
            return new PaymentFeeModel(in);
        }

        @Override
        public PaymentFeeModel[] newArray(int size) {
            return new PaymentFeeModel[size];
        }
    };
    String monthName;
    String[] AmountPayable, AmountPaid, DueAmount;
    String[] FeeHead;
    String[] StudentFeeStructureID;
    int feePriority;

    public PaymentFeeModel(String monthName, String[] amountPayable, String[] amountPaid, String[] dueAmount, String[] feeHead, String[] studentFeeStructureID, int feePriority) {
        this.monthName = monthName;
        AmountPayable = amountPayable;
        AmountPaid = amountPaid;
        DueAmount = dueAmount;
        FeeHead = feeHead;
        StudentFeeStructureID = studentFeeStructureID;
        this.feePriority = feePriority;
    }

    protected PaymentFeeModel(Parcel in) {
        monthName = in.readString();
        AmountPayable = in.createStringArray();
        AmountPaid = in.createStringArray();
        DueAmount = in.createStringArray();
        FeeHead = in.createStringArray();
        StudentFeeStructureID = in.createStringArray();
        feePriority = in.readInt();
    }

    public static Creator<PaymentFeeModel> getCREATOR() {
        return CREATOR;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String[] getAmountPayable() {
        return AmountPayable;
    }

    public void setAmountPayable(String[] amountPayable) {
        AmountPayable = amountPayable;
    }

    public String[] getAmountPaid() {
        return AmountPaid;
    }

    public void setAmountPaid(String[] amountPaid) {
        AmountPaid = amountPaid;
    }

    public String[] getDueAmount() {
        return DueAmount;
    }

    public void setDueAmount(String[] dueAmount) {
        DueAmount = dueAmount;
    }

    public String[] getFeeHead() {
        return FeeHead;
    }

    public void setFeeHead(String[] feeHead) {
        FeeHead = feeHead;
    }

    public String[] getStudentFeeStructureID() {
        return StudentFeeStructureID;
    }

    public void setStudentFeeStructureID(String[] studentFeeStructureID) {
        StudentFeeStructureID = studentFeeStructureID;
    }

    public int getFeePriority() {
        return feePriority;
    }

    public void setFeePriority(int feePriority) {
        this.feePriority = feePriority;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(monthName);
        dest.writeStringArray(AmountPayable);
        dest.writeStringArray(AmountPaid);
        dest.writeStringArray(DueAmount);
        dest.writeStringArray(FeeHead);
        dest.writeStringArray(StudentFeeStructureID);
        dest.writeInt(feePriority);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

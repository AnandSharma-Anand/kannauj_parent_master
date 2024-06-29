package app.added.kannauj.Models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class PaymentFeeGroupModel extends ExpandableGroup<PaymentFeeModel> {

    int feeStatus;
    // feeStatus = 1 (paid)
    // feeStatus = -1 (pending)
    // feeStatus = 0 (neutral)

    public PaymentFeeGroupModel(String title, List<PaymentFeeModel> items, int feeStatus) {
        super(title, items);
        this.feeStatus = feeStatus;
    }

    public int getFeeStatus() {
        return feeStatus;
    }

    public void setFeeStatus(int feeStatus) {
        this.feeStatus = feeStatus;
    }

}

package app.added.kannauj.Models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class FeeGroupModel extends ExpandableGroup<FeeModel> {

    int feeStatus;
    // feeStatus = 1 (paid)
    // feeStatus = -1 (pending)
    // feeStatus = 0 (neutral)

    public FeeGroupModel(String title, List<FeeModel> items, int feeStatus) {
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

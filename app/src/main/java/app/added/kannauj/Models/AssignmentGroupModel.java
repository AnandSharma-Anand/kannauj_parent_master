package app.added.kannauj.Models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class AssignmentGroupModel extends ExpandableGroup<AssignmentModel> {


    public AssignmentGroupModel(String title, List<AssignmentModel> items) {
        super(title, items);
    }
}

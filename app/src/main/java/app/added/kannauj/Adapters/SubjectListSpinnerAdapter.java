package app.added.kannauj.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import app.added.kannauj.Models.SubjectModel;
import app.added.kannauj.R;

public class SubjectListSpinnerAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<SubjectModel>subjectList;

    public SubjectListSpinnerAdapter(Context context, List<SubjectModel>subjectList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.subjectList = subjectList;
        subjectList.add(0,new SubjectModel("","All Subjects"));
    }

    @Override
    public int getCount() {
        return subjectList.size();
    }

    @Override
    public Object getItem(int i) {
        return subjectList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.single_row_student_list,viewGroup,false);
        TextView tv = v.findViewById(R.id.tv);
        if (i==0) {
            tv.setText("Select Subject");
        } else {
            SubjectModel model = subjectList.get(i);
            tv.setText(model.getSubject());
        }
        return v;
    }
}

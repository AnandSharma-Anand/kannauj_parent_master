package app.added.kannauj.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import app.added.kannauj.Models.ScheduleModel;
import app.added.kannauj.R;

public class ChapterSpinnerAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<ScheduleModel> list;

    public ChapterSpinnerAdapter(Context context, List<ScheduleModel>list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        list.add(0, new ScheduleModel("","",null));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
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
            tv.setText("Select Chapter");
        } else {
            ScheduleModel model = list.get(i);
            tv.setText(model.getName());
        }
        return v;
    }
}

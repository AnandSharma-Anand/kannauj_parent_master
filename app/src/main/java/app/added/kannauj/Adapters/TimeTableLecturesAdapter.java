package app.added.kannauj.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.added.kannauj.Models.TimeTableModel;
import app.added.kannauj.R;

public class TimeTableLecturesAdapter extends RecyclerView.Adapter<TimeTableLecturesAdapter.TTLViewHolder> {

    Context context;
    List<TimeTableModel> list;

    public TimeTableLecturesAdapter(Context context, List<TimeTableModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TTLViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TTLViewHolder(LayoutInflater.from(context).inflate(R.layout.single_view_time_table_lecture, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TTLViewHolder ttlViewHolder, int i) {
        //ttlViewHolder.tvLectureNo.setText("Lecture "+(i+1));
        TimeTableModel model = list.get(i);
        ttlViewHolder.tvLectureNo.setText(model.getStartTime()+ " - " + model.getEndTime());
        ttlViewHolder.tvSubject.setText(model.getSubject());
        ttlViewHolder.tvClass.setText(model.getPeriod());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TTLViewHolder extends RecyclerView.ViewHolder {

        TextView tvLectureNo,tvSubject,tvClass;

        public TTLViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLectureNo = itemView.findViewById(R.id.tvLectureNo);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvClass = itemView.findViewById(R.id.tvClass);
        }
    }

}

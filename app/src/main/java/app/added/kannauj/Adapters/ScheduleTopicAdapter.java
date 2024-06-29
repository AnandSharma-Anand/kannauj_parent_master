package app.added.kannauj.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.added.kannauj.Models.TopicModel;
import app.added.kannauj.R;
import app.added.kannauj.activities.SyllabusTrackerActivity;

public class ScheduleTopicAdapter extends RecyclerView.Adapter<ScheduleTopicAdapter.STViewHolder> {

    Context context;
    List<TopicModel>list;
    SyllabusTrackerActivity activity;

    public ScheduleTopicAdapter(Context context, List<TopicModel>list) {
        this.context = context;
        activity = (SyllabusTrackerActivity) context;
        this.list = list;
    }

    @NonNull
    @Override
    public STViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new STViewHolder(LayoutInflater.from(context).inflate(R.layout.single_row_topic, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull STViewHolder holder, int position) {
        final TopicModel model = list.get(position);
        holder.tvText.setText(model.getName());
        holder.checkBox.setClickable(false);
        if (model.getStatus().equalsIgnoreCase("Pending")) {
            holder.checkBox.setChecked(false);
        } else {
            holder.checkBox.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class STViewHolder extends RecyclerView.ViewHolder {

        TextView tvText;
        CheckBox checkBox;

        public STViewHolder(View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

}

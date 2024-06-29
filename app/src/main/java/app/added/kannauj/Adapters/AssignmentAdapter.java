package app.added.kannauj.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.List;

import app.added.kannauj.Models.AssignmentModel;
import app.added.kannauj.R;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {

    Context context;
    List<AssignmentModel> assignmentList;

    public AssignmentAdapter(Context context, List<AssignmentModel>assignmentList) {
        this.context = context;
        this.assignmentList = assignmentList;
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssignmentViewHolder(LayoutInflater.from(context).inflate(R.layout.single_view_assignment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {
        AssignmentModel model = assignmentList.get(position);
        String startDate = DateFormat.getDateInstance(DateFormat.FULL).format(model.getIssueDate());
        String endDate = DateFormat.getDateInstance(DateFormat.FULL).format(model.getEndDate());
        holder.tvHeading.setText(model.getAssignmentHeading());
        holder.tvChapter.setText("Chapter : " + model.getChapterName() + "\nTopic : " + model.getTopicName());
        holder.tvDate.setText("Issue Date : " + startDate + "\n" + "End Date : " + endDate);
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }


    class AssignmentViewHolder extends RecyclerView.ViewHolder {

        TextView tvHeading,tvChapter,tvDate;

        public AssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeading = itemView.findViewById(R.id.tvHeading);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }



}

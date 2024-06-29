package app.added.kannauj.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import app.added.kannauj.Models.AssignmentGroupModel;
import app.added.kannauj.Models.AssignmentModel;
import app.added.kannauj.R;

public class AdvancedSubmitAssignmentAdapter extends ExpandableRecyclerViewAdapter<AdvancedSubmitAssignmentAdapter.TitleViewHolder, AdvancedSubmitAssignmentAdapter.AssignmentViewHolder> {

    Context context;
    LayoutInflater inflater;
    Date date = new Date();
    String pdfName = String.valueOf(date.getTime()) + ".pdf";
    ProgressDialog progressDialog;

    onItemClick onItemClick;

    public AdvancedSubmitAssignmentAdapter(List<? extends ExpandableGroup> groups, Context context, onItemClick onItemClick) {
        super(groups);
        this.context = context;
        this.onItemClick = onItemClick;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TitleViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_assignment_heading, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public AssignmentViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_submit_assignment, parent, false);
        return new AssignmentViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindChildViewHolder(final AssignmentViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final AssignmentModel assignmentModel = ((AssignmentGroupModel) group).getItems().get(childIndex);
        String startDate = DateFormat.getDateInstance(DateFormat.FULL).format(assignmentModel.getIssueDate());
        String endDate = DateFormat.getDateInstance(DateFormat.FULL).format(assignmentModel.getEndDate());
        holder.tvSubject.setText("Subject : " + assignmentModel.getSubject());
        holder.tvHeading.setText("Detail : " + assignmentModel.getAssignmentDetail());
        holder.tvChapter.setText("Chapter : " + assignmentModel.getChapterName() + "\nTopic : " + assignmentModel.getTopicName());
        holder.tvDate.setText("Issue Date : " + startDate + "\n" + "End Date : " + endDate);

        holder.tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemClick.uploadAssignment(assignmentModel.getId());
            }
        });
        holder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemClick.viewAssignment(assignmentModel.getId());
            }
        });

        if (assignmentModel.getAnswerUploaded().equals("1")) {
            holder.tvView.setVisibility(View.VISIBLE);
        } else {
            holder.tvView.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onBindGroupViewHolder(TitleViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.tvTitle.setText(group.getTitle());
        AssignmentModel model = (AssignmentModel) group.getItems().get(0);
        holder.tvSubject.setText(model.getSubject());
    }


    public interface onItemClick {

        void uploadAssignment(String assignmentId);

        void viewAssignment(String id);
    }

    class TitleViewHolder extends GroupViewHolder {

        TextView tvTitle, tvSubject;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubject = itemView.findViewById(R.id.tvSubject);
        }
    }

    class AssignmentViewHolder extends ChildViewHolder {

        TextView tvSubject, tvHeading, tvChapter, tvDate, tvUpload, tvView;

        public AssignmentViewHolder(View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvHeading = itemView.findViewById(R.id.tvHeading);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvUpload = itemView.findViewById(R.id.tvUpload);
            tvView = itemView.findViewById(R.id.tvView);
        }
    }

}

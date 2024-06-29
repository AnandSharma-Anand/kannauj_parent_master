package app.added.kannauj.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import app.added.kannauj.Models.FeeGroupModel;
import app.added.kannauj.Models.FeeModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.CustomTextView;

public class AdvancedFeeAdapter extends ExpandableRecyclerViewAdapter<AdvancedFeeAdapter.MonthViewHolder, AdvancedFeeAdapter.FeeViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<? extends ExpandableGroup> groups;
    String[] header;

    public AdvancedFeeAdapter(String[] header, List<? extends ExpandableGroup> groups, Context context) {
        super(groups);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.groups = groups;
        this.header = header;

    }


    @Override
    public MonthViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_month, parent, false);
        return new MonthViewHolder(view);
    }

    @Override
    public FeeViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_fee, parent, false);
        return new FeeViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(FeeViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        FeeModel feeModel = ((FeeGroupModel)group).getItems().get(childIndex);
        holder.lnParent.removeAllViews();
        for (int j = 0; j < header.length; j++) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView details = new TextView(context);
            details.setText("Amount Payable : " + feeModel.getAdmissionAmount()[j] + " | Amount Paid : " + feeModel.getAdmissionPaid()[j] + " | Due Amount : " + feeModel.getAdmissionDue()[j]);
            ;
            details.setPadding(0, 0, 0, 18);
            details.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            details.setTextColor(Color.WHITE);
            details.setTextSize(14);
            details.setGravity(Gravity.LEFT);


            final CustomTextView textView = new CustomTextView(context);
            textView.setText(header[j] + " : ");
            textView.setPadding(0, 0, 0, 7);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(16);
            textView.setGravity(Gravity.LEFT);

            linearLayout.addView(textView);
            linearLayout.addView(details);

            holder.lnParent.addView(linearLayout);

            /*
            TextView textView = new TextView(context);
            textView.setText(header[j]);
            holder.lnParent.addView(textView);*/

            //    holder.tvAdmissionDetails.setText("Amount Payable : " + feeModel.getAdmissionAmount()[j] + " | Amount Paid : " + feeModel.getAdmissionPaid()[j] + " | Due Amount : " + feeModel.getAdmissionDue()[j]);
           /* holder.tvTutionDetails.setText("Amount Payable : " + feeModel.get() + " | Amount Paid : " + feeModel.getTutionPaid() + " | Due Amount : " + feeModel.getTutionDue());
            holder.tvComputerDetails.setText("Amount Payable : " + feeModel.getComputerAmount() + " | Amount Paid : " + feeModel.getComputerPaid() + " | Due Amount : " + feeModel.getComputerDue());
            holder.tvExamDetails.setText("Amount Payable : " + feeModel.getExamAmount() + " | Amount Paid : " + feeModel.getExamPaid() + " | Due Amount : " + feeModel.getExamDue());
*/
        }
    }
   /* @Override
    public int getItemCount() {
        return groups.size();
    }*/

    @Override
    public void onBindGroupViewHolder(MonthViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.tvMonth.setText(group.getTitle());
        FeeGroupModel feeGroupModel = (FeeGroupModel)group;
        int status = feeGroupModel.getFeeStatus();
        if (status==1) {
            holder.cardStatus.setCardBackgroundColor(context.getResources().getColor(R.color.app_green));
        } else if (status==-1) {
            holder.cardStatus.setCardBackgroundColor(context.getResources().getColor(R.color.app_red));
        } else {
            holder.cardStatus.setCardBackgroundColor(context.getResources().getColor(R.color.app_grey));
        }

    }

    class MonthViewHolder extends GroupViewHolder {

        TextView tvMonth;
        CardView cardStatus;

        public MonthViewHolder(View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            cardStatus = itemView.findViewById(R.id.cardStatus);
        }

    }

    class FeeViewHolder extends ChildViewHolder {
        /*
                TextView tvAdmission,tvAdmissionDetails,tvTution,tvTutionDetails,tvComputer,
                        tvComputerDetails,tvExam,tvExamDetails;*/
        LinearLayout lnParent;

        public FeeViewHolder(View itemView) {
            super(itemView);
           /* tvAdmission = itemView.findViewById(R.id.tvAdmission);
            tvAdmissionDetails = itemView.findViewById(R.id.tvAdmissionDetails);
            tvTution = itemView.findViewById(R.id.tvTution);
            tvTutionDetails = itemView.findViewById(R.id.tvTutionDetails);
            tvComputer = itemView.findViewById(R.id.tvComputer);
            tvComputerDetails = itemView.findViewById(R.id.tvComputerDetails);
            tvExam = itemView.findViewById(R.id.tvExam);
            tvExamDetails = itemView.findViewById(R.id.tvExamDetails);*/
            lnParent = itemView.findViewById(R.id.lnParent);
        }

    }

}

package app.added.kannauj.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.added.kannauj.Models.FeeModel;
import app.added.kannauj.R;

public class OldFeeAdapter extends RecyclerView.Adapter<OldFeeAdapter.FeeViewHolder> {

    Context context;
    List<FeeModel> list;

    public OldFeeAdapter(Context context, List<FeeModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeeViewHolder(LayoutInflater.from(context).inflate(R.layout.single_view_fee, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeeViewHolder holder, int position) {
        FeeModel feeModel = list.get(position);
      /*  holder.tvMonth.setText(feeModel.getMonthName());
        holder.tvAdmissionDetails.setText("Amount Payable : " + feeModel.getAdmissionAmount() + " | Amount Paid : " + feeModel.getAdmissionPaid() + " | Due Amount : " + feeModel.getAdmissionDue() );
        holder.tvTutionDetails.setText("Amount Payable : " + feeModel.getTutionAmount() + " | Amount Paid : " + feeModel.getTutionPaid() + " | Due Amount : " + feeModel.getTutionDue() );
        holder.tvComputerDetails.setText("Amount Payable : " + feeModel.getComputerAmount() + " | Amount Paid : " + feeModel.getComputerPaid() + " | Due Amount : " + feeModel.getComputerDue() );
        holder.tvExamDetails.setText("Amount Payable : " + feeModel.getExamAmount() + " | Amount Paid : " + feeModel.getExamPaid() + " | Due Amount : " + feeModel.getExamDue() );
   */
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FeeViewHolder extends RecyclerView.ViewHolder {

        TextView tvMonth,tvAdmission,tvAdmissionDetails,tvTution,tvTutionDetails,tvComputer,
                tvComputerDetails,tvExam,tvExamDetails;

        public FeeViewHolder(View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvAdmission = itemView.findViewById(R.id.tvAdmission);
            tvAdmissionDetails = itemView.findViewById(R.id.tvAdmissionDetails);
            tvTution = itemView.findViewById(R.id.tvTution);
            tvTutionDetails = itemView.findViewById(R.id.tvTutionDetails);
            tvComputer = itemView.findViewById(R.id.tvComputer);
            tvComputerDetails = itemView.findViewById(R.id.tvComputerDetails);
            tvExam = itemView.findViewById(R.id.tvExam);
            tvExamDetails = itemView.findViewById(R.id.tvExamDetails);
        }
    }

}

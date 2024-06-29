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

import app.added.kannauj.Models.PaymentFeesModel;
import app.added.kannauj.R;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.FeeViewHolder> {

    Context context;
    List<PaymentFeesModel.DataBean.FeeDetailsBean.MonthBean.FeeHeadDetailsBean> list;


    public PaymentAdapter(List<PaymentFeesModel.DataBean.FeeDetailsBean.MonthBean.FeeHeadDetailsBean> feeHeadDetails, Context context) {

        this.context = context;
        this.list = feeHeadDetails;
    }

    @NonNull
    @Override
    public FeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeeViewHolder(LayoutInflater.from(context).inflate(R.layout.payment_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeeViewHolder holder, int position) {
        PaymentFeesModel.DataBean.FeeDetailsBean.MonthBean.FeeHeadDetailsBean feeModel = list.get(position);
        holder.tvHeader.setText(feeModel.getFeeHead());
        holder.tvDueAmount.setText("Due Amount : Rs. " + feeModel.getDueAmount());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FeeViewHolder extends RecyclerView.ViewHolder {

        TextView tvHeader, tvDueAmount;
        CheckBox cbClick;

        public FeeViewHolder(View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tvHeader);
            tvDueAmount = itemView.findViewById(R.id.tvDueAmount);
            cbClick = itemView.findViewById(R.id.cbClick);

        }
    }

}

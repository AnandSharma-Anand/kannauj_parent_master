package app.added.kannauj.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.added.kannauj.Models.FeesReceiptModel;
import app.added.kannauj.R;
import app.added.kannauj.activities.WebViewActivity;


public class FeesReceiptAdapter extends RecyclerView.Adapter<FeesReceiptAdapter.FeedViewHolder> {

    Context context;
    FeesReceiptModel feesReceiptModel;

    public FeesReceiptAdapter(FeesReceiptModel feesReceiptModel, Context context) {
        this.context = context;
        this.feesReceiptModel = feesReceiptModel;
    }


    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FeedViewHolder(LayoutInflater.from(context).inflate(R.layout.single_fees_payment_receipt, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder feedViewHolder, int i) {
        FeesReceiptModel.DataBean.FeeReceiptsBean model = feesReceiptModel.getData().getFeeReceipts().get(i);
        feedViewHolder.tvTitle.setText(model.getHeader());
        feedViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", model.getFeeReceiptPage());
                intent.putExtra("heading", "Fee Receipt");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return feesReceiptModel.getData().getFeeReceipts().size();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);


        }

    }


}

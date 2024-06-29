package app.added.kannauj.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.added.kannauj.Models.ChatMessageListModel;
import app.added.kannauj.R;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    List<ChatMessageListModel> list;
    boolean isLoadingAdded = false;

    public ChatMessageAdapter(Context context, List<ChatMessageListModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM:
                viewHolder = new ChatMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.single_view_chat_message, parent, false));
                break;
            case LOADING:
                viewHolder = new LoadingVH(LayoutInflater.from(context).inflate(R.layout.item_progress, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                ChatMessageViewHolder viewHolder = (ChatMessageViewHolder) holder;
                ChatMessageListModel chatMessageListModel = list.get(position);

                if (chatMessageListModel.getIsSelfMessage().equals("1")) {
                    viewHolder.llRight.setVisibility(View.VISIBLE);
                    viewHolder.llLeft.setVisibility(View.GONE);
                    viewHolder.tvRight.setText(chatMessageListModel.getMessage());
                } else {
                    viewHolder.llRight.setVisibility(View.GONE);
                    viewHolder.llLeft.setVisibility(View.VISIBLE);
                    viewHolder.tvLeft.setText(chatMessageListModel.getMessage());
                }
                break;
            case LOADING:
                // do nothing
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(null);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = 0;
        String result = getItem(position);

        if (result != null) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public String getItem(int position) {
        return list.get(position).getId();
    }

    public void add(ChatMessageListModel r) {
        list.add(0,r);
        notifyItemInserted(0);
    }

    public void addAll(List<ChatMessageListModel> results) {
        for (ChatMessageListModel result : results) {
            add(result);
        }
    }

    class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llLeft, llRight;
        TextView tvLeft, tvRight;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            llLeft = itemView.findViewById(R.id.llLeft);
            llRight = itemView.findViewById(R.id.llRight);
            tvLeft = itemView.findViewById(R.id.tvLeft);
            tvRight = itemView.findViewById(R.id.tvRight);
        }
    }

    class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

}

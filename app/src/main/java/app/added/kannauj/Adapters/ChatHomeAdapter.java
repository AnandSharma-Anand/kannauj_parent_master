package app.added.kannauj.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.added.kannauj.Models.ChatListModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.CircleTransform;
import app.added.kannauj.activities.ChatActivity;
import app.added.kannauj.activities.ChatMessageActivity;

public class ChatHomeAdapter extends RecyclerView.Adapter<ChatHomeAdapter.ChatViewHolder>  {

    Context context;
    List<ChatListModel> chatListModels;
    String Name;

    public ChatHomeAdapter(ChatActivity context, List<ChatListModel> chatListModels, String Name) {
        this.context = context;
        this.chatListModels = chatListModels;
        this.Name = Name;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.single_view_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatViewHolder holder, int position) {
        final ChatListModel chatListModel = chatListModels.get(position);
        String tempName = chatListModel.getFirstName() + " " + chatListModel.getLastName();
        if (tempName.equals(Name)) {
            holder.ivUnread.setVisibility(View.VISIBLE);
        } else {
            holder.ivUnread.setVisibility(View.INVISIBLE);
        }

        if (chatListModel.getImage().equals("")) {
            Picasso.get().load(R.drawable.girl_1).transform(new CircleTransform()).into(holder.ivImg);
        } else {
            Picasso.get().load(chatListModel.getImage()).placeholder(R.drawable.girl_1).transform(new CircleTransform()).into(holder.ivImg);
        }
        holder.tvName.setText(chatListModel.getFirstName() + " " + chatListModel.getLastName());
        holder.tvSubject.setText(chatListModel.getSubject());
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ivUnread.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(context, ChatMessageActivity.class);
                intent.putExtra("BranchStaffID", chatListModel.getBranchStaffID());
                intent.putExtra("UserName", chatListModel.getFirstName() + " " + chatListModel.getLastName());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return chatListModels.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImg;
        ImageView ivUnread;
        RelativeLayout rl;
        TextView tvName;
        TextView tvSubject;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.ivImg);
            ivUnread = itemView.findViewById(R.id.ivUnread);
            rl = itemView.findViewById(R.id.rl);
            tvName = itemView.findViewById(R.id.tvName);
            tvSubject = itemView.findViewById(R.id.tvSubject);
        }
    }


}

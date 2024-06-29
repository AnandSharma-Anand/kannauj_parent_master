package app.added.kannauj.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.added.kannauj.Models.GalleryModel;
import app.added.kannauj.R;
import app.added.kannauj.activities.ImageActivity;


public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumViewListHolder> {


    Context context;
    String imageURL;
    ArrayList<GalleryModel> albumList;

    public AlbumListAdapter(Context context, ArrayList<GalleryModel> albumList, String imageURL) {
        this.context = context;
        this.albumList = albumList;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public AlbumViewListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumListAdapter.AlbumViewListHolder(LayoutInflater.from(context).inflate(R.layout.single_view_album_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AlbumViewListHolder holder, final int position) {
        Picasso.get().load(imageURL + albumList.get(position).getImageName()).into(holder.ivImg, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                holder.progressBar.setVisibility(View.GONE);
            }
        });
                //.resize(holder.ivImg.getWidth(),holder.ivImg.getHeight()).into(holder.ivImg);
        holder.ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ImageActivity.class).putParcelableArrayListExtra("list",albumList).putExtra("imagePath",imageURL).putExtra("position",position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


    class AlbumViewListHolder extends RecyclerView.ViewHolder {

        ImageView ivImg;
        ProgressBar progressBar;

        public AlbumViewListHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.ivImg);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }


}

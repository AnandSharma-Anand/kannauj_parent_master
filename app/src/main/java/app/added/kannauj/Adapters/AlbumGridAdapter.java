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


public class AlbumGridAdapter extends RecyclerView.Adapter<AlbumGridAdapter.AlbumViewHolder> {

    Context context;
    String imageURL;
    ArrayList<GalleryModel> albumList;

    public AlbumGridAdapter(Context context, ArrayList<GalleryModel> albumList, String imageURL) {
        this.context = context;
        this.albumList = albumList;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AlbumViewHolder(LayoutInflater.from(context).inflate(R.layout.single_view_album_grid,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AlbumViewHolder albumViewHolder, final int i) {
            Picasso.get().load(imageURL + albumList.get(i).getImageName()).centerInside().resize(300,300).into(albumViewHolder.ivImg, new Callback() {
                @Override
                public void onSuccess() {
                    albumViewHolder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    albumViewHolder.progressBar.setVisibility(View.GONE);
                }
            });
        albumViewHolder.ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ImageActivity.class).putParcelableArrayListExtra("list",albumList).putExtra("imagePath",imageURL).putExtra("position",i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImg;
        ProgressBar progressBar;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.ivImg);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}

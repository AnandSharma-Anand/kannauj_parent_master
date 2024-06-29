package app.added.kannauj.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import app.added.kannauj.Models.GalleryModel;
import app.added.kannauj.R;
import app.added.kannauj.activities.AlbumActivity;


public class GalleryAlbumAdapter extends RecyclerView.Adapter<GalleryAlbumAdapter.GAViewHolder> {

    Context context;
    String imageURL;
    List<GalleryModel> galleryList;
    private static final String TAG = "GalleryAlbumAdapter";

    public GalleryAlbumAdapter(Context context, String imageURL, List<GalleryModel> galleryList) {
        this.context = context;
        this.imageURL = imageURL;
        this.galleryList = galleryList;
    }

    @NonNull
    @Override
    public GAViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GAViewHolder(LayoutInflater.from(context).inflate(R.layout.single_view_gallery,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final GAViewHolder gaViewHolder, int i) {
        final GalleryModel item = galleryList.get(i);
        Picasso.get().load(imageURL+item.getImageName()).into(gaViewHolder.ivImg, new Callback() {
            @Override
            public void onSuccess() {
                gaViewHolder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                gaViewHolder.progressBar.setVisibility(View.GONE);
            }
        });
        gaViewHolder.tvAlbumName.setText(item.getName());
        gaViewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AlbumActivity.class).putExtra("id",item.getId()));
            }
        });
        gaViewHolder.tvCount.setText(item.getTotalImages() + " Photos");
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }


    class GAViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImg;
        TextView tvAlbumName;
        TextView tvCount;
        LinearLayout rootLayout;
        ProgressBar progressBar;

        public GAViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.ivImg);
            tvAlbumName = itemView.findViewById(R.id.tvAlbumName);
            tvCount = itemView.findViewById(R.id.tvCount);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

}

package app.added.kannauj.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import app.added.kannauj.Models.EDiaryModel;
import app.added.kannauj.R;


public class EDiaryAdapter extends RecyclerView.Adapter<EDiaryAdapter.FeedViewHolder> {

    Context context;
    List<EDiaryModel> noticeList;

    public EDiaryAdapter(Context context, List<EDiaryModel> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    public static String[] extractLinks(String text) {
        List<String> links = new ArrayList<String>();
        Matcher m = Patterns.WEB_URL.matcher(text);
        while (m.find()) {
            String url = m.group();
            Log.d("URL", "URL extracted: " + url);
            links.add(url);
        }

        return links.toArray(new String[links.size()]);
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FeedViewHolder(LayoutInflater.from(context).inflate(R.layout.single_e_diary_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder feedViewHolder, int i) {
        EDiaryModel model = noticeList.get(i);
        feedViewHolder.tvTitle.setText(model.getHeading());
        String date = DateFormat.getDateInstance(DateFormat.FULL).format(model.getCreateDate());
        feedViewHolder.tvDetail.setText(model.getDetails());
        feedViewHolder.tvSubDetail.setText(date);
        feedViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] Link = extractLinks(model.getDetails());
                if (Link.length > 0) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Link[0]));
                    context.startActivity(browserIntent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDetail, tvSubDetail;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubDetail = itemView.findViewById(R.id.tvSubDetail);

        }

    }


}

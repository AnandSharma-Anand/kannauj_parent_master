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

import app.added.kannauj.Models.NoticeModel;
import app.added.kannauj.R;


public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.FeedViewHolder>{

    Context context;
    List<NoticeModel> noticeList;

    public NoticeAdapter(Context context, List<NoticeModel> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FeedViewHolder(LayoutInflater.from(context).inflate(R.layout.single_feed_view, viewGroup, false));
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

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder feedViewHolder, int i) {
        NoticeModel model = noticeList.get(i);
        feedViewHolder.tvTitle.setText(model.getSubject());
        String date = DateFormat.getDateInstance(DateFormat.FULL).format(model.getDate());
        feedViewHolder.tvDetail.setText(date);
        feedViewHolder.tvSubDetail.setText(model.getDetail());
        feedViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] Link = extractLinks(model.getDetail());
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

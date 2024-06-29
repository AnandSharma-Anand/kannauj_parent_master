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

import java.text.DateFormat;
import java.util.List;

import app.added.kannauj.Models.FeedModel;
import app.added.kannauj.R;
import app.added.kannauj.activities.AssignmentActivity;
//import app.added.parents.activities.CalendarActivity;
import app.added.kannauj.activities.EDiaryViewActivity;
import app.added.kannauj.activities.FeeActivity;
import app.added.kannauj.activities.GalleryActivity;
import app.added.kannauj.activities.NoticeViewActivity;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<FeedModel> list;
    String imagePath;

    private static final int NOTICE_VIEW = 1;
    private static final int HOLIDAY_VIEW = 2;
    private static final int GALLERY_VIEW = 3;
    private static final int FEE_VIEW = 4;
    private static final int HOMEWORK_VIEW = 5;
    private static final int STUDENT_DIARY = 6;

    public FeedAdapter(Context context, List<FeedModel> list, String imagePath) {
        this.context = context;
        this.list = list;
        this.imagePath = imagePath;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case NOTICE_VIEW:
                return new FeedNoticeViewHolder(LayoutInflater.from(context).inflate(R.layout.single_feed_view, parent, false));
            case STUDENT_DIARY:
                return new FeedNoticeViewHolder(LayoutInflater.from(context).inflate(R.layout.single_feed_view,parent,false));
            case HOLIDAY_VIEW:
                return new FeedNoticeViewHolder(LayoutInflater.from(context).inflate(R.layout.single_feed_view,parent,false));
            case GALLERY_VIEW:
                return new FeedNoticeViewHolder(LayoutInflater.from(context).inflate(R.layout.single_gallery_feed_view,parent,false));
            case FEE_VIEW:
                return new FeedNoticeViewHolder(LayoutInflater.from(context).inflate(R.layout.single_feed_view,parent,false));
            case HOMEWORK_VIEW:
                return new FeedNoticeViewHolder(LayoutInflater.from(context).inflate(R.layout.single_feed_view,parent,false));
        }

        return new RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_feed_view,parent,false)) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FeedModel model = list.get(position);
        switch (holder.getItemViewType()) {
            case NOTICE_VIEW:
                FeedNoticeViewHolder noticeViewHolder = (FeedNoticeViewHolder) holder;
                noticeViewHolder.ivImage.setImageResource(R.drawable.notice);
                noticeViewHolder.tvTitle.setText(model.getNoticeSubject());
                String date = DateFormat.getDateInstance(DateFormat.FULL).format(model.getDate());
                noticeViewHolder.tvDetail.setText(date);
                noticeViewHolder.tvSubDetail.setText(model.getNoticeDetail());
                noticeViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, NoticeViewActivity.class));
                    }
                });
                break;
            case STUDENT_DIARY:
                FeedNoticeViewHolder student_diary = (FeedNoticeViewHolder) holder;
                student_diary.ivImage.setImageResource(R.drawable.e_diary);
                student_diary.tvTitle.setText(model.getNoticeSubject());
                date = DateFormat.getDateInstance(DateFormat.FULL).format(model.getDate());
                student_diary.tvDetail.setText(date);
                student_diary.tvSubDetail.setText(model.getNoticeDetail());
                student_diary.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, EDiaryViewActivity.class));
                    }
                });
                break;
            case HOLIDAY_VIEW:
                noticeViewHolder = (FeedNoticeViewHolder) holder;
                noticeViewHolder.ivImage.setImageResource(R.drawable.calendar);
                noticeViewHolder.tvTitle.setText(model.getHolidayName());
                String startDate = DateFormat.getDateInstance(DateFormat.FULL).format(model.getHolidayStartDate());
                String endDate = DateFormat.getDateInstance(DateFormat.FULL).format(model.getHolidayEndDate());
                date = "(" + startDate + " - " + endDate + ")";
                noticeViewHolder.tvDetail.setText(date);
                noticeViewHolder.tvSubDetail.setText(model.getHolidayDetail());
                noticeViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        context.startActivity(new Intent(context, CalendarActivity.class));
                    }
                });
                break;
            case GALLERY_VIEW:
                noticeViewHolder = (FeedNoticeViewHolder) holder;
                if (model.getGalleryImageName()!=null && !model.getGalleryImageName().isEmpty())
                    Picasso.get().load(imagePath + model.getGalleryImageName()).resize(500, 500).into(noticeViewHolder.ivImage);
                noticeViewHolder.tvTitle.setText(model.getGalleryImageDescription());
                date = "(" + DateFormat.getDateInstance(DateFormat.FULL).format(model.getDate())+")\n";
                noticeViewHolder.tvDetail.setText(date);
                noticeViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, GalleryActivity.class));
                    }
                });
                break;
            case FEE_VIEW:
                noticeViewHolder = (FeedNoticeViewHolder) holder;
                noticeViewHolder.ivImage.setImageResource(R.drawable.fee);
                noticeViewHolder.tvTitle.setText("Fees due");
                date = "(" + DateFormat.getDateInstance(DateFormat.FULL).format(model.getDate()) + ")";
                noticeViewHolder.tvDetail.setText(date);
                noticeViewHolder.tvSubDetail.setText(model.getDueAmt());
                noticeViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, FeeActivity.class));
                    }
                });
                break;
            case HOMEWORK_VIEW:
                noticeViewHolder = (FeedNoticeViewHolder) holder;
                noticeViewHolder.ivImage.setImageResource(R.drawable.homework);
                noticeViewHolder.tvTitle.setText("Assignment / Homework");
                date = "(" + DateFormat.getDateInstance(DateFormat.FULL).format(model.getDate()) + ")";
                noticeViewHolder.tvDetail.setText(date);
                String hw = "Subject : " + model.getSubject() + "\nChapter : " + model.getChapter()
                        + "\nTopic : " + model.getTopic() + "\n" + model.getAssignmentHeading() +
                        "\n" + model.getAssignmentDetail();
                noticeViewHolder.tvSubDetail.setText(hw);
                noticeViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, AssignmentActivity.class));
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        String type = list.get(position).getType();
        switch (type) {
            case "Notice":
                return NOTICE_VIEW;
            case "Holiday":
                return HOLIDAY_VIEW;
            case "EventGalleryImage":
                return GALLERY_VIEW;
            case "DueFeeStatus":
                return FEE_VIEW;
            case "Homework":
                return HOMEWORK_VIEW;
            case "StudentDiary":
                return HOMEWORK_VIEW;
        }
        return super.getItemViewType(position);
    }

    class FeedNoticeViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvTitle, tvDetail, tvSubDetail;
        RelativeLayout parentLayout;

        public FeedNoticeViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImg);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvSubDetail = itemView.findViewById(R.id.tvSubDetail);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }

}

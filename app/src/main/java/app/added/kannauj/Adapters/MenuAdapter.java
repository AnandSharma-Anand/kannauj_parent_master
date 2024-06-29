package app.added.kannauj.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.added.kannauj.Models.MenuModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.activities.AssignmentActivity;
import app.added.kannauj.activities.AssignmentSubmitActivity;
//import app.added.parents.activities.CalendarActivity;
import app.added.kannauj.activities.CalendarActivity;
import app.added.kannauj.activities.ChatActivity;
import app.added.kannauj.activities.EDiaryViewActivity;
import app.added.kannauj.activities.FeeActivity;
import app.added.kannauj.activities.FeeReceiptActivity;
import app.added.kannauj.activities.GalleryActivity;
import app.added.kannauj.activities.NoticeViewActivity;
import app.added.kannauj.activities.PaymentActivity;
import app.added.kannauj.activities.ReportsActivity;
//import app.added.parents.activities.SelectExamActivity;
//import app.added.parents.activities.SelectExamnationActivity;
import app.added.kannauj.activities.SyllabusTrackerActivity;
import app.added.kannauj.activities.TimeTableActivity;

//import app.added.parents.Activities.TrackingActivity;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    Activity context;
    List<MenuModel> menuList;

    public MenuAdapter(Activity context) {
        this.context = context;
        this.menuList = initializeList();
    }

    public List<MenuModel> initializeList() {

        List<MenuModel> tempList =new ArrayList<>();
        tempList.add(new MenuModel("Fee Payment", R.drawable.payment));
        tempList.add(new MenuModel("Time Table",R.drawable.time_table));
        tempList.add(new MenuModel("Assignment/HomeWork",R.drawable.homework));
        //tempList.add(new MenuModel("Attendance",R.drawable.attendance));
        tempList.add(new MenuModel("Calendar",R.drawable.calendar));
        tempList.add(new MenuModel("Notice",R.drawable.notice));
        tempList.add(new MenuModel("Chat",R.drawable.chat));
        //tempList.add(new MenuModel("Syllabus Tracker",R.drawable.track));
        tempList.add(new MenuModel("Gallery",R.drawable.gallery));
        tempList.add(new MenuModel("Syllabus Tracker",R.drawable.track));
        tempList.add(new MenuModel("Attendance", R.drawable.calendar));
        tempList.add(new MenuModel("Tracking", R.drawable.tracking));
        tempList.add(new MenuModel("Reports", R.drawable.report));
        tempList.add(new MenuModel("Submit Homework", R.drawable.submit_homework));
        tempList.add(new MenuModel("E-Diary", R.drawable.e_diary));
        tempList.add(new MenuModel("Fee Status", R.drawable.fee));
        tempList.add(new MenuModel("Fee receipt", R.drawable.receipt));
        tempList.add(new MenuModel("Online Exam",R.drawable.exam));
        tempList.add(new MenuModel("Logout",R.drawable.logout));
        return tempList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MenuViewHolder(LayoutInflater.from(context).inflate(R.layout.single_menu_view, viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder menuViewHolder, int i) {
        menuViewHolder.tvItem.setText(menuList.get(i).getName());
        menuViewHolder.ivIcon.setImageDrawable(context.getResources().getDrawable(menuList.get(i).getImg()));

        if (i == 0) {
            menuViewHolder.ivNew.setVisibility(View.VISIBLE);
        } else {
            menuViewHolder.ivNew.setVisibility(View.INVISIBLE);
        }
        menuViewHolder.rlMenuItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (i) {

                    case 0:
                        context.startActivity(new Intent(context, PaymentActivity.class));
                        break;
                    case 1:
                        context.startActivity(new Intent(context, TimeTableActivity.class));
                        break;
                    case 2:
                        context.startActivity(new Intent(context, AssignmentActivity.class));
                        break;
                    case 3:
                        context.startActivity(new Intent(context, CalendarActivity.class).putExtra("type","cal"));
                        break;
                    case 4:
                        context.startActivity(new Intent(context, NoticeViewActivity.class));
                        break;
                    case 5:
                        context.startActivity(new Intent(context, ChatActivity.class));
                        break;
                    case 6:
                        context.startActivity(new Intent(context, GalleryActivity.class));
                        break;
                    case 7:
                        context.startActivity(new Intent(context, SyllabusTrackerActivity.class));
                        break;
                    case 8:
                        context.startActivity(new Intent(context, CalendarActivity.class).putExtra("type","atn"));
                        break;
                    case 9:
                        //context.startActivity(new Intent(context, TrackingActivity.class));
                        break;
                    case 10:
                        context.startActivity(new Intent(context, ReportsActivity.class));
                        break;
                    case 11:
                        context.startActivity(new Intent(context, AssignmentSubmitActivity.class));
                        break;
                    case 12:
                        context.startActivity(new Intent(context, EDiaryViewActivity.class));
                        break;
                    case 13:
                        context.startActivity(new Intent(context, FeeActivity.class));
                        break;
                    case 14:
                        context.startActivity(new Intent(context, FeeReceiptActivity.class));
                        break;
                    case 15:
//                      context.startActivity(new Intent(context, SelectExamnationActivity.class));
                        break;
                    case 16:
                        Log.i("sakjfhjh","------"+i);
                        new Prefs(context).makeLogout(context);

                    //  context.startActivity(new Intent(context, SelectExamActivity.class));
                        break;
                }
//                if (i==4) {
//                    context.startActivity(new Intent(context, CalendarActivity.class));
//                } else if (i==2) {
//                    context.startActivity(new Intent(context, TrackingActivity.class));
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }


    class MenuViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;
        TextView tvItem;
        ImageView ivNew;
        RelativeLayout rlMenuItemLayout;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvItem = itemView.findViewById(R.id.tvItem);
            ivNew = itemView.findViewById(R.id.ivNew);
            rlMenuItemLayout = itemView.findViewById(R.id.rlMenuItemLayout);
        }
    }

}



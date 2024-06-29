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

import app.added.kannauj.Models.StudentModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.CircleTransform;
import app.added.kannauj.Utils.DatabaseHelper;
import app.added.kannauj.Utils.Prefs;
import app.added.kannauj.activities.HomeActivity;

public class BottomSheetStudentAdapter extends RecyclerView.Adapter<BottomSheetStudentAdapter.BSSViewHolder> {

    Context context;
    DatabaseHelper helper;
    List<StudentModel> list;
    HomeActivity homeActivity;

    public BottomSheetStudentAdapter(Context context) {
        this.context = context;
        helper = new DatabaseHelper(context);
        list = helper.getAllStudents();
        homeActivity = (HomeActivity) context;
    }

    @NonNull
    @Override
    public BSSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BSSViewHolder(LayoutInflater.from(context).inflate(R.layout.single_view_bs_student, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final BSSViewHolder holder, int position) {
        final StudentModel studentModel = list.get(position);
        if (studentModel.getProfileImage().isEmpty()) {
            Picasso.get().load(R.drawable.student).transform(new CircleTransform()).into(holder.ivImg);
        } else {
            Picasso.get().load(studentModel.getProfileImage()).placeholder(R.drawable.student).transform(new CircleTransform()).into(holder.ivImg);
        }
        holder.tvText.setText(studentModel.getName());
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.with(context).removeStudent();
                Prefs.with(context).saveStudent(studentModel.getName(),studentModel.getId(),
                        studentModel.getClassId(),studentModel.getClassSectionId()
                        ,studentModel.getClassName(), studentModel.getProfileImage());
                // Prefs.saveStudent(name,id,classId,classSectionId,className,photo);
                //vxplore 18/7/19
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("isLoginFirstTime", "1");
                 context.startActivity(intent);
                //  context.startActivity(new Intent(context, HomeActivity.class));
                homeActivity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BSSViewHolder extends RecyclerView.ViewHolder {

        TextView tvText;
        ImageView ivImg;
        RelativeLayout rl;

        public BSSViewHolder(View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
            ivImg = itemView.findViewById(R.id.ivImg);
            rl = itemView.findViewById(R.id.rl);
        }
    }

}

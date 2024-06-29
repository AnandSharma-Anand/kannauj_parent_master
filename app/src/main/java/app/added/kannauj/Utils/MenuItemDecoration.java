package app.added.kannauj.Utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MenuItemDecoration extends RecyclerView.ItemDecoration{


    private final int horizontalSpace,verticalSpace;

    public MenuItemDecoration(int horizontalSpace,int verticalSpace) {
        this.horizontalSpace = horizontalSpace;
        this.verticalSpace = verticalSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = horizontalSpace;
//        if (parent.getChildAdapterPosition(view) == 0) {
//            outRect.left = horizontalSpace;
//        }

        outRect.top = verticalSpace;
//        if (parent.getChildAdapterPosition(view) == 0) {
//            outRect.bottom = verticalSpace;
//        }
    }



}

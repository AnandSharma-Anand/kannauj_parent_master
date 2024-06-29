package app.added.kannauj.Utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class FeedItemDecoration extends RecyclerView.ItemDecoration {


    private final int leftRight,verticalSpace,numOfitems;

    public FeedItemDecoration(int leftRight,int verticalSpace,int numOfitems) {
        this.leftRight = leftRight;
        this.verticalSpace = verticalSpace;
        this.numOfitems = numOfitems;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = leftRight;
        outRect.right = leftRight;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = verticalSpace + 30;

        }
            outRect.bottom = verticalSpace;


        if (parent.getChildAdapterPosition(view) == numOfitems) {
            outRect.bottom = verticalSpace+30;
        }

    }

}

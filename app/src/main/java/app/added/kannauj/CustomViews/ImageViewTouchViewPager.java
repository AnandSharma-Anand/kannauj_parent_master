package app.added.kannauj.CustomViews;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class ImageViewTouchViewPager extends ViewPager {

    public ImageViewTouchViewPager(Context context) {
        super(context);
    }

    public ImageViewTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ImageViewTouch) {
            ImageViewTouch imageViewTouch = (ImageViewTouch)v;
            if (imageViewTouch.getScale() == imageViewTouch.getMinScale()) {
                return super.canScroll(v, checkV, dx, x, y);
            }
            return imageViewTouchCanScroll(imageViewTouch, dx);
        } else {
            return super.canScroll(v, checkV, dx, x, y);
        }
    }


    /**
     * Determines whether the ImageViewTouch can be scrolled.
     *
     * @param direction - positive direction value means scroll from right to left,
     *                  negative value means scroll from left to right
     * @return true if there is some more place to scroll, false - otherwise.
     */
    private boolean imageViewTouchCanScroll(ImageViewTouch v, int direction){
        RectF bitmapRect = v.getBitmapRect();
        Rect imageViewRect = new Rect();
        getGlobalVisibleRect(imageViewRect);

        if (null == bitmapRect) {
            return false;
        }

        if (direction < 0) {
            return Math.abs(bitmapRect.right - imageViewRect.right) > 1.0f;
        }else {
            return Math.abs(bitmapRect.left - imageViewRect.left) > 1.0f;
        }

    }

}

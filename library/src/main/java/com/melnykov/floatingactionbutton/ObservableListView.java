package com.melnykov.floatingactionbutton;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;

public class ObservableListView extends ListView {

    private int mItemOffsetY[];
    private boolean isScrollComputed;

    public ObservableListView(Context context) {
        super(context);
        init();
    }

    public ObservableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                computeScrollY();
            }
        });
    }

    private void computeScrollY() {
        int height = 0;
        int itemCount = getAdapter().getCount();
        if (mItemOffsetY == null) {
            mItemOffsetY = new int[itemCount];
        }
        for (int i = 0; i < itemCount; ++i) {
            View view = getAdapter().getView(i, null, this);
            view.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            mItemOffsetY[i] = height;
            height += view.getMeasuredHeight();
        }
        isScrollComputed = true;
    }

    public int getComputedScrollY() {
        return  isScrollComputed ? mItemOffsetY[getFirstVisiblePosition()] - getChildAt(0).getTop() : 0;
    }
}

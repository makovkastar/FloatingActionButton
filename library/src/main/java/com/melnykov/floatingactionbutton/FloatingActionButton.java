package com.melnykov.floatingactionbutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageButton;

public class FloatingActionButton extends ImageButton {

    private StateListDrawable mDrawable;
    private AbsListView mListView;

    private int mListViewItemOffsetY[];
    private boolean isScrollComputed;
    private int mScrollY;

    private ScrollSettleHandler mScrollSettleHandler = new ScrollSettleHandler();

    public FloatingActionButton(Context context) {
        super(context);
        init();
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = (int) getResources().getDimension(R.dimen.floating_action_button);
        setMeasuredDimension(size, size);
    }

    public void attachToListView(AbsListView listView) {
        mListView = listView;
        mListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                computeListViewScrollY();
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (getListViewScrollY() == mScrollY) {
                    return;
                }

                int translationY;
                if (getListViewScrollY() > mScrollY) {
                    // Scrolling up
                    translationY = getTop();
                } else {
                    // Scrolling down
                    translationY = 0;
                }
                mScrollY = getListViewScrollY();
                mScrollSettleHandler.onScroll(translationY);
            }
        });
    }

    private void init() {
        mDrawable = new StateListDrawable();
        mDrawable.addState(new int[]{android.R.attr.state_pressed}, createPressedDrawable());
        mDrawable.addState(new int[] {}, createNormalDrawable());
        setBackgroundCompat(mDrawable);
    }

    private Drawable createNormalDrawable() {
        return getDrawable(R.drawable.floating_action_button);
    }

    private Drawable createPressedDrawable() {
        return getDrawable(R.drawable.floating_action_button_pressed);
    }

    private Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void setBackgroundCompat(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    private void computeListViewScrollY() {
        int height = 0;
        int itemCount = mListView.getAdapter().getCount();
        if (mListViewItemOffsetY == null) {
            mListViewItemOffsetY = new int[itemCount];
        }
        for (int i = 0; i < itemCount; ++i) {
            View view = mListView.getAdapter().getView(i, null, mListView);
            view.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            mListViewItemOffsetY[i] = height;
            height += view.getMeasuredHeight();
        }
        isScrollComputed = true;
    }

    private int getListViewScrollY() {
        return  isScrollComputed ? mListViewItemOffsetY[mListView.getFirstVisiblePosition()] -
                mListView.getChildAt(0).getTop() : 0;
    }

    private class ScrollSettleHandler extends Handler {
        private static final int SETTLE_DELAY_MILLIS = 100;
        private static final int TRANSLATE_DURATION_MILLIS = 500;

        private int mSettledScrollY;

        public void onScroll(int scrollY) {
            if (mSettledScrollY != scrollY) {
                mSettledScrollY = scrollY;
                removeMessages(0);
                // Clear any pending messages and post delayed
                sendEmptyMessageDelayed(0, SETTLE_DELAY_MILLIS);
            }
        }

        @Override
        public void handleMessage(Message msg) {
            animate().setDuration(TRANSLATE_DURATION_MILLIS).translationY(mSettledScrollY);
        }
    }
}

package com.melnykov.floatingactionbutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ImageButton;

public class FloatingActionButton extends ImageButton {

    private StateListDrawable mDrawable;
    private ObservableListView mListView;

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

    public void attachToListView(ObservableListView listView) {
        mListView = listView;
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mListView.getComputedScrollY() == mScrollY) {
                    return;
                }
                boolean scrollUp = false;
                if (mListView.getComputedScrollY() > mScrollY) {
                    scrollUp = true;
                }
                mScrollY = mListView.getComputedScrollY();

                int translationY = scrollUp ? getTop() : 0;
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

    private class ScrollSettleHandler extends Handler {
        private static final int SETTLE_DELAY_MILLIS = 100;

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
            // Handle the scroll settling.
            animate().translationY(mSettledScrollY);
        }
    }
}

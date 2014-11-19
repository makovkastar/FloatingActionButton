package com.melnykov.fab;

import android.support.annotation.NonNull;
import android.widget.ScrollView;

class ScrollViewScrollDirectionDetector implements ObservableScrollView.OnScrollChangedListener {
    private ScrollDirectionListener mScrollDirectionListener;
    private int mLastScrollY;
    private int mScrollThreshold;

    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        if (mScrollDirectionListener != null) {
            boolean isSignificantDelta = Math.abs(t - mLastScrollY) > mScrollThreshold;
            if (isSignificantDelta) {
                if (t > mLastScrollY) {
                    mScrollDirectionListener.onScrollUp();
                } else {
                    mScrollDirectionListener.onScrollDown();
                }
            }
            mLastScrollY = t;
        }
    }

    public void setScrollDirectionListener(@NonNull ScrollDirectionListener mScrollDirectionListener) {
        this.mScrollDirectionListener = mScrollDirectionListener;
    }

    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }
}
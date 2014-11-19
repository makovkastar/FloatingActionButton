package com.melnykov.fab;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Detects which direction list view was scrolled.
 * <p/>
 * Set {@link ScrollDirectionListener} to get callbacks
 * {@link ScrollDirectionListener#onScrollDown()} or
 * {@link ScrollDirectionListener#onScrollUp()}
 *
 * @author Aidan Follestad
 * @author Oleksandr Melnykov
 */
public abstract class ScrollDirectionRecyclerViewDetector extends RecyclerView.OnScrollListener {
    private ScrollDirectionListener mScrollDirectionListener;
    private int mMinSignificantScroll;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        mMinSignificantScroll = recyclerView.getContext().getResources().getDimensionPixelOffset(R.dimen.fab_min_significant_scroll);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mScrollDirectionListener != null) {
            boolean isSignificantDelta = Math.abs(dy) > mMinSignificantScroll;
            if (isSignificantDelta) {
                if (dy > 0) {
                    mScrollDirectionListener.onScrollUp();
                } else {
                    mScrollDirectionListener.onScrollDown();
                }
            }
        }
    }

    public ScrollDirectionListener getScrollDirectionListener() {
        return mScrollDirectionListener;
    }

    public void setScrollDirectionListener(@NonNull ScrollDirectionListener mScrollDirectionListener) {
        this.mScrollDirectionListener = mScrollDirectionListener;
    }
}
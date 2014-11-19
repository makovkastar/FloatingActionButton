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
abstract class RecyclerViewScrollDirectionDetector extends RecyclerView.OnScrollListener {
    private ScrollDirectionListener mScrollDirectionListener;
    private int mScrollThreshold;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mScrollDirectionListener != null) {
            boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
            if (isSignificantDelta) {
                if (dy > 0) {
                    mScrollDirectionListener.onScrollUp();
                } else {
                    mScrollDirectionListener.onScrollDown();
                }
            }
        }
    }

    public void setScrollDirectionListener(@NonNull ScrollDirectionListener mScrollDirectionListener) {
        this.mScrollDirectionListener = mScrollDirectionListener;
    }

    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }
}
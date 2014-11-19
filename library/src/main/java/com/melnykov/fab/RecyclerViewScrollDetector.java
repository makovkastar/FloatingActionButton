package com.melnykov.fab;

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
abstract class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener {
    private int mScrollThreshold;

    abstract void onScrollUp();

    abstract void onScrollDown();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
        if (isSignificantDelta) {
            if (dy > 0) {
                onScrollUp();
            } else {
                onScrollDown();
            }
        }
    }

    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }
}
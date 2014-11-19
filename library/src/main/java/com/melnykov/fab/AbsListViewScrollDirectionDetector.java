package com.melnykov.fab;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AbsListView;

/**
 * Detects which direction list view was scrolled.
 * <p/>
 * Set {@link ScrollDirectionListener} to get callbacks
 * {@link ScrollDirectionListener#onScrollDown()} or
 * {@link ScrollDirectionListener#onScrollUp()}
 *
 * @author Vilius Kraujutis
 * @author Oleksandr Melnykov
 */
abstract class AbsListViewScrollDirectionDetector implements AbsListView.OnScrollListener {
    private ScrollDirectionListener mScrollDirectionListener;
    private int mLastScrollY;
    private int mPreviousFirstVisibleItem;
    private AbsListView mListView;
    private int mScrollThreshold;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mScrollDirectionListener != null) {
            if (isSameRow(firstVisibleItem)) {
                int newScrollY = getTopItemScrollY();
                boolean isSignificantDelta = Math.abs(mLastScrollY - newScrollY) > mScrollThreshold;
                if (isSignificantDelta) {
                    if (mLastScrollY > newScrollY) {
                        mScrollDirectionListener.onScrollUp();
                    } else {
                        mScrollDirectionListener.onScrollDown();
                    }

                    mLastScrollY = newScrollY;
                }
            } else {
                if (firstVisibleItem > mPreviousFirstVisibleItem) {
                    mScrollDirectionListener.onScrollUp();
                } else {
                    mScrollDirectionListener.onScrollDown();
                }

                mLastScrollY = getTopItemScrollY();
                mPreviousFirstVisibleItem = firstVisibleItem;
            }
        }
    }

    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }

    public void setScrollDirectionListener(@NonNull ScrollDirectionListener mScrollDirectionListener) {
        this.mScrollDirectionListener = mScrollDirectionListener;
    }

    public void setListView(@NonNull AbsListView listView) {
        mListView = listView;
    }

    private boolean isSameRow(int firstVisibleItem) {
        return firstVisibleItem == mPreviousFirstVisibleItem;
    }

    private int getTopItemScrollY() {
        if (mListView == null || mListView.getChildAt(0) == null) return 0;
        View topChild = mListView.getChildAt(0);
        return topChild.getTop();
    }
}
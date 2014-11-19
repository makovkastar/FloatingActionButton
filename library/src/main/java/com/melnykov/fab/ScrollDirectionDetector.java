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
public abstract class ScrollDirectionDetector implements AbsListView.OnScrollListener {
    private ScrollDirectionListener mScrollDirectionListener;
    private int mLastScrollY;
    private int mPreviousFirstVisibleItem;
    private AbsListView mListView;
    private int mMinSignificantScroll;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mMinSignificantScroll = view.getContext().getResources().getDimensionPixelOffset(R.dimen.fab_min_significant_scroll);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mScrollDirectionListener != null) {
            if (isSameRow(firstVisibleItem)) {
                int newScrollY = getTopItemScrollY();
                boolean isSignificantDelta = Math.abs(mLastScrollY - newScrollY) > mMinSignificantScroll;
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

    public ScrollDirectionListener getScrollDirectionListener() {
        return mScrollDirectionListener;
    }

    public void setScrollDirectionListener(@NonNull ScrollDirectionListener mScrollDirectionListener) {
        this.mScrollDirectionListener = mScrollDirectionListener;
    }

    /**
     * Checks if <code>firstVisibleItem</code> equals the last seen first visible item.
     *
     * @return <code>true</code> if <code>firstVisibleItem</code> did not change since the last check, <code>false</code> otherwise
     */
    private boolean isSameRow(int firstVisibleItem) {
        return firstVisibleItem == mPreviousFirstVisibleItem;
    }

    /**
     * @return top value of the first visible item or 0 if there is none
     */
    private int getTopItemScrollY() {
        if (mListView == null || mListView.getChildAt(0) == null) return 0;
        View topChild = mListView.getChildAt(0);
        return topChild.getTop();
    }

    public void setListView(@NonNull AbsListView listView) {
        mListView = listView;
    }
}
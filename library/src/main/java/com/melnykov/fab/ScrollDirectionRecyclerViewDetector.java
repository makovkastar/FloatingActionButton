package com.melnykov.fab;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Detects which direction list view was scrolled.
 * <p/>
 * Set {@link ScrollDirectionListener} to get callbacks
 * {@link ScrollDirectionListener#onScrollDown()} or
 * {@link ScrollDirectionListener#onScrollUp()}
 *
 * @author Aidan Follestad
 */
public abstract class ScrollDirectionRecyclerViewDetector extends RecyclerView.OnScrollListener {
    private ScrollDirectionListener mScrollDirectionListener;
    private int mPreviousScrollY;
    private int mPreviousFirstVisibleItem;
    public int mLastChangeY;
    private RecyclerView mRecyclerView;
    private int mMinSignificantScroll;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        mMinSignificantScroll = recyclerView.getContext().getResources().getDimensionPixelOffset(R.dimen.fab_min_significant_scroll);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int newScrollY = estimateScrollY();
        if (mScrollDirectionListener != null && isSameRow(getFirstVisibleItem()) && isSignificantDelta(newScrollY)) {
            if (isScrollUp(newScrollY)) {
                mScrollDirectionListener.onScrollUp();
            } else {
                mScrollDirectionListener.onScrollDown();
            }
        }
    }

    public ScrollDirectionListener getScrollDirectionListener() {
        return mScrollDirectionListener;
    }

    public void setScrollDirectionListener(ScrollDirectionListener mScrollDirectionListener) {
        this.mScrollDirectionListener = mScrollDirectionListener;
    }

    /**
     * @return true if scrolled up or false otherwise
     * @see #isSignificantDelta(int) which ensures, that events are not fired it there was no scrolling
     */
    private boolean isScrollUp(int newScrollY) {
        boolean scrollUp = newScrollY > mPreviousScrollY;
        mPreviousScrollY = newScrollY;
        return scrollUp;
    }

    /**
     * Make sure wrong direction method is not called when stopping scrolling
     * and finger moved a little to opposite direction.
     *
     * @see #isScrollUp(int)
     */
    private boolean isSignificantDelta(int newScrollY) {
        boolean isSignificantDelta = Math.abs(mLastChangeY - newScrollY) > mMinSignificantScroll;
        if (isSignificantDelta)
            mLastChangeY = newScrollY;
        return isSignificantDelta;
    }

    /**
     * <code>newScrollY</code> position might not be correct if:
     * <ul>
     * <li><code>firstVisibleItem</code> is different than <code>mPreviousFirstVisibleItem</code></li>
     * <li>list has rows of different height</li>
     * </ul>
     * <p/>
     * It's necessary to track if row did not change, so events
     * {@link com.melnykov.fab.ScrollDirectionListener#onScrollUp()} or {@link com.melnykov.fab.ScrollDirectionListener#onScrollDown()} could be fired with confidence
     *
     * @see #estimateScrollY()
     */
    private boolean isSameRow(int firstVisibleItem) {
        boolean rowsChanged = firstVisibleItem == mPreviousFirstVisibleItem;
        mPreviousFirstVisibleItem = firstVisibleItem;
        return rowsChanged;
    }

    /**
     * Will be incorrect if rows has changed and if list has rows of different heights
     * <p/>
     * So when measuring scroll direction, it's necessary to ignore this value
     * if first visible row is different than previously calculated.
     *
     * @deprecated because it should be used with caution
     */
    private int estimateScrollY() {
        if (mRecyclerView == null || mRecyclerView.getChildAt(0) == null) return 0;
        View topChild = mRecyclerView.getChildAt(0);
        return getFirstVisibleItem() * topChild.getHeight() - topChild.getTop();
    }

    private int getFirstVisibleItem() {
        RecyclerView.LayoutManager mLayoutManager = mRecyclerView.getLayoutManager();
        if (mLayoutManager == null)
            throw new IllegalStateException("Your RecyclerView does not have a LayoutManager.");
        if (mLayoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
        } else {
            throw new RuntimeException("Currently only LinearLayoutManager is supported for the RecyclerView.");
        }
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }
}
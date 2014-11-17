package com.melnykov.fab;

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
            if(isScrollDown(firstVisibleItem))
                mScrollDirectionListener.onScrollDown();
            else if (isScrollUp(firstVisibleItem))
                mScrollDirectionListener.onScrollUp();
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
     * @see #isSignificantDelta(int, boolean) which is used to determine if there was signifcant scrolling within the same top element
     */
    private boolean isScrollUp(int firstVisibleItem) {
        boolean scrollUp = firstVisibleItem > mPreviousFirstVisibleItem;
        scrollUp |= isSignificantDelta(firstVisibleItem, true);
        return scrollUp;
    }

    /**
     * @return true if scrolled down or false otherwise
     * @see #isSignificantDelta(int, boolean) which is used to determine if there was signifcant scrolling within the same top element
     */
    private boolean isScrollDown(int firstVisibleItem) {
        boolean scrollDown = firstVisibleItem < mPreviousFirstVisibleItem;
        scrollDown |= isSignificantDelta(firstVisibleItem, false);
        return scrollDown;
    }

    /**
     * Make sure wrong direction method is not called when stopping scrolling
     * and finger moved a little to opposite direction.
     * Only works if current firstVisibleItem equals last firstVisibleItem
     *
     * @param firstVisibleItem the current first visible item
       @param isUpCheck if true, significant delta will only return true if firstVisibleItem was scrolled up. If false, it checks if firstVisibleItem was scrolled down.
     * @return true if there is a significant delta for the desired direction
     *
     * @see #isScrollUp(int)
     * @see #isScrollDown(int)
     * @see #isSameRow(int)
     */
    private boolean isSignificantDelta(int firstVisibleItem, boolean isUpCheck) {
        if(!isSameRow(firstVisibleItem)) {
            return false;
        }
        int newScrollY = getTopItemScrollY();
        boolean isDesiredDirection;
        if(isUpCheck)
            isDesiredDirection = mLastScrollY > newScrollY;
        else
            isDesiredDirection = mLastScrollY < newScrollY;
        boolean isSignificantDelta = Math.abs(mLastScrollY - newScrollY) > mMinSignificantScroll;
        if (isSignificantDelta && isDesiredDirection)
            mLastScrollY = newScrollY;
        return isSignificantDelta && isDesiredDirection;
    }

    /**
     * Used to check if firstVisibleItem equals the last seen first visible item.
     * @return true if firstVisibleItem did not change since last check, false otherwise
     *
     * @see #isSignificantDelta(int, boolean)
     */
    private boolean isSameRow(int firstVisibleItem) {
        boolean isSame = firstVisibleItem == mPreviousFirstVisibleItem;
        mPreviousFirstVisibleItem = firstVisibleItem;
        if(!isSame) {
            mLastScrollY = getTopItemScrollY();
        }
        return isSame;
    }

    /**
     * @return top value of first visible item or 0 if there is none
     */
    private int getTopItemScrollY() {
        if (mListView == null || mListView.getChildAt(0) == null) return 0;
        View topChild = mListView.getChildAt(0);
        return topChild.getTop();
    }

    public void setListView(AbsListView listView) {
        mListView = listView;
    }
}
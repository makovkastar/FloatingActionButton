package com.melnykov.fab;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * A custom {@link ScrollView} that can accept a scroll change listener.
 */
public class ObservableScrollView extends ScrollView {
    private ArrayList<OnScrollChangedListener> mCallbacks
            = new ArrayList<OnScrollChangedListener>();

    /**
     * {@link ObservableScrollView} scroll changed listener
     */
    public interface OnScrollChangedListener {
        /**
         * This is called in response to an internal scroll in this view (i.e., the
         * view scrolled its own contents). This is typically as a result of
         * {@link ScrollView#scrollBy(int, int)} or {@link ScrollView#scrollTo(int, int)} having been
         * called.
         *
         * @param who owner of the event
         * @param l Current horizontal scroll origin.
         * @param t Current vertical scroll origin.
         * @param oldl Previous horizontal scroll origin.
         * @param oldt Previous vertical scroll origin.
         */
        void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
    }

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        for (OnScrollChangedListener c : mCallbacks) {
            c.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    /**
     * Adds given {@code listener} to callback list if not added before (not sets).
     *
     * @param listener {@link OnScrollChangedListener}
     *
     * @deprecated use {@link #addOnScrollChangedListener(OnScrollChangedListener)} instead
     */
    @Deprecated
    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        addOnScrollChangedListener(listener);
    }

    /**
     * Adds given {@code listener} to callback list if not added before.
     *
     * @param listener {@link OnScrollChangedListener}
     */
    public void addOnScrollChangedListener(OnScrollChangedListener listener) {
        if (!mCallbacks.contains(listener)) {
            mCallbacks.add(listener);
        }
    }
}
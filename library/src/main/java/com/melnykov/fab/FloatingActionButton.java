package com.melnykov.fab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.ImageButton;

/**
 * Android Google+ like floating action button which reacts on the attached list view scrolling events.
 *
 * @author Oleksandr Melnykov
 */
public class FloatingActionButton extends ImageButton {

    @IntDef({TYPE_NORMAL, TYPE_MINI})
    public @interface TYPE {
    }

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_MINI = 1;

    protected AbsListView mAbsListView;
    protected RecyclerView mRecyclerView;

    private int mScrollY;
    private boolean mVisible;

    private int mColorNormal;
    private int mColorPressed;
    private boolean mShadow;
    private int mType;

    private final ScrollSettleHandler mScrollSettleHandler = new ScrollSettleHandler();
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private final AbsListView.OnScrollListener mAbsListViewOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            updateVisibility();
        }
    };
    private final RecyclerView.OnScrollListener mRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(int scrollState) {
        }

        @Override
        public void onScrolled(int dx, int dy) {
            updateVisibility();
        }
    };

    private void updateVisibility() {
        int newScrollY = getListViewScrollY();
        if (newScrollY == mScrollY) {
            return;
        }

        if (newScrollY > mScrollY) {
            // Scrolling up
            hide();
        } else if (newScrollY < mScrollY) {
            // Scrolling down
            show();
        }
        mScrollY = newScrollY;
    }

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = getDimension(
                mType == TYPE_NORMAL ? R.dimen.fab_size_normal : R.dimen.fab_size_mini);
        if (mShadow) {
            int shadowSize = getDimension(R.dimen.fab_shadow_size);
            size += shadowSize * 2;
        }
        setMeasuredDimension(size, size);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.mScrollY = mScrollY;

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            mScrollY = savedState.mScrollY;
            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private void init(Context context, AttributeSet attributeSet) {
        mVisible = true;
        mColorNormal = getColor(android.R.color.holo_blue_dark);
        mColorPressed = getColor(android.R.color.holo_blue_light);
        mType = TYPE_NORMAL;
        mShadow = true;
        if (attributeSet != null) {
            initAttributes(context, attributeSet);
        }
        updateBackground();
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray attr = getTypedArray(context, attributeSet, R.styleable.FloatingActionButton);
        if (attr != null) {
            try {
                mColorNormal = attr.getColor(R.styleable.FloatingActionButton_fab_colorNormal,
                        getColor(android.R.color.holo_blue_dark));
                mColorPressed = attr.getColor(R.styleable.FloatingActionButton_fab_colorPressed,
                        getColor(android.R.color.holo_blue_light));
                mShadow = attr.getBoolean(R.styleable.FloatingActionButton_fab_shadow, true);
                mType = attr.getInt(R.styleable.FloatingActionButton_fab_type, TYPE_NORMAL);
            } finally {
                attr.recycle();
            }
        }
    }

    private void updateBackground() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, createDrawable(mColorPressed));
        drawable.addState(new int[]{}, createDrawable(mColorNormal));
        setBackgroundCompat(drawable);
    }

    private Drawable createDrawable(int color) {
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(color);

        if (mShadow) {
            LayerDrawable layerDrawable = new LayerDrawable(
                    new Drawable[]{getResources().getDrawable(R.drawable.shadow),
                            shapeDrawable});
            int shadowSize = getDimension(
                    mType == TYPE_NORMAL ? R.dimen.fab_shadow_size : R.dimen.fab_mini_shadow_size);
            layerDrawable.setLayerInset(1, shadowSize, shadowSize, shadowSize, shadowSize);
            return layerDrawable;
        } else {
            return shapeDrawable;
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    private int getColor(@ColorRes int id) {
        return getResources().getColor(id);
    }

    private int getDimension(@DimenRes int id) {
        return getResources().getDimensionPixelSize(id);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void setBackgroundCompat(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    protected int getListViewScrollY() {
        View topChild;
        if (mRecyclerView == null && mAbsListView != null) {
            topChild = mAbsListView.getChildAt(0);
            return topChild == null ? 0 : mAbsListView.getFirstVisiblePosition() * topChild.getHeight() -
                    topChild.getTop();
        } else if (mRecyclerView != null) {
            topChild = mRecyclerView.getChildAt(0);
            return topChild == null ? 0 : ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition() * topChild.getHeight() -
                    topChild.getTop();
        } else
            throw new IllegalStateException("Not attached to a view.");
    }

    private int getMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

    private class ScrollSettleHandler extends Handler {
        private static final int TRANSLATE_DURATION_MILLIS = 200;

        private int mSettledScrollY;

        public void onScroll(int scrollY) {
            if (mSettledScrollY != scrollY) {
                mSettledScrollY = scrollY;
                removeMessages(0);
                sendEmptyMessage(0);
            }
        }

        @Override
        public void handleMessage(Message msg) {
            animate().setInterpolator(mInterpolator)
                    .setDuration(TRANSLATE_DURATION_MILLIS)
                    .translationY(mSettledScrollY);
        }
    }

    public void setColorNormal(int color) {
        if (color != mColorNormal) {
            mColorNormal = color;
            updateBackground();
        }
    }

    public void setColorNormalResId(@ColorRes int colorResId) {
        setColorNormal(getColor(colorResId));
    }

    public int getColorNormal() {
        return mColorNormal;
    }

    public void setColorPressed(int color) {
        if (color != mColorPressed) {
            mColorPressed = color;
            updateBackground();
        }
    }

    public void setColorPressedResId(@ColorRes int colorResId) {
        setColorPressed(getColor(colorResId));
    }

    public int getColorPressed() {
        return mColorPressed;
    }

    public void setShadow(boolean shadow) {
        if (shadow != mShadow) {
            mShadow = shadow;
            updateBackground();
        }
    }

    public boolean hasShadow() {
        return mShadow;
    }

    public void setType(@TYPE int type) {
        if (type != mType) {
            mType = type;
            updateBackground();
        }
    }

    @TYPE
    public int getType() {
        return mType;
    }

    /**
     * @deprecated Use {@link #getOnAbsListViewOnScrollListener()} instead.
     */
    protected AbsListView.OnScrollListener getOnScrollListener() {
        return mAbsListViewOnScrollListener;
    }

    protected AbsListView.OnScrollListener getOnAbsListViewOnScrollListener() {
        return mAbsListViewOnScrollListener;
    }

    protected RecyclerView.OnScrollListener getRecyclerViewOnScrollListener() {
        return mRecyclerViewOnScrollListener;
    }

    public void show() {
        if (!mVisible) {
            mVisible = true;
            mScrollSettleHandler.onScroll(0);
        }
    }

    public void hide() {
        if (mVisible) {
            mVisible = false;
            mScrollSettleHandler.onScroll(getHeight() + getMarginBottom());
        }
    }

    /**
     * Attaches this FloatingActionButton to a RecyclerView so that it behaves depending on it.
     * <p/>
     * A FloatingActionButton can only be attached to a view at a time and therefore, once this method is called,
     * calls to {@link com.melnykov.fab.FloatingActionButton#attachToListView(android.widget.AbsListView)} will cause an exception to be thrown.
     *
     * @param view {@link android.view.ViewGroup} The view to attach this FloatingActionButton to.
     * @throws IllegalArgumentException If the LayoutManager of the view is not a LinearLayoutManager.
     * @throws IllegalStateException    If the view lacks a LayoutManager or the FAB is already listening to a ListView.
     */
    public void attachToRecyclerView(@NonNull RecyclerView view) {
        LinearLayoutManager linearLayoutManager;
        try {
            linearLayoutManager = (LinearLayoutManager) view.getLayoutManager();
        } catch (ClassCastException ignored) {
            throw new IllegalArgumentException("To attach a FloatingActionButton a RecyclerView it must have a supported LayoutManager (LinearLayoutManager).");
        }
        if (linearLayoutManager == null) {
            throw new IllegalStateException("The LayoutManager of the view cannot be null.");
        }
        if (mAbsListView != null) {
            throw new IllegalStateException("Already listening to an AbsListView");
        }
        mRecyclerView = view;
        mRecyclerView.setOnScrollListener(mRecyclerViewOnScrollListener);
    }

    /**
     * Attaches this FloatingActionButton to an AbsListView so that it behaves depending on it.
     * <p/>
     * A FloatingActionButton can only be attached to a view at a time and therefore, once this method is called,
     * calls to {@link com.melnykov.fab.FloatingActionButton#attachToRecyclerView(android.support.v7.widget.RecyclerView)} will cause an exception to be thrown.
     *
     * @param view {@link android.widget.AbsListView} The view to attach this FloatingActionButton to.
     * @throws IllegalStateException If the FAB is already listening to a ListView.
     */
    public void attachToListView(@NonNull AbsListView view) {
        if (mRecyclerView != null)
            throw new IllegalStateException("Already listening to a RecyclerView");
        mAbsListView = view;
        mAbsListView.setOnScrollListener(mAbsListViewOnScrollListener);
    }

    /**
     * A {@link android.os.Parcelable} representing the {@link com.melnykov.fab.FloatingActionButton}'s
     * state.
     */
    public static class SavedState extends BaseSavedState {

        private int mScrollY;

        public SavedState(Parcelable parcel) {
            super(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            mScrollY = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mScrollY);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}

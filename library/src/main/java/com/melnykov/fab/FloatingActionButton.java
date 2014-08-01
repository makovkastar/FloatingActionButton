package com.melnykov.fab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;

/**
 * Android Google+ like floating action button which reacts on the attached list view scrolling events.
 *
 * @author Oleksandr Melnykov
 */
public class FloatingActionButton extends View {

    private final ScrollSettleHandler mScrollSettleHandler = new ScrollSettleHandler();
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private final Paint mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mDrawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private AbsListView mListView;
    private Bitmap mBitmap;
    private int mScrollY;
    private boolean mVisible;
    private int mColorNormal;
    private int mColorPressed;
    private boolean mShadow;

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.FloatingActionButton, 0, 0);

        mVisible = true;
        mColorNormal = a.getColor(R.styleable.FloatingActionButton_fab_colorNormal,
                getColor(android.R.color.holo_blue_dark));
        mButtonPaint.setStyle(Paint.Style.FILL);
        mButtonPaint.setColor(mColorNormal);
        mColorPressed = a.getColor(R.styleable.FloatingActionButton_fab_colorPressed,
                getColor(android.R.color.holo_blue_light));
        mShadow = a.getBoolean(R.styleable.FloatingActionButton_fab_shadow, true);

        float radius, dx, dy;
        radius = a.getFloat(R.styleable.FloatingActionButton_fab_shadowRadius, 10.0f);
        dx = a.getFloat(R.styleable.FloatingActionButton_fab_shadowDx, 0.0f);
        dy = a.getFloat(R.styleable.FloatingActionButton_fab_shadowDy, 3.5f);
        int color = a.getInteger(R.styleable.FloatingActionButton_fab_shadowColor, Color.argb(100, 0, 0, 0));
        mButtonPaint.setShadowLayer(radius, dx, dy, color);

        Drawable drawable = a.getDrawable(R.styleable.FloatingActionButton_fab_drawable);
        if (null != drawable) {
            mBitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        a.recycle();
        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) (getWidth() / 2.6), mButtonPaint);
        if (null != mBitmap) {
            canvas.drawBitmap(mBitmap, (getWidth() - mBitmap.getWidth()) / 2,
                    (getHeight() - mBitmap.getHeight()) / 2, mDrawablePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mButtonPaint.setColor(mColorNormal);
        } else {
            mButtonPaint.setColor(mColorPressed);
        }
        invalidate();
        return super.onTouchEvent(event);
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

    public void setImageDrawable(Drawable drawable) {
        if (null != drawable) {
            mBitmap = ((BitmapDrawable) drawable).getBitmap();
        }
    }

    public void setImageDrawable(@DrawableRes int resId) {
        Drawable drawable = getContext().getResources().getDrawable(resId);
        mBitmap = ((BitmapDrawable) drawable).getBitmap();

    }

    private int getColor(int id) {
        return getResources().getColor(id);
    }

    private int getDimension(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void setBackgroundCompat(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    private int getListViewScrollY() {
        View topChild = mListView.getChildAt(0);
        return topChild == null ? 0 : mListView.getFirstVisiblePosition() * topChild.getHeight() -
                topChild.getTop();
    }

    private int getMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

    public int getColorNormal() {
        return mColorNormal;
    }

    public void setColorNormal(int color) {
        if (color != mColorNormal) {
            mColorNormal = color;
            invalidate();
        }
    }

    public int getColorPressed() {
        return mColorPressed;
    }

    public void setColorPressed(int color) {
        if (color != mColorPressed) {
            mColorPressed = color;
            invalidate();
        }
    }

    public void setShadow(boolean shadow) {
        if (shadow != mShadow) {
            mShadow = shadow;
            invalidate();
        }
    }

    public boolean hasShadow() {
        return mShadow;
    }

    public void attachToListView(AbsListView listView) {
        if (listView == null) {
            throw new NullPointerException("AbsListView cannot be null.");
        }
        mListView = listView;
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int newScrollY = getListViewScrollY();
                if (newScrollY == mScrollY) {
                    return;
                }

                if (newScrollY > mScrollY && mVisible) {
                    // Scrolling up
                    mVisible = false;
                    mScrollSettleHandler.onScroll(getHeight() + getMarginBottom());
                } else if (newScrollY < mScrollY && !mVisible) {
                    // Scrolling down
                    mVisible = true;
                    mScrollSettleHandler.onScroll(0);
                }
                mScrollY = newScrollY;
            }
        });
    }

    /**
     * A {@link android.os.Parcelable} representing the {@link com.melnykov.fab.FloatingActionButton}'s
     * state.
     */
    public static class SavedState extends BaseSavedState {

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
}

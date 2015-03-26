package com.melnykov.fab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * Android Google+ like floating action button which reacts on the attached list view scrolling events.
 *
 * @author Oleksandr Melnykov
 */
public class FloatingActionButton extends ImageButton {
    private static final int TRANSLATE_DURATION_MILLIS = 200;

    @IntDef({TYPE_NORMAL, TYPE_MINI})
    public @interface TYPE {
    }

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_MINI = 1;

    private boolean mVisible;

    private int mColorNormal;
    private int mColorPressed;
    private int mColorRipple;
    private boolean mShadow;
    private int mType;

    private int mShadowSize;

    private int mScrollThreshold;

    private boolean mMarginsSet;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

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
        if (mShadow && !hasLollipopApi()) {
            size += mShadowSize * 2;
            setMarginsWithoutShadow();
        }
        setMeasuredDimension(size, size);
    }

    private void init(Context context, AttributeSet attributeSet) {
        mVisible = true;
        mColorNormal = getColor(R.color.material_blue_500);
        mColorPressed = getColor(R.color.material_blue_600);
        mColorRipple = getColor(android.R.color.white);
        mType = TYPE_NORMAL;
        mShadow = true;
        mScrollThreshold = getResources().getDimensionPixelOffset(R.dimen.fab_scroll_threshold);
        mShadowSize = getDimension(R.dimen.fab_shadow_size);
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
                    getColor(R.color.material_blue_500));
                mColorPressed = attr.getColor(R.styleable.FloatingActionButton_fab_colorPressed,
                    getColor(R.color.material_blue_600));
                mColorRipple = attr.getColor(R.styleable.FloatingActionButton_fab_colorRipple,
                    getColor(android.R.color.white));
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

        if (mShadow && !hasLollipopApi()) {
            Drawable shadowDrawable = getResources().getDrawable(mType == TYPE_NORMAL ? R.drawable.shadow
                : R.drawable.shadow_mini);
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shadowDrawable, shapeDrawable});
            layerDrawable.setLayerInset(1, mShadowSize, mShadowSize, mShadowSize, mShadowSize);
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

    private void setMarginsWithoutShadow() {
        if (!mMarginsSet) {
            if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                int leftMargin = layoutParams.leftMargin - mShadowSize;
                int topMargin = layoutParams.topMargin - mShadowSize;
                int rightMargin = layoutParams.rightMargin - mShadowSize;
                int bottomMargin = layoutParams.bottomMargin - mShadowSize;
                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

                requestLayout();
                mMarginsSet = true;
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void setBackgroundCompat(Drawable drawable) {
        if (hasLollipopApi()) {
            float elevation;
            if (mShadow) {
                elevation = getElevation() > 0.0f ? getElevation()
                    : getDimension(R.dimen.fab_elevation_lollipop);
            } else {
                elevation = 0.0f;
            }
            setElevation(elevation);
            RippleDrawable rippleDrawable = new RippleDrawable(new ColorStateList(new int[][]{{}},
                new int[]{mColorRipple}), drawable, null);
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int size = getDimension(mType == TYPE_NORMAL ? R.dimen.fab_size_normal
                        : R.dimen.fab_size_mini);
                    outline.setOval(0, 0, size, size);
                }
            });
            setClipToOutline(true);
            setBackground(rippleDrawable);
        } else if (hasJellyBeanApi()) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    private int getMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
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

    public void setColorRipple(int color) {
        if (color != mColorRipple) {
            mColorRipple = color;
            updateBackground();
        }
    }

    public void setColorRippleResId(@ColorRes int colorResId) {
        setColorRipple(getColor(colorResId));
    }

    public int getColorRipple() {
        return mColorRipple;
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

    public boolean isVisible() {
        return mVisible;
    }

    public void show() {
        show(true);
    }

    public void hide() {
        hide(true);
    }

    public void show(boolean animate) {
        toggle(true, animate, false);
    }

    public void hide(boolean animate) {
        toggle(false, animate, false);
        // Hide menu items if showing
        if (isMenuShowing())
            triggerMenu(ViewHelper.getRotation(this));
    }

    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height + getMarginBottom();
            if (animate) {
                ViewPropertyAnimator.animate(this).setInterpolator(mInterpolator)
                    .setDuration(TRANSLATE_DURATION_MILLIS)
                    .translationY(translationY);
            } else {
                ViewHelper.setTranslationY(this, translationY);
            }

            // On pre-Honeycomb a translated view is still clickable, so we need to disable clicks manually
            if (!hasHoneycombApi()) {
                setClickable(visible);
            }
        }
    }

    public void attachToListView(@NonNull AbsListView listView) {
        attachToListView(listView, null, null);
    }

    public void attachToListView(@NonNull AbsListView listView,
                                 ScrollDirectionListener scrollDirectionListener) {
        attachToListView(listView, scrollDirectionListener, null);
    }

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView) {
        attachToRecyclerView(recyclerView, null, null);
    }

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView,
                                     ScrollDirectionListener scrollDirectionListener) {
        attachToRecyclerView(recyclerView, scrollDirectionListener, null);
    }

    public void attachToScrollView(@NonNull ObservableScrollView scrollView) {
        attachToScrollView(scrollView, null, null);
    }

    public void attachToScrollView(@NonNull ObservableScrollView scrollView,
                                   ScrollDirectionListener scrollDirectionListener) {
        attachToScrollView(scrollView, scrollDirectionListener, null);
    }

    public void attachToListView(@NonNull AbsListView listView,
                                 ScrollDirectionListener scrollDirectionListener,
                                 AbsListView.OnScrollListener onScrollListener) {
        AbsListViewScrollDetectorImpl scrollDetector = new AbsListViewScrollDetectorImpl();
        scrollDetector.setScrollDirectionListener(scrollDirectionListener);
        scrollDetector.setOnScrollListener(onScrollListener);
        scrollDetector.setListView(listView);
        scrollDetector.setScrollThreshold(mScrollThreshold);
        listView.setOnScrollListener(scrollDetector);
    }

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView,
                                     ScrollDirectionListener scrollDirectionlistener,
                                     RecyclerView.OnScrollListener onScrollListener) {
        RecyclerViewScrollDetectorImpl scrollDetector = new RecyclerViewScrollDetectorImpl();
        scrollDetector.setScrollDirectionListener(scrollDirectionlistener);
        scrollDetector.setOnScrollListener(onScrollListener);
        scrollDetector.setScrollThreshold(mScrollThreshold);
        recyclerView.setOnScrollListener(scrollDetector);
    }

    public void attachToScrollView(@NonNull ObservableScrollView scrollView,
                                   ScrollDirectionListener scrollDirectionListener,
                                   ObservableScrollView.OnScrollChangedListener onScrollChangedListener) {
        ScrollViewScrollDetectorImpl scrollDetector = new ScrollViewScrollDetectorImpl();
        scrollDetector.setScrollDirectionListener(scrollDirectionListener);
        scrollDetector.setOnScrollChangedListener(onScrollChangedListener);
        scrollDetector.setScrollThreshold(mScrollThreshold);
        scrollView.setOnScrollChangedListener(scrollDetector);
    }

    private boolean hasLollipopApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private boolean hasJellyBeanApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    private boolean hasHoneycombApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private class AbsListViewScrollDetectorImpl extends AbsListViewScrollDetector {
        private ScrollDirectionListener mScrollDirectionListener;
        private AbsListView.OnScrollListener mOnScrollListener;

        private void setScrollDirectionListener(ScrollDirectionListener scrollDirectionListener) {
            mScrollDirectionListener = scrollDirectionListener;
        }

        public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
            mOnScrollListener = onScrollListener;
        }

        @Override
        public void onScrollDown() {
            show();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollDown();
            }
        }

        @Override
        public void onScrollUp() {
            hide();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollUp();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }

            super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollStateChanged(view, scrollState);
            }

            super.onScrollStateChanged(view, scrollState);
        }
    }

    private class RecyclerViewScrollDetectorImpl extends RecyclerViewScrollDetector {
        private ScrollDirectionListener mScrollDirectionListener;
        private RecyclerView.OnScrollListener mOnScrollListener;

        private void setScrollDirectionListener(ScrollDirectionListener scrollDirectionListener) {
            mScrollDirectionListener = scrollDirectionListener;
        }

        public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
            mOnScrollListener = onScrollListener;
        }

        @Override
        public void onScrollDown() {
            show();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollDown();
            }
        }

        @Override
        public void onScrollUp() {
            hide();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollUp();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrolled(recyclerView, dx, dy);
            }

            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollStateChanged(recyclerView, newState);
            }

            super.onScrollStateChanged(recyclerView, newState);
        }
    }

    private class ScrollViewScrollDetectorImpl extends ScrollViewScrollDetector {
        private ScrollDirectionListener mScrollDirectionListener;

        private ObservableScrollView.OnScrollChangedListener mOnScrollChangedListener;

        private void setScrollDirectionListener(ScrollDirectionListener scrollDirectionListener) {
            mScrollDirectionListener = scrollDirectionListener;
        }

        public void setOnScrollChangedListener(ObservableScrollView.OnScrollChangedListener onScrollChangedListener) {
            mOnScrollChangedListener = onScrollChangedListener;
        }

        @Override
        public void onScrollDown() {
            show();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollDown();
            }
        }

        @Override
        public void onScrollUp() {
            hide();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollUp();
            }
        }

        @Override
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            if (mOnScrollChangedListener != null) {
                mOnScrollChangedListener.onScrollChanged(who, l, t, oldl, oldt);
            }

            super.onScrollChanged(who, l, t, oldl, oldt);
        }
    }

    /**
     * Menu
     */

    private int menuItemCount;
    private boolean isMenuShowing = false;
    private Drawable originalDrawable, menuOpenedDrawable;

    /**
     * Use this function to add a menu to your FloatingActionButton. Use the triggerMenu function to open/close the menu
     * The menu items are given ids based on their position(index) in the menuItemDrawables array
     * @param menuItemDrawables These are the image resources to be used on the menu items (FABs).
     *                          These determine the number of menu items
     * @param menuItemHintText These are the hints to be shown next to the menu items. This must be equal to the number of menu items
     *                         Provide null or empty for items that don't need a hint. Each hint is given an id (100 + its position(index)
     *                         in the menuItemHintText array)
     * @param menuItemStyle This determines the style of the menu item and same style is applied to all items
     *                      This style should contain the attributes used for styling the FAB (eg fab_colorNormal)
     * @param menuItemClickListener This is the listener for the onClick event for any menu item.
     *                              You can find which menu item was clicked based on it's id
     *                              which is it's position(index) in menuItemDrawables array
     * @param menuOpenedDrawable This is the drawable set to the base FAB when the menu is opened.
     *                           It is reset back to the original one once the menu is closed
     */
    @SuppressWarnings("ResourceType")
    public void setMenuItems(int[] menuItemDrawables,
                             String[] menuItemHintText,
                             int menuItemStyle,
                             OnClickListener menuItemClickListener,
                             Drawable menuOpenedDrawable) {

        if (menuItemHintText != null  &&
               menuItemDrawables.length != menuItemHintText.length)
            throw new IllegalArgumentException("Menu Item drawables and hint text must have the same length");

        this.menuItemCount = menuItemDrawables.length;
        this.menuOpenedDrawable = menuOpenedDrawable;
        this.originalDrawable = getDrawable();

        int[] styleValues = new int[] {
                R.attr.fab_colorPressed,
                R.attr.fab_colorNormal,
                R.attr.fab_colorRipple,
                R.attr.fab_shadow,
                R.attr.fab_type
        };

        TypedArray array = getContext().obtainStyledAttributes(menuItemStyle, styleValues);

        int colorPressed = array.getColor(0, getColorPressed());
        int colorNormal = array.getColor(1, getColorNormal());
        int colorRipple = array.getColor(2, getColorRipple());
        boolean fabShadow = array.getBoolean(3, hasShadow());
        int type = array.getInt(4, getType());

        array.recycle();

        if (getWidth() == 0)
            measure(0, 0);

        for (int id=0; id < menuItemDrawables.length; id++) {

            FloatingActionButton button = new FloatingActionButton(getContext());
            button.setId(id);
            button.setColorNormal(colorNormal);
            button.setColorPressed(colorPressed);
            button.setColorRipple(colorRipple);
            button.setShadow(fabShadow);
            button.setType(type);

            button.setImageResource(menuItemDrawables[id]);
            button.setLayoutParams(copyLayoutParams((FrameLayout.LayoutParams) getLayoutParams()));
            button.setVisibility(GONE);
            button.setOnClickListener(menuItemClickListener);
            button.measure(0, 0);

            if (getType() != type)
                if (type == TYPE_MINI && getType() == TYPE_NORMAL)
                    ((FrameLayout.LayoutParams) button.getLayoutParams())
                            .rightMargin += (getWidth() == 0 ? getMeasuredWidth() : getWidth()) / 2.0f
                                            - button.getMeasuredWidth() / 2.0f;

            ((ViewGroup) getParent()).addView(button);

            if (menuItemHintText == null ||
                menuItemHintText[id] == null ||
                menuItemHintText[id].equals(""))
                continue;

            TextView textView = new TextView(getContext());
            textView.setId(100 + id);
            textView.setText(menuItemHintText[id]);
            textView.setTextColor(Color.parseColor("#555555"));
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setVisibility(GONE);
            textView.measure(0, 0);

            FrameLayout.LayoutParams newParams =
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                 ViewGroup.LayoutParams.WRAP_CONTENT);
            newParams.rightMargin = ((FrameLayout.LayoutParams) getLayoutParams()).rightMargin
                                    + (textView.getWidth() == 0 ?
                                       textView.getMeasuredWidth() : textView.getWidth())
                                    + (button.getWidth() == 0 ?
                                       button.getMeasuredWidth() : button.getWidth()) / 2;
            newParams.gravity = ((FrameLayout.LayoutParams) getLayoutParams()).gravity;
            textView.setLayoutParams(newParams);
            ((ViewGroup) getParent()).addView(textView);

        }

    }

    /**
     * This is the method used to open/close the menu based on its current state. The base FAB
     * has a rotation animation added to it.
     * @param baseRotation The amount by which the base FAB needs to be rotated.
     */
    public void triggerMenu(float baseRotation) {
        AnimatorSet menuAnimatorSet = new AnimatorSet();
        menuAnimatorSet.setDuration(350);
        menuAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setClickable(false);
                setImageDrawable(isMenuShowing ? originalDrawable : menuOpenedDrawable);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setClickable(true);
                isMenuShowing = !isMenuShowing;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        if (baseRotation != 0)
            menuAnimatorSet.play(getBaseFABAnimator(baseRotation));

        for (int id=0; id < menuItemCount; id++) {
            menuAnimatorSet.play(getMenuItemAnimator(id));
            Animator animator = getMenuItemHintTextAnimator(id);
            if (animator != null)
                menuAnimatorSet.play(animator);
        }

        menuAnimatorSet.setInterpolator(new OvershootInterpolator(2.5f));
        menuAnimatorSet.start();
    }

    public boolean isMenuShowing() {
        return isMenuShowing;
    }

    private Animator getBaseFABAnimator(float baseRotation) {
        return ObjectAnimator.ofFloat(this, "rotation",
                                      isMenuShowing ? baseRotation : 0,
                                      isMenuShowing ? 0 : baseRotation);
    }

    private Animator getMenuItemAnimator(int id) {
        final View menuItem = ((ViewGroup) getParent()).findViewById(id);

        float newY = ViewHelper.getY(this)
                     - (menuItem.getHeight() == 0 ?
                        menuItem.getMeasuredHeight() : menuItem.getHeight()) * (id + 1)
                     - 16 * getResources().getDisplayMetrics().density * (id + 1);

        AnimatorSet menuItemAnimator = new AnimatorSet();
        menuItemAnimator.playTogether(ObjectAnimator.ofFloat(menuItem, "y",
                                                             isMenuShowing ? ViewHelper.getY(menuItem)
                                                                           : ViewHelper.getY(this),
                                                             isMenuShowing ? ViewHelper.getY(this) : newY),
                                      ObjectAnimator.ofFloat(menuItem, "alpha",
                                                             isMenuShowing ? 1 : 0,
                                                             isMenuShowing ? 0 : 1));
        menuItemAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                menuItem.setClickable(false);
                if (!isMenuShowing)
                    menuItem.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                menuItem.setClickable(true);
                if (isMenuShowing) // Looks wrong but the main listener changes this before this function is called
                    menuItem.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                menuItem.setClickable(true);
                if (isMenuShowing) // Looks wrong but the main listener changes this before this function is called
                    menuItem.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return menuItemAnimator;
    }

    private Animator getMenuItemHintTextAnimator(int id) {
        final View menuItemHintText = ((ViewGroup) getParent()).findViewById(id + 100);
        final View menuItem = ((ViewGroup) getParent()).findViewById(id);

        if (menuItemHintText == null)
            return null;

        float newY = ViewHelper.getY(this)
                     - (menuItem.getHeight() == 0 ?
                        menuItem.getMeasuredHeight() : menuItem.getHeight()) * (id + 1)
                     - 16 * getResources().getDisplayMetrics().density * (id + 1)
                     + (menuItem.getHeight() == 0 ?
                        menuItem.getMeasuredHeight() : menuItem.getHeight()) / 2.0f
                     - (menuItemHintText.getHeight() == 0 ?
                        menuItemHintText.getMeasuredHeight() : menuItemHintText.getHeight()) / 2.0f;

        AnimatorSet menuItemHintTextAnimator = new AnimatorSet();
        menuItemHintTextAnimator.playTogether(ObjectAnimator.ofFloat(menuItemHintText, "y",
                                                             isMenuShowing ? ViewHelper.getY(menuItemHintText)
                                                                           : ViewHelper.getY(this),
                                                             isMenuShowing ? ViewHelper.getY(this) : newY),
                                      ObjectAnimator.ofFloat(menuItemHintText, "alpha",
                                                             isMenuShowing ? 1 : 0,
                                                             isMenuShowing ? 0 : 1));
        menuItemHintTextAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (!isMenuShowing)
                    menuItemHintText.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isMenuShowing) // Looks wrong but the main listener changes this before this function is called
                    menuItemHintText.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (isMenuShowing) // Looks wrong but the main listener changes this before this function is called
                    menuItemHintText.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return menuItemHintTextAnimator;
    }

    private FrameLayout.LayoutParams copyLayoutParams (FrameLayout.LayoutParams source) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(source.width, source.height);
        params.gravity = source.gravity;
        params.bottomMargin = source.bottomMargin;
        params.leftMargin = source.leftMargin;
        params.rightMargin = source.rightMargin;
        params.topMargin = source.topMargin;
        return params;
    }
}

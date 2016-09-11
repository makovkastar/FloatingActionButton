package android.support.design.widget;

import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.Animation;

import com.melnykov.fab.R;

import java.util.List;

/**
 * Implementation of the FloatingActionButton behavior using com.melnykov.FloatingActionButton
 * instead of the support library version
 *
 * Created by wdullaer on 6/07/15.
 */

public class FabBehavior extends android.support.design.widget.CoordinatorLayout.Behavior<com.melnykov.fab.FloatingActionButton> {
    private static final boolean SNACKBAR_BEHAVIOR_ENABLED;
    private static final FastOutSlowInInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();
    private Rect mTmpRect;
    private boolean mIsAnimatingOut;
    private float mTranslationY;

    public FabBehavior() {
    }

    public boolean layoutDependsOn(CoordinatorLayout parent, com.melnykov.fab.FloatingActionButton child, View dependency) {
        return SNACKBAR_BEHAVIOR_ENABLED && dependency instanceof Snackbar.SnackbarLayout;
    }

    public boolean onDependentViewChanged(CoordinatorLayout parent, com.melnykov.fab.FloatingActionButton child, View dependency) {
        if(dependency instanceof Snackbar.SnackbarLayout) {
            this.updateFabTranslationForSnackbar(parent, child, dependency);
        }
        else if(dependency instanceof AppBarLayout) {
            AppBarLayout appBarLayout = (AppBarLayout)dependency;
            if(this.mTmpRect == null) {
                this.mTmpRect = new Rect();
            }

            Rect rect = this.mTmpRect;
            ViewGroupUtils.getDescendantRect(parent, dependency, rect);
            if(rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                if(!this.mIsAnimatingOut && child.getVisibility() == View.VISIBLE) {
                    this.animateOut(child);
                }
            } else if(child.getVisibility() != View.VISIBLE) {
                this.animateIn(child);
            }
        }

        return false;
    }

    private void updateFabTranslationForSnackbar(CoordinatorLayout parent, com.melnykov.fab.FloatingActionButton fab, View snackbar) {
        float translationY = this.getFabTranslationYForSnackbar(parent, fab);
        if(translationY != this.mTranslationY) {
            ViewCompat.animate(fab).cancel();
            if(Math.abs(translationY - this.mTranslationY) == (float)snackbar.getHeight()) {
                ViewCompat.animate(fab).translationY(translationY).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(null);
            } else {
                ViewCompat.setTranslationY(fab, translationY);
            }

            this.mTranslationY = translationY;
        }

    }

    private float getFabTranslationYForSnackbar(CoordinatorLayout parent, com.melnykov.fab.FloatingActionButton fab) {
        float minOffset = 0.0F;
        List dependencies = parent.getDependencies(fab);
        int i = 0;

        for(int z = dependencies.size(); i < z; ++i) {
            View view = (View)dependencies.get(i);
            if(view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset, ViewCompat.getTranslationY(view) - (float)view.getHeight());
            }
        }

        return minOffset;
    }

    private void animateIn(com.melnykov.fab.FloatingActionButton button) {
        button.setVisibility(View.VISIBLE);
        if(Build.VERSION.SDK_INT >= 14) {
            // removed the scale X & Y to avoid strange animation behavior with the FAB menu
            ViewCompat.animate(button).alpha(1.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(null).start();
        } else {
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(button.getContext(), R.anim.fab_in);
            anim.setDuration(200L);
            anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
            button.startAnimation(anim);
        }

    }

    private void animateOut(final com.melnykov.fab.FloatingActionButton button) {
        if(Build.VERSION.SDK_INT >= 14) {
            // removed the scale X & Y to avoid strange animation behavior with the FAB menu
            ViewCompat.animate(button).alpha(0.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).withLayer().setListener(new ViewPropertyAnimatorListener() {
                public void onAnimationStart(View view) {
                    FabBehavior.this.mIsAnimatingOut = true;
                }

                public void onAnimationCancel(View view) {
                    FabBehavior.this.mIsAnimatingOut = false;
                }

                public void onAnimationEnd(View view) {
                    FabBehavior.this.mIsAnimatingOut = false;
                    view.setVisibility(View.GONE);
                }
            }).start();
        } else {
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(button.getContext(), R.anim.fab_out);
            anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(200L);
            anim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationRepeat(Animation animation) {

                }

                public void onAnimationStart(Animation animation) {
                    FabBehavior.this.mIsAnimatingOut = true;
                }

                public void onAnimationEnd(Animation animation) {
                    FabBehavior.this.mIsAnimatingOut = false;
                    button.setVisibility(View.GONE);
                }
            });
            button.startAnimation(anim);
        }
    }

    static {
        SNACKBAR_BEHAVIOR_ENABLED = Build.VERSION.SDK_INT >= 11;
    }
}
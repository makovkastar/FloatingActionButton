package com.melnykov.fab;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Interpolator;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * Compat class that handles the animations of the FAB
 */
public class AnimationCompat {

    public static void animateTranslationY(@NonNull View view, @NonNull Interpolator interpolator, int duration, float y) {
        ViewPropertyAnimator.animate(view).setInterpolator(interpolator)
                .setDuration(duration)
                .translationY(y);
    }

    public static void setTranslationY(@NonNull View view, float y) {
        ViewHelper.setTranslationY(view, y);
    }
}

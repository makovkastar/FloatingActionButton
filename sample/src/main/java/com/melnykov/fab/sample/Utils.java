package com.melnykov.fab.sample;

import android.content.Context;
import android.util.TypedValue;

public class Utils {

    private Utils() {
        // Prevent instantiation
    }

    public static int getActionBarWithTabsHeight(Context context) {
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        return context.getResources().getDimensionPixelSize(tv.resourceId)
            + context.getResources().getDimensionPixelSize(R.dimen.tab_bar_size);
    }
}
package com.melnykov.fab;

/**
 * Callbacks when list was scrolled up or down.
 *
 * @author Vilius Kraujutis
 * @since 2014-10-09 02:11
 */
public interface ScrollDirectionListener {
    void onScrollDown();

    void onScrollUp();
}

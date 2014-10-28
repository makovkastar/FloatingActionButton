package com.melnykov.fab;

/**
 * Callbacks when list was scrolled up or down.
 *
 * @author Vilius Kraujutis
 */
public interface ScrollDirectionListener {
    void onScrollDown();

    void onScrollUp();
}
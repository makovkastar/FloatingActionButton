package com.melnykov.fab.sample;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ScrollView;

import com.melnykov.fab.FloatingActionButton;


public class ScrollViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.button_floating_action);
        floatingActionButton.attachToScrollView(scrollView);

    }
}
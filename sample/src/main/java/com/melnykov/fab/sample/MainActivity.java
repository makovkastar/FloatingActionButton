package com.melnykov.fab.sample;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.melnykov.fab.FloatingActionButton;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scrollview(View view) {
        Intent intent = new Intent(this, ScrollViewActivity.class);
        startActivity(intent);
    }

    public void listview(View view) {
        Intent intent = new Intent(this, ListViewActivity.class);
        startActivity(intent);
    }
}
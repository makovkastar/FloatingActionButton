package com.melkynov.fab.codesample;

import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;


public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FloatingActionButton fab = new FloatingActionButton(this);
        Resources res = getResources();
        fab.setColorNormal(res.getColor(R.color.primary));
        fab.setColorPressed(res.getColor(R.color.primary_pressed));
        fab.setImageDrawable(R.drawable.ic_action_content_new);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 72.0f, displayMetrics);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        addContentView(fab, params);

        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.planets));

        ListView listView = getListView();
        listView.setAdapter(listAdapter);

        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_LONG).show();
            }
        });
    }

}

FloatingActionButton
====================

### Description

Android Google+ like floating action button which reacts on the list view scrolling events. Becomes visible when the list view is scrolled up and invisible when scrolled down.


### Integration

Add the ``com.melnykov.fab.FloatingActionButton`` to your layout XML file. The button should be placed in the bottom right corner of the screen. The width and height of the floating action button are hardcoded to ``56dp`` as specified in the [guidlines].

```
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
             xmlns:fab="http://schemas.android.com/apk/res-auto"
             android:layout_width="match_parent"
             android:layout_height="match_parent">

    <ListView
            android:id="@android:id/list"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    <com.melnykov.fab.FloatingActionButton
            android:id="@+id/button_floating_action"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom|right"
            android:layout_margin="32dp"
            android:src="@drawable/ic_action_content_new"
            fab:colorNormal="@android:color/holo_red_dark"
            fab:colorPressed="@android:color/holo_red_light"/>
</FrameLayout>
```

[guidlines]:http://www.google.com/design/spec/patterns/promoted-actions.html#promoted-actions-floating-action-button

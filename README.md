FloatingActionButton
====================

[![Android Arsenal](http://img.shields.io/badge/Android%20Arsenal-makovkastar%2FFloatingActionButton-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/824)

### Description

Android [Google+] like floating action button which reacts on the list view scrolling events. Becomes visible when the list view is scrolled up and invisible when scrolled down.

![Demo](art/demo.gif)

### Demo

[![FloatingActionButton Demo on Google Play Store](http://developer.android.com/images/brand/en_generic_rgb_wo_60.png)](https://play.google.com/store/apps/details?id=com.melnykov.fab.sample)

### Integration

**1)** Add as a dependency to your ``build.gradle``:

```groovy
dependencies {
    compile 'com.melnykov:floatingactionbutton:1.0.6'
}
```

**2)** Add the ``com.melnykov.fab.FloatingActionButton`` to your layout XML file. The button should be placed in the bottom right corner of the screen. The width and height of the floating action button are hardcoded to **56dp** for the normal and **40dp** for the mini button as specified in the [guidlines].

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
             xmlns:fab="http://schemas.android.com/apk/res-auto"
             android:layout_width="match_parent"
             android:layout_height="match_parent">

    <ListView
            android:id="@android:id/list"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    <com.melnykov.fab.FloatingActionButton
            android:id="@+id/fab"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom|right"
            android:layout_margin="16dp"
            android:src="@drawable/ic_action_content_new"
            fab:fab_colorNormal="@color/primary"
            fab:fab_colorPressed="@color/primary_pressed"
            fab:fab_colorRipple="@color/ripple" />
</FrameLayout>
```

**3)** Attach the ``ListView``, ``GridView`` or ``RecyclerView``(currently only the ``LinearLayoutManager`` is supported) to the button in the Java code:

```java
ListView listView = (ListView) findViewById(android.R.id.list);
FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
fab.attachToListView(listView);
```
``FloatingActionButton`` extends ``android.widget.ImageButton`` so it has all methods the latter has.

If you need custom code to be executed when scrolling events occur, extend ``FloatingActionButton.FabOnScrollListener`` or ``FloatingActionButton.FabRecyclerOnViewScrollListener`` and override ``FabOnScrollListener#onScrollDown()``/`` FabOnScrollListener#onScrollUp()``. Then pass an instance of a custom listener as a second argument to ``attachToListView``/``attachToRecyclerView``.

**Do not forget to call ``super.onScrollDown()`` and ``super.onScrollUp()`` in overriden methods. If you do not, the FAB will not react to scrolling events.**

**4)** Add the namespace ``xmlns:fab="http://schemas.android.com/apk/res-auto"`` to your layout file.

+ Set the button type (normal or mini) via the ``fab_type`` xml attribute (default is normal):

    ```xml
    fab:fab_type="mini"
    ```
    or
    ```java
    fab.setType(FloatingActionButton.TYPE_MINI);
    ```
+ Set the normal and pressed colors via the xml attributes:

    ```xml
    fab:fab_colorNormal="@color/primary"
    fab:fab_colorPressed="@color/primary_pressed"
    ```
    or
    ```java
    fab.setColorNormal(getResources().getColor(R.color.primary));
    fab.setColorPressed(getResources().getColor(R.color.primary_pressed));
    ```
    
+ Enable/disable the button shadow with the ``fab_shadow`` xml attribite (it's enabled by default):

    ```xml
    fab:fab_shadow="false"
    ```
    or
    ```java
    fab.setShadow(false);
    ```
    
+ Show/hide the button expliciltly:
    
    ```java
    fab.show();
    fab.hide();
    
    fab.show(false); // Show without an animation
    fab.hide(false); // Hide without an animation
    ```
    
+ Specify the ripple color for API 21+:

    ```xml
    fab:fab_colorRipple="@color/ripple"
    ```

    or
   ```java
   fab.setColorRipple(getResources().getColor(R.color.ripple));
   ```

**5)** Set an icon for the ``FloatingActionButton`` using ``android:src`` xml attribute. Use drawables of size **24dp** as specified by [guidlines]. Icons of desired size can be generated with [Android Asset Studio].

### Changelog

**Version 1.0.6:**
+ Added support for the ``RecyclerView``;
+ Added ripple effect and elevation for API level 21.

Thanks to [Aidan Follestad](https://github.com/afollestad).

**Version 1.0.5:**
+ Updated shadow to more accurately match the material design spec;

**Version 1.0.4:**

+ Allow a custom ``OnScrollListeners`` to be attached to a list view;
+ Work properly with list of different height rows;
+ Ignore tiny shakes of fingers.

**Version 1.0.3:**
+ Add methods to show/hide without animation;
+ Fix show/hide when a view is not measured yet.


### Applications using FloatingActionButton

Please [ping](mailto:makovkastar@gmail.com) me if you would like to be added here.

Icon | Application
------------ | -------------

### Links

Country flag icons used in the sample are taken from www.icondrawer.com

### License

```
The MIT License (MIT)

Copyright (c) 2014 Oleksandr Melnykov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

[Google+]:https://play.google.com/store/apps/details?id=com.google.android.apps.plus
[guidlines]:http://www.google.com/design/spec/patterns/promoted-actions.html#promoted-actions-floating-action-button
[Android Asset Studio]:http://romannurik.github.io/AndroidAssetStudio/icons-generic.html

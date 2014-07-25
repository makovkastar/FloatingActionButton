FloatingActionButton
====================

### Description

Android [Google+] like floating action button which reacts on the list view scrolling events. Becomes visible when the list view is scrolled up and invisible when scrolled down.

![Demo](art/demo.gif)

### Integration

**1)** Clone this repo, copy the `library` module to your Gradle project. Rename it to *FloatingActionButton* and add as a dependency to your ``build.gradle``:

```groovy
dependencies {
    ...
    compile project(':FloatingActionButton')
}
```

**2)** Add the ``com.melnykov.fab.FloatingActionButton`` to your layout XML file. The button should be placed in the bottom right corner of the screen. The width and height of the floating action button are hardcoded to **56dp** as specified in the [guidlines].

```xml
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
            fab:fab_colorNormal="@android:color/holo_red_dark"
            fab:fab_colorPressed="@android:color/holo_red_light"/>
</FrameLayout>
```


**3)** Attach the list view to the button in the Java code:

```java
ListView listView = (ListView) findViewById(android.R.id.list);
FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.button_floating_action);
floatingActionButton.attachToListView(listView);
```
``FloatingActionButton`` extends ``android.widget.ImageButton`` so it has all methods the latter has.

**4)** Add the namespace ``xmlns:fab="http://schemas.android.com/apk/res-auto"`` to your layout file and set the normal and pressed colors via xml attributes:

```xml
fab:fab_colorNormal="@android:color/holo_red_dark"
fab:fab_colorPressed="@android:color/holo_red_light"
```

or in Java code:

```java
floatingActionButton.setColorNormal(getResources().getColor(android.R.color.holo_red_dark));
floatingActionButton.setColorPressed(getResources().getColor(android.R.color.holo_red_light));
```

You can also enable/disable the button shadow with the ``fab:fab_shadow`` xml attribite (it's enabled by default):

```xml
fab:fab_shadow="false"
```

or in Java code:

```java
floatingActionButton.setShadow(false);
```

**5)** Set an icon for the ``FloatingActionButton`` using ``android:src`` xml attribute. Use drawables of size **24dp** as specified by the [guidlines]. Icons of desired size can be generated with [Android Asset Studio].

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

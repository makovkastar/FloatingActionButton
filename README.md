FloatingActionButton
====================

[![Android Arsenal](http://img.shields.io/badge/Android%20Arsenal-makovkastar%2FFloatingActionButton-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/824)

### Description

Android [floating action button] which reacts on scrolling events. Becomes visible when an attached target is scrolled up and invisible when scrolled down.

![Demo](art/demo.gif)

### Demo

[![FloatingActionButton Demo on Google Play Store](http://developer.android.com/images/brand/en_generic_rgb_wo_60.png)](https://play.google.com/store/apps/details?id=com.melnykov.fab.sample)

### Integration

**1)** Add as a dependency to your ``build.gradle``:

```groovy
dependencies {
    compile 'com.melnykov:floatingactionbutton:1.3.0'
}
```

**2)** Add the ``com.melnykov.fab.FloatingActionButton`` to your layout XML file. The button should be placed in the bottom right corner of the screen. The width and height of the floating action button are hardcoded to **56dp** for the normal and **40dp** for the mini button as specified in the [guidelines].

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

**3)** Attach the FAB to ``AbsListView``, ``RecyclerView`` or ``ScrollView`` :

```java
ListView listView = (ListView) findViewById(android.R.id.list);
FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
fab.attachToListView(listView);
```

Check the sample project to see how to use custom listeners if you need to track scroll events.

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

**5)** Set an icon for the ``FloatingActionButton`` using ``android:src`` xml attribute. Use drawables of size **24dp** as specified by [guidelines]. Icons of desired size can be generated with [Android Asset Studio].

### Changelog

**Version 1.3.0**
+ Add the disabled state for the FAB (thanks to [Aleksey Malevaniy](https://github.com/almozavr));
+ Fix shadow assets. Rename shadow drawables with a prefix;
+ Generate default pressed and ripple colors (thanks to [Aidan Follestad](https://github.com/afollestad)).

**Version 1.2.0**
+ Respect an elevation set manually for the FAB;
+ Don't emit a scroll when the listview is empty;
+ Add an ability to attach normal listeners for scroll operations (thanks to [Bill Donahue](https://github.com/bdonahue)).

**Version 1.1.0:**
+ Do not ignore negative margins on pre-Lollipop;
+ Disable clicks on the FAB when it's hidden on pre-Honeycomb;
+ Some shadow tuning.

**Version 1.0.9:**
+ Support API 7;
+ Fixed extra margins on pre-Lollipop devices;
+ Fixed mini FAB size;
+ Updated shadow assets to more accurately match 8dp elevation.

**Version 1.0.8:**
+ ATTENTION! Breaking changes for custom listeners. Check an updated sample how to use them.
+ Added support for the ``ScrollView``;
+ Significantly optimized scroll detection for the ``RecyclerView``;
+ Fixed laggy animation for a list view with items of different height;
+ Added ``isVisible`` getter;
+ Deleted deprecated methods.

**Version 1.0.7:**
+ Updated shadow assets to better match material design guidelines;
+ Make ``FabOnScrollListener`` and ``FabRecyclerOnViewScrollListener`` implement ``ScrollDirectionListener`` for easier custom listeners usage.

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

Please [ping](mailto:makovkastar@gmail.com) me or send a pull request if you would like to be added here.

Icon | Application | Icon | Application
------------ | ------------- | ------------- | -------------
<img src="https://lh4.ggpht.com/oA-y_Rnemgoii-kup0MHTU0MjB9YaogVfxFfd9hDgaBysVoe6VL1cg5iNVz2pZPCx6ss=w300-rw" width="48" height="48" /> | [Finger Gesture Launcher] | <img src="https://lh3.ggpht.com/j5j1fbjO2xqDoI0C4eaVjpQ4K6kxPcSSwOQS_4xihRPKqUKtt5ud1Jd60MAuogyrUQ=w300-rw" width="48" height="48" /> | [Vocabletrainer] 
<img src="https://lh6.ggpht.com/Z40eE1-2KhlE_rSpErTkn8gjU1EOITxTCxNTDWY6EjCi0NAoAN0aUaQ5afmxC-EZqKs=w300-rw" width="48" height="48" /> | [Lanekeep GPS Mileage Tracker] | <img src="https://lh6.ggpht.com/pTT1RebLeNJMH7pm9XgQtDWpm0azxOJ7dFYkZqAMT-QE1oi2OGor3qI1ZgiJze4uYvo=w300-rw" width="48" height="48" /> | [Score It]
<img src="https://lh3.ggpht.com/M-AwKN9xIbhoAkHZWEarCasxyNYjJt2gT3HS8ScGYbJWAUi2zSQ2K_tow8lsznB4XhQ=w300-rw" width="48" height="48" /> | [Перезвони мне] | <img src="https://lh5.ggpht.com/itn5l8TL7g7YJLi-4GlD7Sg4hI-yCZv0NX85S9l5cq8BtDHPYv60S3h3ta75Pjaerg=w300-rw" width="48" height="48" /> | [App Swap] 
<img src="https://lh6.ggpht.com/wzbvTDntUigvCBp-rZj61rhPuAIF0biycsxlOZveKA8zLKpYA9pIdqp4y0h8sYPqiA=w300-rw" width="48" height="48" /> | [QKSMS - Quick Text Messenger] | <img src="https://lh3.ggpht.com/wWyXUUfu1Ryl0mpLGZC66XZjg5SuKppPSM4rLEMV94aLcQy_3HsxsylAyxadEPOM11c8=w300-rw" width="48" height="48" /> | [Uninstaller - Beta Version] 
<img src="https://lh3.ggpht.com/Rf4wGr902RKyQxBgaw7uBglwMw8JvjKCgDwZWXAH91GOcd1fvBAHo2nT5J-Uvszlsg=w300-rw" width="48" height="48" /> | [Device Control] | <img src="https://lh5.ggpht.com/lXmTtP_1u48UzqLHpeMlMp12SrtUx_otPkwH917W7Z6oL7gTSMI7Xj6tr6pYC5Vv5I8=w300-rw" width="48" height="48" /> | [Confide]
<img src="https://lh6.ggpht.com/HmBZeeZFeZ5m0AoYOK5Y2H1k8eqwa860ySNhJ_wF1AHAJlu9lxcAt8e2d8C8RWakjRY=w300-rw" width="48" height="48" /> | [Date Night] | <img src="https://lh6.ggpht.com/3swb0k5GzAiZADacoSH14JUkWij9lj_Q78VjDEkwYRSmTY7NWV9artEoSr0Jt6Ew5gsX=w300-rw" width="48" height="48" /> | [Jair Player The Music Rainbow] 
<img src="https://lh4.ggpht.com/0VAjMOZobMCMXeyynn1qUh3rOdHjIriqtNPRC7Vkm1pUKAPtIJy1ma5Y3xmGYxWwmkA=w300-rw" width="48" height="48" /> | [Taskr - Lista de Tareas] | <img src="https://lh4.ggpht.com/GC2GNlCoxHzFaE3SUFTMgKfUaMN32iOrEN8NqSUnQUNlIAMceaKyuYVNNo0j_k37LkM=w300-rw" width="48" height="48" /> | [Festivos: ¡Conoce el mundo!] 
<img src="https://lh6.ggpht.com/SE4n_W1LSRHucyRLHyXwM76XlLZAsUr4awEokmQyWkCTscZJiu8rJuE1ygWp2m0zKA=w300-rw" width="48" height="48" /> | [nowPaper] | <img src="https://lh5.ggpht.com/i6rmn-yiLY97cP9X8URrbZghNEzaVRO3Un_z7vd6PGME88ViCo99g4lKas48XzNqgw=w300-rw" width="48" height="48" /> | [Vicious chain - Don't do that!]
<img src="https://lh5.ggpht.com/ehV6oywWQw4FkXMNL8SPWCUd73oywlHJt-3b4uOI4YkH2mJgQIQADE-2Js3yupkZUUs=w300-rw" width="48" height="48" /> | [My Football Stats] | <img src="https://lh6.ggpht.com/qFqsjsT_olw1EHU34pdZO2sYGqyPZ-2Zney-qxs7mQovP5nPXKQrk56ygQh7QF9Shvk=w300-rw" width="48" height="48" /> | [The ScoreBoard]
<img src="https://lh3.ggpht.com/T19LngArl__MPoTa6Q4R6mstrtHBwte_iElYbIUbZ2kkinTw53Qfhcjrqb9bggsPvpk=w300-rw" width="48" height="48" /> | [NavPoint] | <img src="https://lh5.ggpht.com/1-XPVeLHC3Re-IIzwQGpl4I1942YUyGoetCk5ex4VteAyKVqPanWw1N1BrvSpgrNEw=w300-rw" width="48" height="48" /> | [Just Reminder] 
<img src="https://lh6.ggpht.com/vwho8dbo-Pt5zTzVN6dPlu8Ckd7YUlnLb_0H-pX3jJUPpXCIJVJ9OBtFIovPh5WWN3qO=w300-rw" width="48" height="48" /> | [Early Notes] | <img src="https://lh4.ggpht.com/aq2DO3JI3TJNuwGQq5tsNU5REfuuFRHpLep_GYYq9wyo7kv650dRNqfPoi5H3Z7d4g=w300-rw" width="48" height="48" /> | [Ranch - Smart Tip Calculator]
<img src="https://lh4.ggpht.com/Ed_Bg9DLCUX-wzqOk9TC5iABpvyOvf206kSpaUY4k8RjioiFqEnp2XjazWOW1vr0vewp=w300-rw" width="48" height="48" /> | [Thiengo Calopsita] | <img src="https://lh4.ggpht.com/VUqcWsh2BCZy45GOVo-RJSCb6rRp7xgIOOSBrgW5nARN19yrahw6cxl51wUt-Z2h7PM=w300-rw" width="48" height="48" /> | [Tinycore - CPU, RAM monitor] |
<img src="https://lh3.ggpht.com/TGCTXOfDdpjM9pVqRI83vIx3Tr0fGBPNC1_LdM5lUJPfwEyPvi9vwIAN5R2CasYmDvpT=w300-rw" width="48" height="48" /> | [Battery Aid Saver & Manager]

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

[floating action button]:http://www.google.com/design/spec/components/buttons.html#buttons-floating-action-button
[guidelines]:http://www.google.com/design/spec/patterns/promoted-actions.html#promoted-actions-floating-action-button
[Android Asset Studio]:http://romannurik.github.io/AndroidAssetStudio/icons-generic.html
[Finger Gesture Launcher]:https://play.google.com/store/apps/details?id=com.carlosdelachica.finger
[Vocabletrainer]:https://play.google.com/store/apps/details?id=com.rubengees.vocables
[Lanekeep GPS Mileage Tracker]:https://play.google.com/store/apps/details?id=me.hanx.android.dashio&hl=en
[Score It]:https://play.google.com/store/apps/details?id=com.sbgapps.scoreit
[Перезвони мне]:https://play.google.com/store/apps/details?id=com.melnykov.callmeback
[App Swap]:https://play.google.com/store/apps/details?id=net.ebt.appswitch
[QKSMS - Quick Text Messenger]:https://play.google.com/store/apps/details?id=com.moez.QKSMS
[Uninstaller - Beta Version]:https://play.google.com/store/apps/details?id=com.kimcy92.uninstaller
[Device Control]:https://play.google.com/store/apps/details?id=org.namelessrom.devicecontrol
[Confide]:https://play.google.com/store/apps/details?id=cm.confide.android
[Date Night]:https://play.google.com/store/apps/details?id=com.sababado.datenight
[Jair Player The Music Rainbow]:https://play.google.com/store/apps/details?id=aj.jair.music
[Taskr - Lista de Tareas]:https://play.google.com/store/apps/details?id=es.udc.gestortareas
[Festivos: ¡Conoce el mundo!]:https://play.google.com/store/apps/details?id=com.logicapps.holidays
[nowPaper]:https://play.google.com/store/apps/details?id=com.dunrite.now
[Vicious chain - Don't do that!]:https://play.google.com/store/apps/details?id=com.magratheadesign.viciouschain
[My Football Stats]:https://play.google.com/store/apps/details?id=com.nicorz.futbol5
[The ScoreBoard]:https://play.google.com/store/apps/details?id=it.pagano.sp
[NavPoint]:https://play.google.com/store/apps/details?id=com.abi_khalil.remy.nav_point
[Just Reminder]:https://play.google.com/store/apps/details?id=com.cray.software.justreminder
[Early Notes]:https://play.google.com/store/apps/details?id=com.gmail.earlynotesapp.earlynotes
[Ranch - Smart Tip Calculator]:https://play.google.com/store/apps/details?id=com.andreifedianov.android.ranch
[Thiengo Calopsita]:https://play.google.com/store/apps/details?id=br.thiengocalopsita
[Tinycore - CPU, RAM monitor]:https://play.google.com/store/apps/details?id=org.neotech.app.tinycore
[Battery Aid Saver & Manager]:https://play.google.com/store/apps/details?id=com.battery.plusfree

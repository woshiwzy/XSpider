package com.wangzy.xspider;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by wangzy on 15/11/10.
 */
public class App extends Application {

    public static String tag="fl";

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "xIvGrwCmPNYqJ0rEuNxKs6Sg5LpI5Ole26XRVG67", "sa8Hd0J629F5zcCrzIV6QpNIlL2dGBSJK7VJP5Ve");
    }
}

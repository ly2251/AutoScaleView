package com.ly2251.autoscaleviewui.autoviewutils;

import android.app.Application;

/**
 * Description:
 * Created by: liuyi
 * Time: 16/5/3
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AutoUtils.init(this);
    }
}

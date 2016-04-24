package com.ly2251.autoscaleviewui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import com.ly2251.autoscaleviewui.autoviewutils.AutoUtils;


/**
 * Description:
 * Created by: liuyi
 * Time: 16/4/24
 */
public class DemoUiActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        AutoUtils.getInstance().setOrientation(newConfig.orientation);
    }
}

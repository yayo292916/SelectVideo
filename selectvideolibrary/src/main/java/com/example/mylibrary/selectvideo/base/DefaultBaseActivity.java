package com.example.mylibrary.selectvideo.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;


import com.example.mylibrary.selectvideo.utils.ActivityManager;
import com.example.mylibrary.selectvideo.utils.ScreenUtil;


/**
 * Created by Wxcily on 16/1/5.
 */
public abstract class DefaultBaseActivity extends BaseActivity {

    protected Context context;
    protected Activity activity;
    protected int screenWidth;
    protected int screenHeight;

    protected boolean addTask = true;

    protected void thisHome() {
        this.addTask = false;
    }

    @Override
    protected void onBefore() {
        super.onBefore();
        this.context = this;
        this.activity = this;
        screenWidth =ScreenUtil.getScreenWidth(context);
        screenHeight =ScreenUtil.getStatusHeight(context);
//        screenWidth = MyApplicition.getInstance2().getScreenWidth();
//        screenHeight = MyApplicition.getInstance2().getScreenHeight();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (addTask)
            ActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addTask)
            ActivityManager.getInstance().delActivity(this);
    }

}

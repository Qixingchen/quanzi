package com.tizi.quanzi.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.otto.ActivityResultAns;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.otto.PermissionAnser;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;

/**
 * Created by qixingchen on 15/8/31.
 * Activity 的抽象类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();
    protected Context mContext;
    protected View view;
    protected Activity mActivity;

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mContext = this;
        mActivity = this;
        view = getWindow().getDecorView().getRootView();
        Tool.hideKeyboard(view, this);
        findView();
        initView();
        setViewEvent();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppStaticValue.setActivitys(this);
        try {
            BusProvider.getInstance().register(this);
        } catch (IllegalArgumentException ignore) {
        }
    }

    /**
     * 获取布局控件
     */
    protected abstract void findView();

    /**
     * 初始化View的一些数据
     */
    protected abstract void initView();

    /**
     * 设置点击监听
     */
    protected abstract void setViewEvent();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            BusProvider.getInstance().unregister(this);
        } catch (IllegalArgumentException ignore) {
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (StaticField.PermissionRequestCode.isPermissionRequest(requestCode)) {
            BusProvider.getInstance().post(PermissionAnser.getAns(requestCode, permissions, grantResults));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BusProvider.getInstance().post(new ActivityResultAns(requestCode, resultCode, data));
    }
}

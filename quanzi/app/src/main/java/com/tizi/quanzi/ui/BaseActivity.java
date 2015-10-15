package com.tizi.quanzi.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.otto.BusProvider;

/**
 * Created by qixingchen on 15/8/31.
 * Activity 的抽象类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();
    protected Context mContext;
    protected View view;

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mContext = this;
        view = getWindow().getDecorView().getRootView();
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
}

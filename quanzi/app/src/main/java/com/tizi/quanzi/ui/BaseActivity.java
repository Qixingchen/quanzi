package com.tizi.quanzi.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;

/**
 * Created by qixingchen on 15/8/31.
 * Activity 的抽象类
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        findView();
        initView();
        setViewEvent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppStaticValue.setActivitys(this);
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

}

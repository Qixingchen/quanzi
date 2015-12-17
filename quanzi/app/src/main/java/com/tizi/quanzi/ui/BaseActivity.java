package com.tizi.quanzi.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.otto.ActivityResultAns;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.otto.PermissionAnser;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.Tool;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by qixingchen on 15/8/31.
 * Activity 的抽象类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();
    public Menu mMenu;
    public View view;
    protected Context mContext;
    protected Activity mActivity;
    protected Toolbar toolbar;

    private CompositeSubscription mCompositeSubscription;

    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mContext = this;
        mActivity = this;
        view = findViewById(R.id.fragment);
        if (view == null) {
            Log.e(TAG, "fragment view is null");
            view = getWindow().getDecorView().getRootView();
        }
        Tool.addHideKeyboardToAllViews(view, this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
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
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
        try {
            BusProvider.getInstance().unregister(this);
        } catch (IllegalArgumentException ignore) {
        }
        RequreForImage.clear(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Tool.hideSoftKeyboard(view, mActivity);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int ans : grantResults) {
            if (ans == PackageManager.PERMISSION_DENIED) {
                Snackbar.make(view, "您拒绝了权限!", Snackbar.LENGTH_LONG).setAction("去设置", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + getApplicationContext().getPackageName())));

                    }
                }).show();
                BusProvider.getInstance().post(
                        PermissionAnser.getAns(requestCode, permissions, grantResults, false));
                return;
            }
        }
        BusProvider.getInstance().post(PermissionAnser.getAns(requestCode, permissions, grantResults, true));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BusProvider.getInstance().post(new ActivityResultAns(requestCode, resultCode, data));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }
}

package com.tizi.quanzi.ui.system_ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;

import com.tizi.quanzi.R;
import com.tizi.quanzi.notification.AddNotification;
import com.tizi.quanzi.ui.BaseActivity;

public class NotifySettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_settings);


    }

    /**
     * 获取布局控件
     */
    @Override
    protected void findView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("通知设置");
    }

    /**
     * 初始化View的一些数据
     */
    @Override
    protected void initView() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.notify_setting_fragment, new NotifyItemsFragment()).commit();
    }

    /**
     * 设置点击监听
     */
    @Override
    protected void setViewEvent() {

    }


    public static class NotifyItemsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.notify_settings);
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            AddNotification.getInstance().setSharedPreferences();
        }
    }

}

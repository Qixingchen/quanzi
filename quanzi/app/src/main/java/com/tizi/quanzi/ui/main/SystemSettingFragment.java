package com.tizi.quanzi.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * 系统设置
 */
public class SystemSettingFragment extends BaseFragment {

    public SystemSettingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_system_setting, container, false);
    }

    @Override
    protected void findViews(View view) {
        SettingItemsFragment settingItemsFragment = new SettingItemsFragment();
        getFragmentManager().beginTransaction().add(R.id.setting_fragment, settingItemsFragment).commit();
    }

    @Override
    protected void initViewsAndSetEvent() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

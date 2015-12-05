package com.tizi.quanzi.ui.main;


import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tizi.quanzi.BuildConfig;
import com.tizi.quanzi.R;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.widget.custom_tab.SimpleCustomChromeTabsHelper;

/**
 * 系统设置
 */
public class SystemSettingFragment extends BaseFragment {

    private TextView version, openSourceLicenses, userLicense;

    private SimpleCustomChromeTabsHelper mCustomTabHelper;

    public SystemSettingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_system_setting, container, false);
    }

    @Override
    protected void findViews(View view) {
        version = (TextView) view.findViewById(R.id.version);
        version.setPaintFlags(version.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        userLicense = (TextView) view.findViewById(R.id.user_license);
        userLicense.setPaintFlags(userLicense.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        openSourceLicenses = (TextView) view.findViewById(R.id.open_source_licenses);
        openSourceLicenses.setPaintFlags(openSourceLicenses.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        SettingItemsFragment settingItemsFragment = new SettingItemsFragment();
        getFragmentManager().beginTransaction().add(R.id.setting_fragment, settingItemsFragment).commit();

    }

    @Override
    protected void initViewsAndSetEvent() {

        mCustomTabHelper = new SimpleCustomChromeTabsHelper(mActivity);
        mCustomTabHelper.prepareUrl(getString(R.string.update_notices));
        mCustomTabHelper.prepareUrl(getString(R.string.user_license));
        mCustomTabHelper.prepareUrl(getString(R.string.open_source_license));

        version.setText(String.format("%s  版本%s (version code : %s )", getString(R.string.app_name),
                BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomTabHelper.openUrl(getString(R.string.update_notices));
            }
        });

        userLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomTabHelper.openUrl(getString(R.string.user_license));
            }
        });

        openSourceLicenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomTabHelper.openUrl(getString(R.string.open_source_license));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCustomTabHelper.unbindCustomTabsService();
    }
}

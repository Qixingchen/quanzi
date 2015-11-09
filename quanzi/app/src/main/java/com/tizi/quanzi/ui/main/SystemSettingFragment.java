package com.tizi.quanzi.ui.main;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tizi.quanzi.BuildConfig;
import com.tizi.quanzi.R;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * 系统设置
 */
public class SystemSettingFragment extends BaseFragment {

    private TextView version, openSourceLicenses;

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
        openSourceLicenses = (TextView) view.findViewById(R.id.open_source_licenses);
        openSourceLicenses.setPaintFlags(version.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        SettingItemsFragment settingItemsFragment = new SettingItemsFragment();
        getFragmentManager().beginTransaction().add(R.id.setting_fragment, settingItemsFragment).commit();

    }

    @Override
    protected void initViewsAndSetEvent() {

        version.setText(String.format("%s  版本%s (version code : %s )", getString(R.string.app_name),
                BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent releaseNotice = new Intent(Intent.ACTION_VIEW);
                releaseNotice.setData(Uri.parse("https://github.com/Qixingchen/quanzi_public/wiki/%E5%8D%87%E7%BA%A7%E8%AE%B0%E5%BD%95"));
                startActivity(releaseNotice);
            }
        });

        openSourceLicenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent license = new Intent(Intent.ACTION_VIEW);
                license.setData(Uri.parse("https://github.com/Qixingchen/quanzi_public/wiki/License"));
                startActivity(license);
            }
        });
    }

}

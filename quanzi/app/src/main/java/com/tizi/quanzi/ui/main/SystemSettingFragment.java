package com.tizi.quanzi.ui.main;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.tizi.quanzi.BuildConfig;
import com.tizi.quanzi.R;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.notification.AddNotification;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * 系统设置
 */
public class SystemSettingFragment extends BaseFragment implements Switch.OnCheckedChangeListener {


    private android.widget.Switch allowVibrate;
    private android.widget.Switch allowvoice;
    private android.widget.Switch allowinappnotifi;
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
        this.allowinappnotifi = (Switch) view.findViewById(R.id.allow_in_app_notifi);
        this.allowvoice = (Switch) view.findViewById(R.id.allow_sound);
        this.allowVibrate = (Switch) view.findViewById(R.id.allow_Vibrate);
        version = (TextView) view.findViewById(R.id.version);
        version.setPaintFlags(version.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        openSourceLicenses = (TextView) view.findViewById(R.id.open_source_licenses);
        openSourceLicenses.setPaintFlags(version.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

    @Override
    protected void initViewsAndSetEvent() {
        allowinappnotifi.setChecked(AddNotification.getInstance().isNeedInAppNotifi());
        allowvoice.setChecked(AddNotification.getInstance().isNeedSound());
        allowVibrate.setChecked(AddNotification.getInstance().isNeedVibrate());
        allowinappnotifi.setOnCheckedChangeListener(this);
        allowvoice.setOnCheckedChangeListener(this);
        allowVibrate.setOnCheckedChangeListener(this);

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


    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.allow_in_app_notifi:
                AddNotification.getInstance().setNeedInAppNotifi(isChecked);
                break;
            case R.id.allow_Vibrate:
                AddNotification.getInstance().setNeedVibrate(isChecked);
                break;
            case R.id.allow_sound:
                AddNotification.getInstance().setNeedSound(isChecked);
                break;
            default:
                Log.e(TAG, "error Switch id" + buttonView.getId());
        }

    }
}

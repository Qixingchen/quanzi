package com.tizi.quanzi.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

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
    }

    @Override
    protected void initViewsAndSetEvent() {
        allowinappnotifi.setChecked(AddNotification.getInstance().isNeedInAppNotifi());
        allowvoice.setChecked(AddNotification.getInstance().isNeedSound());
        allowVibrate.setChecked(AddNotification.getInstance().isNeedVibrate());
        allowinappnotifi.setOnCheckedChangeListener(this);
        allowvoice.setOnCheckedChangeListener(this);
        allowVibrate.setOnCheckedChangeListener(this);
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

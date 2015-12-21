package com.tizi.quanzi.ui.main;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.tizi.quanzi.R;

/**
 * Created by qixingchen on 15/12/21.
 */
public class DevSettingsFragmet extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.dev_settings);
    }
}

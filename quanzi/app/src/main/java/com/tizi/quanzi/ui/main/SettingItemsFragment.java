package com.tizi.quanzi.ui.main;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.tizi.quanzi.BuildConfig;
import com.tizi.quanzi.R;
import com.tizi.quanzi.notification.AddNotification;

/**
 * Created by qixingchen on 15/11/9.
 */
public class SettingItemsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final static String TAG = SettingItemsFragment.class.getSimpleName();

    @SuppressLint("StringFormatInvalid")
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.notify_settings);
        addPreferencesFromResource(R.xml.about_settings);
        addPreferencesFromResource(R.xml.adver_settings);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        Preference versionPref = findPreference("version");
        versionPref.setSummary(String.format("%s  版本 %s [版本号 : %s]", getString(R.string.app_name),
                BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));

        Preference flavorPref = findPreference("flavor");
        flavorPref.setSummary(BuildConfig.FLAVOR);

        Preference devSettings = findPreference("dev_settings");
        devSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DevSettingsFragmet devSettingsFragmet = new DevSettingsFragmet();
                getFragmentManager().beginTransaction()
                        .replace(R.id.setting_fragment, devSettingsFragmet)
                        .addToBackStack("DevSettings").commit();

                return false;
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * Called when a shared preference is changed, added, or removed. This
     * may be called even if a preference is set to its existing value.
     * <p/>
     * <p>This callback will be run on your main thread.
     *
     * @param sharedPreferences The {@link SharedPreferences} that received
     *                          the change.
     * @param key               The key of the preference that was changed, added, or
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        AddNotification.getInstance().setSharedPreferences();
    }
}

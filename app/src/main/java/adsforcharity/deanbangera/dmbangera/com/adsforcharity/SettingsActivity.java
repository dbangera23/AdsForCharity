package adsforcharity.deanbangera.dmbangera.com.adsforcharity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = "MAIN ACTIVITY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(getString(R.string.notification_pref_key))) {
            Log.d(TAG, "onSharedPreferenceChanged: " + getString(R.string.notification_pref_key) + " to " + sharedPreferences.getBoolean(key,false));
        }
        if (key.equals(getString(R.string.notification_freq_key))) {
            Log.d(TAG, "onSharedPreferenceChanged: " + getString(R.string.notification_freq_key) + " to " + sharedPreferences.getString(key, getString(R.string.daily)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}

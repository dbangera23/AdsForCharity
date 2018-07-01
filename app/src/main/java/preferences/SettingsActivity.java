package preferences;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import adsforcharity.deanbangera.dmbangera.com.adsforcharity.AlarmReceiver;
import adsforcharity.deanbangera.dmbangera.com.adsforcharity.R;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "MAIN ACTIVITY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        //if you are using default SharedPreferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String timeKey = getString(R.string.notification_time_key);
        String time = sharedPrefs.getString(getString(R.string.notification_time_key), getString(R.string.notification_default_time));
        try {
            time = formatTimeBasedOnLocale(time);
        } catch (ParseException e) {
            Log.d(TAG, "onCreate Time Parse Exception: " + e.getMessage());
        }

        TimePreference timePreference = (TimePreference) findPreference(timeKey);
        timePreference.setSummary(time);
        onSharedPreferenceChanged(sharedPrefs, timeKey);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(getString(R.string.notification_pref_key)) && !sharedPreferences.getBoolean(key, false)) {
            Intent intent1 = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
            if (am != null) {
                am.cancel(pendingIntent);
            } else {
                Toast.makeText(this, R.string.alarm_cancel_fail, Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (key.equals(getString(R.string.notification_time_key))) {
            TimePreference timePreference = (TimePreference) findPreference(key);
            String time = sharedPreferences.getString(getString(R.string.notification_time_key), getString(R.string.notification_default_time));
            try {
                time = formatTimeBasedOnLocale(time);
            } catch (ParseException e) {
                Log.d(TAG, "onSharedPreferenceChanged Time Parse Exception: " + e.getMessage());
            }
            timePreference.setSummary(time);
        }

        Log.d(TAG, "onSharedPreferenceChanged: " + getString(R.string.notification_pref_key) + " = " + sharedPreferences.getBoolean(getString(R.string.notification_pref_key), false));
        Log.d(TAG, "onSharedPreferenceChanged: " + getString(R.string.notification_freq_key) + " = " + sharedPreferences.getString(getString(R.string.notification_freq_key), getString(R.string.daily)));
        setAlarm(sharedPreferences);
    }

    private String formatTimeBasedOnLocale(String inputTime) throws ParseException {
        // Read in time preference from memory and get ready to format based on locale
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.US);
        Date dateTime;

        dateTime = sdf.parse(inputTime);

        // Then convert date back to string and show to user.
        return DateFormat.getTimeFormat(this).format(dateTime);
    }

    private void setAlarm(SharedPreferences sharedPreferences) {

        String time = sharedPreferences.getString(getString(R.string.notification_time_key), getString(R.string.notification_default_time));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, TimePreference.getHour(time));
        calendar.set(Calendar.MINUTE, TimePreference.getMinute(time));
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        if (am != null) {
            String interval = sharedPreferences.getString(getString(R.string.notification_freq_key), getString(R.string.daily));
            if (interval.equals(getString(R.string.daily)))
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            if (interval.equals(getString(R.string.weekly)))
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            if (interval.equals(getString(R.string.bi_Weekly)))
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 14, pendingIntent);
            if (interval.equals(getString(R.string.monthly)))
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 30, pendingIntent);
        } else {
            Toast.makeText(this, R.string.alarm_set_fail, Toast.LENGTH_SHORT).show();
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

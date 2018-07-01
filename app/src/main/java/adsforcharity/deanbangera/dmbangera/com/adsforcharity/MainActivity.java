package adsforcharity.deanbangera.dmbangera.com.adsforcharity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import preferences.SettingsActivity;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {

    static final String CHANNEL_ID = "REMINDER_CHANNEL_ID";
    private static final String TAG = "MAIN ACTIVITY";
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createNotificationChannel();

        MobileAds.initialize(this, getString(R.string.ad_key));
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        // play button click listener
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                } else {
                    Snackbar.make(view, R.string.ad_not_loaded, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        // Update Ad count
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int highScore = sharedPref.getInt(getString(R.string.ad_count_key), 0);
        TextView ad_count_view = findViewById(R.id.adCount);
        ad_count_view.setText(String.valueOf(highScore));


        loadRewardedVideoAd();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadRewardedVideoAd() {
        Log.d(TAG, "loadRewardedVideoAd");
        mRewardedVideoAd.loadAd(getString(R.string.ad_key),
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem reward) {
        Log.d(TAG, "onRewarded");
        // Watched a video so lets increment the count
        incrementAdCount();
        Toast.makeText(this, R.string.watch_thanks, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.d(TAG, "onRewardedVideoAdLeftApplication");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.d(TAG, "onRewardedVideoAdClosed");
        // Load the next rewarded video ad.
        loadRewardedVideoAd();
        // Change visibility of views
        setVideoLoadedVisibility(false);
    }

    private void incrementAdCount() {
        // Update VIew
        TextView count_view = findViewById(R.id.adCount);
        int ad_count = Integer.parseInt(count_view.getText().toString());
        ad_count++;
        count_view.setText(String.valueOf(ad_count));
        // Update store
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.ad_count_key), ad_count);
        editor.apply();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Log.d(TAG, "onRewardedVideoAdFailedToLoad");
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Log.d(TAG, "onRewardedVideoAdLoaded");
        setVideoLoadedVisibility(true);
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.d(TAG, "onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.d(TAG, "onRewardedVideoStarted");
    }

    @Override
    public void onRewardedVideoCompleted() {
        Log.d(TAG, "onRewardedVideoCompleted");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    private void setVideoLoadedVisibility(boolean visibility) {
        if (visibility) {
            ProgressBar progressBar = findViewById(R.id.videoProgress);
            progressBar.setVisibility(View.INVISIBLE);
            TextView loadText = findViewById(R.id.loadText);
            loadText.setVisibility((View.INVISIBLE));

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            TextView loadedText = findViewById(R.id.loadedText);
            loadedText.setVisibility((View.VISIBLE));
        } else {
            ProgressBar progressBar = findViewById(R.id.videoProgress);
            progressBar.setVisibility(View.VISIBLE);
            TextView loadText = findViewById(R.id.loadText);
            loadText.setVisibility((View.VISIBLE));

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setVisibility(View.INVISIBLE);
            TextView loadedText = findViewById(R.id.loadedText);
            loadedText.setVisibility((View.INVISIBLE));
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            } else {
                Log.d(TAG, "createNotificationChannel: Failed");
            }
        }
    }
}

package adsforcharity.deanbangera.dmbangera.com.adsforcharity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import fragments.CharitiesFragment;
import fragments.VideoFragment;
import preferences.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements CharitiesFragment.OnListFragmentInteractionListener {

    static final String CHANNEL_ID = "REMINDER_CHANNEL_ID";
    private static final String TAG = "MAIN ACTIVITY";

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Fragment fragment;
                        switch (menuItem.getItemId()) {
                            case R.id.nav_video:
                                fragment = new VideoFragment();
                                break;
                            case R.id.nav_donate:
                                // TODO: implement donate fragment
                                fragment = new VideoFragment();
                                break;
                            case R.id.nav_charities:
                                fragment = new CharitiesFragment();
                                break;
                            case R.id.nav_settings:
                                Intent settingIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                                startActivity(settingIntent);
                                return true;
                            case R.id.nav_about:
                                // TODO: implement about fragment
                                fragment = new VideoFragment();
                                break;
                            default:
                                fragment = new VideoFragment();
                                break;
                        }
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.commit();

                        return true;
                    }
                });
        navigationView.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new VideoFragment())
                .commit();

        createNotificationChannel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onListFragmentInteraction() {
        Log.d(TAG, "onListFragmentInteraction: FRAGMENT TO ACTIVITY COMMUNICATION");
    }
}

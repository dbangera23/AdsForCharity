package fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import adsforcharity.deanbangera.dmbangera.com.adsforcharity.R;

public class VideoFragment extends Fragment implements RewardedVideoAdListener {

    private RewardedVideoAd mRewardedVideoAd;

    private static final String TAG = "VIDEO FRAGMENT";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.video_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        MobileAds.initialize(getContext(), getString(R.string.ad_key));
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();

        // play button click listener
        FloatingActionButton fab = view.findViewById(R.id.fab);
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
        Activity mainActivity = getActivity();
        if (mainActivity != null) {
            SharedPreferences sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE);
            int highScore = sharedPref.getInt(getString(R.string.ad_count_key), 0);
            TextView ad_count_view = view.findViewById(R.id.adCount);
            ad_count_view.setText(String.valueOf(highScore));
        }
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
        Toast.makeText(getContext(), R.string.watch_thanks, Toast.LENGTH_SHORT).show();
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
        // Update store
        Activity mainActivity = getActivity();
        if (mainActivity != null) {
            SharedPreferences sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE);
            int ad_count = 0;
            ad_count = sharedPref.getInt(getString(R.string.ad_count_key), ad_count);
            ad_count++;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.ad_count_key), ad_count);
            editor.apply();

            // Update View
            View view = getView();
            if (view != null) {
                TextView count_view = view.findViewById(R.id.adCount);
                if (count_view != null) {
                    count_view.setText(String.valueOf(ad_count));
                }
            }
        }
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
        mRewardedVideoAd.resume(getContext());
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        mRewardedVideoAd.pause(getContext());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mRewardedVideoAd.destroy(getContext());
        super.onDestroy();
    }

    private void setVideoLoadedVisibility(boolean visibility) {
        View view = getView();
        if (view != null) {
            if (visibility) {
                ProgressBar progressBar = view.findViewById(R.id.videoProgress);
                progressBar.setVisibility(View.INVISIBLE);
                TextView loadText = view.findViewById(R.id.loadText);
                loadText.setVisibility((View.INVISIBLE));

                FloatingActionButton fab = view.findViewById(R.id.fab);
                fab.setVisibility(View.VISIBLE);
                TextView loadedText = view.findViewById(R.id.loadedText);
                loadedText.setVisibility((View.VISIBLE));
            } else {
                ProgressBar progressBar = view.findViewById(R.id.videoProgress);
                progressBar.setVisibility(View.VISIBLE);
                TextView loadText = view.findViewById(R.id.loadText);
                loadText.setVisibility((View.VISIBLE));

                FloatingActionButton fab = view.findViewById(R.id.fab);
                fab.setVisibility(View.INVISIBLE);
                TextView loadedText = view.findViewById(R.id.loadedText);
                loadedText.setVisibility((View.INVISIBLE));
            }
        }
    }
}

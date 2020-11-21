package com.icurety;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.AdListener;

public class AndroidAd extends AdManager {
    public static final String TAG = "AndroidAd";

    private AndroidLauncher activity;
    private RewardedAd ad;

    public AndroidAd(ClickToWin ctw, AndroidLauncher activity)
    {
        super(ctw);
        this.activity = activity;
    }


    @Override
    public void load() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("INFO", "Loading Ads...");
                ad = new RewardedAd(activity, "ca-app-pub-3940256099942544/5224354917");
                RewardedAdLoadCallback callback = new RewardedAdLoadCallback() {
                    public void onRewardedAdFailedToLoad(LoadAdError var1) {
                        Log.i(TAG, "Failed to load add " + var1);
                    }
                };
                ad.loadAd(new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
                    @Override
                    public void onRewardedAdLoaded() {
                        // Ad successfully loaded.
                        Log.i(TAG, "RewardAd Loaded!");
                        activity.powerUpButton.setEnabled(true);
                    }

                    @Override
                    public void onRewardedAdFailedToLoad(LoadAdError adError) {
                        // Ad failed to load.
                        Log.i(TAG, "RewardAd Couldn't load!");
                    }
                });
            }
        });

    }

    @Override
    public void show() {
        if(ad != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (ad.isLoaded()) {
                        RewardedAdCallback callback = new RewardedAdCallback() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                Log.i(TAG, "YUHUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU Reward!");
                                ctw.spawnPowerUp();
                            }

                            public void onRewardedAdOpened() {
                                ctw.pause();
                                Log.i(TAG, "Ad Opened");
                            }

                            public void onRewardedAdClosed() {
                                Log.i(TAG, "Ad Closed");
                                activity.powerUpButton.setEnabled(false);
                                load();
                                ctw.unpause();
                            }

                            public void onRewardedAdFailedToShow(AdError var1) {
                                Log.i(TAG, "Ad failed to show");
                            }
                        };
                        ad.show(activity, callback);
                    } else {
                        Log.i(TAG, "Ad not loaded");
                    }
                }
            });
        }
    }
}

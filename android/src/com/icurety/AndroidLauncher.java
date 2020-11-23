package com.icurety;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.icurety.ClickToWin;
import com.google.android.gms.ads.AdListener;

import java.util.Arrays;

public class AndroidLauncher extends AndroidApplication {

	public Button powerUpButton;

	private ClickToWin ctw;
	private AdView adView;

	RewardedAd rewardedAd;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		final AndroidAd ad = new AndroidAd(null, this);
		ctw = new ClickToWin(new AndroidSaveSystem(this), ad);
		Log.i("INFO", "THIS IS SOME VERY IMPORTANT CLASS:_ HERE IT IS: " + this.getPreferences(MODE_PRIVATE));
		ad.ctw = ctw;
		View gameView = initializeForView(ctw, config);

		RelativeLayout layout = new RelativeLayout(this);

		layout.addView(gameView);

		powerUpButton = new Button(this);
		powerUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.i("AndroidLauncher", "CLCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCLIOIIIGIGGIGN!");
				ad.show();
			}
		});
		powerUpButton.setText("Earn PowerUp");
		powerUpButton.setEnabled(false);
		layout.addView(powerUpButton);
		ad.load();
		setContentView(layout);

	}
}

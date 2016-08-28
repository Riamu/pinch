package com.liamhartery.pinch;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class AndroidLauncher extends AndroidApplication implements AdsController{

	// TODO PRODUCTION Switch these IDs for productions
	private static final String BANNER_AD_UNIT_ID_TEST = "ca-app-pub-3940256099942544/6300978111";
	private static final String BANNER_AD_UNIT_ID_REAL = "ca-app-pub-1465882120648049/7880779010";

	private static final String BANNER_AD_UNIT_ID = BANNER_AD_UNIT_ID_TEST;
	AdView bannerAd;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new PinchGame(this),config);
		setupAds();

		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(
				gameView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT
				);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layout.addView(bannerAd,params);
		setContentView(layout);
		//MobileAds.initialize(getApplicationContext(),"ca-app-pub-1465882120648049~9497113016");

	}
	public void setupAds(){
		bannerAd = new AdView(this);
		bannerAd.setVisibility(View.INVISIBLE);
		bannerAd.setBackgroundColor(0xff000000);
		bannerAd.setAdUnitId(BANNER_AD_UNIT_ID);
		bannerAd.setAdSize(AdSize.SMART_BANNER);
	}

	@Override
	public void showBannerAd(){
		runOnUiThread(new Runnable(){
			@Override
			public void run(){
				bannerAd.setVisibility(View.VISIBLE);
				AdRequest.Builder builder = new AdRequest.Builder();
				AdRequest ad = builder.build();
				bannerAd.loadAd(ad);
			}
		});
	}
	@Override
	public void hideBannerAd(){
		runOnUiThread(new Runnable(){
			@Override
			public void run(){
				bannerAd.setVisibility(View.INVISIBLE);
			}
		});

	}
}

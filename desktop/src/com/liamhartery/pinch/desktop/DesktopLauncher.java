package com.liamhartery.pinch.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.liamhartery.pinch.AdsController;
import com.liamhartery.pinch.PinchGame;

public class DesktopLauncher
{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new PinchGame(new AdsController() {
			@Override
			public void showBannerAd() {

			}

			@Override
			public void hideBannerAd() {

			}
		}), config);
	}
}

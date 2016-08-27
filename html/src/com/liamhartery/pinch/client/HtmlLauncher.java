package com.liamhartery.pinch.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.liamhartery.pinch.AdsController;
import com.liamhartery.pinch.PinchGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new PinchGame(new AdsController() {
                        @Override
                        public void showBannerAd() {
                                // do nothing
                        }

                        @Override
                        public void hideBannerAd() {
                                // do nothing
                        }
                });
        }
}
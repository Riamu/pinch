package com.liamhartery.pinch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// URGENT is marked with [*]

// General
// [*]TODO At least 15 levels for release (not including tutorial levels) (5/15)
// [*]TODO Icons for power-ups ??? Necessary now?
// TODO make the little "which floor am I on?" icon


// Player
// [*]TODO Get CC0 player image (size no longer matters, we've figured out setSize()
// TODO more thinking about how pinching or zooming into void affects the game-play

// Enemies
// TODO make the enemies collide with the player and vice versa?
// TODO Enemies look really silly not colliding with chests and things... Maybe change that?
// I kind of want to add an enemy that uses projectiles but I'm unsure

// Known Bugs
// can sometimes clip out of the confines of the map
// pressing back while on the ButtonScreen quits out of the app.
// enemies don't collide with: doors,chests,enemies
// Dialogs are weird sizes

// RELEASE CHECKLIST
// Update version code in manifest
// Update other version numbers in build.gradle
// ensure devmode is off
// ensure the proper ad unit ID is selected

public class PinchGame extends Game {
	public SpriteBatch batch;
	public BitmapFont bigfont;
	public BitmapFont font;
	public float timer;
	public AdsController adsController;
	public PinchGame(AdsController adsController){
		this.adsController = adsController;
	}
	public boolean music = true;
	public float musicVolume;
	public boolean soundEffects = true;
	public float soundEffectVolume;
	public Music musicFile;
	@Override
	public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/data_control.fnt"));
		bigfont = new BitmapFont(Gdx.files.internal("fonts/codex.fnt"));
		adsController.hideBannerAd();
		musicFile = Gdx.audio.newMusic(Gdx.files.internal("sound/music/music.mp3"));

		// Change screens to the menu screen
		this.setScreen(new com.liamhartery.pinch.screens.MainMenuScreen(this));
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		bigfont.dispose();
	}
}

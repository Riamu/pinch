package com.liamhartery.pinch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


// URGENT is marked with [*]

// General
// [*]TODO Music
// [*]TODO At least 15 levels for release (not including tutorial levels) (5/15)
// [*]TODO Figure out how to display text mid-game and make a tutorial
// TODO Icons for power-ups
// TODO make the little "which floor am I on?" icon
// TODO think about changing how power-ups work:
//     Might do a thing where you only can have X number of powerups and you have to choose to take
//     a chest or leave it because your oldest powerup will get knocked out of the list if you do
//     take a new one.

// Player
// [*]TODO Get CC0 player image (size no longer matters, we've figured out setSize()
// TODO Decide about projectile mechanics
// TODO more thinking about how pinching or zooming into void affects the game-play

// Enemies
// TODO make the enemies collide with the player and vice versa?
// TODO Enemies look really silly colliding with chests and things... Maybe change that?
// I kind of want to add an enemy that uses projectiles but I'm unsure

// Sounds - Code is already here for most of these, just have to change the .mp3 files around
// TODO Enemy Hit Sound
// TODO Player Hit Sound
// TODO better chest open sound
// TODO projectile shoot sound
// TODO Game Over Jingle
// TODO Win Jingle
// TODO Reset Jingle

// Known Bugs
// can sometimes clip out of the confines of the map
// pressing back while on the ButtonScreen quits out of the app.

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
	@Override
	public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/data_control.fnt"));
		bigfont = new BitmapFont(Gdx.files.internal("fonts/codex.fnt"));
		adsController.hideBannerAd();
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

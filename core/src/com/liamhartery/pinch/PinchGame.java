package com.liamhartery.pinch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


// URGENT is marked with [*]

// General
// [*]TODO Main menu Buttons
// [*]TODO Music
// [*]TODO AdMob Integration
// [*]TODO At least 15 levels for release (not including tutorial levels)
// [*]TODO Figure out how to display text mid-game and make a tutorial

// Player
// [*]TODO Get better 16x16 Player image
// TODO Decide about projectile mechanics


// Enemies
// TODO make the enemies collide with the player and vice versa?
// I kind of want to add an enemy that uses projectiles but I'm unsure


// Known Bugs
// player sprite is too large
// can sometimes clip out of the confines of the map

public class PinchGame extends Game {
	public SpriteBatch batch;
	public BitmapFont bigfont;
	public BitmapFont font;
	public float timer;
	@Override
	public void create () {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/data_control.fnt"));
		bigfont = new BitmapFont(Gdx.files.internal("fonts/codex.fnt"));

		// Change screens to the menu screen
		this.setScreen(new com.liamhartery.pinch.screens.MainMenuScreen(this));
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

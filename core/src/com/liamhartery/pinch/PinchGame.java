package com.liamhartery.pinch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


// URGENT is marked with [*]

// General
// TODO Main menu Buttons

// Player
// TODO Get better 16x16 Player image
// [*]TODO inventory / Powerups
// [*]TODO Key and locked door mechanic
// TODO Decide about projectile mechanics

// Enemies
// TODO Add more enemies
    // BlobEnemy
    // TODO Better movement


// Known Bugs
// player sprite is too large

public class PinchGame extends Game {
	public SpriteBatch batch;
	public BitmapFont bigfont;
	public BitmapFont font;

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

package com.liamhartery.pinch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


// URGENT is marked with [*]
// FINISHED marked with [^]
// General
// TODO make more enemies
// [*]TODO Dynamic level loading and progression
// TODO re-work winning
// TODO Main menu Buttons

// Player
// TODO Get better 16x16 Player image
// [*]TODO give the player an attack
// [*]TODO inventory / Powerups
// [*]TODO Key and locked door mechanic

// BlobEnemy
// TODO Better movement
// TODO allow blob to die

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

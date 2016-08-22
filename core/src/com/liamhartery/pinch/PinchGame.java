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
// [*]TODO Key and locked door mechanic
// TODO Decide about projectile mechanics

// Enemies
// TODO Add more enemies
    // BlobEnemy
    // TODO Better movement


// Known Bugs
// player sprite is too large
// timer isn't working properly

/*
 * Changes in this version:
 *     Projectile is now on a death timer
 *     Re-organized some more code
 *     Fixed Projectiles colliding with things on different layers from it
 *     Added items, chests will now give you a randomly chosen item from all possible items
 *         might change this so certain items are more common than others but the current system is
 *         fine for now
 */
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

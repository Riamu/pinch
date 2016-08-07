package com.liamhartery.pinch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PinchGame extends Game {
	public SpriteBatch batch;
	public BitmapFont bigfont;
	public BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();

		font = new BitmapFont();
		bigfont = new BitmapFont();
		bigfont.getData().setScale(3,3);

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

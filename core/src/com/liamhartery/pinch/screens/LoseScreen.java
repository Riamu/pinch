package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.PinchGame;

/*
 * This is the screen that is shown when the player loses all his health
 * usually called through the lose() method in GameScreen
 */
public class LoseScreen implements Screen, GestureDetector.GestureListener {
    private final PinchGame game;
    private OrthographicCamera camera;
    private int screenWidth, screenHeight;
    private GlyphLayout bigLayout, lilLayout;
    private float bigWidth, lilWidth;

    public LoseScreen(final PinchGame pinch) {
        game = pinch;

        // camera setup
        screenWidth = 800;
        screenHeight = 480;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,screenWidth,screenHeight);

        // this is so we can fix fonts up nice
        bigLayout = new GlyphLayout(game.bigfont,"g a m e");
        bigWidth = bigLayout.width;

        // This is so we can use gestures
        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(33/255f,30/255f,39/255f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // update the camera and look at stuff using the camera
        camera.update();
        // game is called because we use the batch created in PinchGame
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
            game.bigfont.draw(game.batch,"g a m e",(screenWidth-bigWidth)/2,320);
            game.bigfont.draw(game.batch,"o v e r",(screenWidth-bigWidth)/2,260);

            lilLayout = new GlyphLayout(game.font,"Tap to return to the main menu");
            lilWidth = lilLayout.width;
            game.font.draw(game.batch,"Tap to return to the main menu",(screenWidth-lilWidth)/2,180);
            lilLayout = new GlyphLayout(game.font,"Long-press to quit");
            lilWidth = lilLayout.width;
            game.font.draw(game.batch,"Long-press to quit",(screenWidth-lilWidth)/2,140);
        game.batch.end();
    }
    @Override
    public boolean longPress(float x, float y) {
        dispose();
        Gdx.app.exit();
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        game.setScreen(new MainMenuScreen(game));
        dispose();
        return false;
    }


    /*
     * Unused Methods
     */

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
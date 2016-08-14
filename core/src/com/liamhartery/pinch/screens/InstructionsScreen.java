package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.PinchGame;

public class InstructionsScreen implements Screen,GestureDetector.GestureListener {
    private final PinchGame game;
    private OrthographicCamera camera;
    private int screenWidth, screenHeight;
    private final GlyphLayout bigLayout;
    private float bigWidth, lilWidth;

    public InstructionsScreen(final PinchGame pinch){
        game = pinch;

        screenWidth = 800;
        screenHeight = 480;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,screenWidth,screenHeight);

        game.bigfont.getData().setScale(0.8f,0.8f);
        bigLayout = new GlyphLayout(game.bigfont,"instructions");
        bigWidth = bigLayout.width;

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        game.setScreen(new GameScreen(game));
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
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

    @Override
    public void show() {

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
        game.bigfont.draw(game.batch, "instructions",(screenWidth-bigWidth)/2,450);
        game.font.draw(game.batch,"1. George will follow your finger",20,370);
        game.font.draw(game.batch,"2. Pinching will make you go up one floor",20,340);
        game.font.draw(game.batch,"3. Zooming will make you go down one floor",20,310);
        game.font.draw(game.batch,"4. Tap the screen to interact with things next to you",20,280);
        game.font.draw(game.batch,"5. Win by tapping while standing next to the door",20,250);
        game.font.draw(game.batch,"6. Tap now to play",20,220);
        // License: https://creativecommons.org/licenses/by/3.0/
        game.font.draw(game.batch,"Character by Sheep: http://opengameart.org/users/sheep",20,20);
        game.batch.end();
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
}

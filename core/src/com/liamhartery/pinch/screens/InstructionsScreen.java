package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.PinchGame;

// TODO make instruction screen prettier

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

        game.bigfont.getData().setScale(3,3);
        game.font.getData().setScale(1,1);
        bigLayout = new GlyphLayout(game.bigfont,"INSTRUCTIONS");
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
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update the camera and look at stuff using the camera
        camera.update();
        // game is called because we use the batch created in PinchGame
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.bigfont.draw(game.batch, "INSTRUCTIONS",(screenWidth-bigWidth)/2,450);
        game.font.getData().setScale(2,2);
        game.font.draw(game.batch,"1. George will follow your finger",20,400);
        game.font.draw(game.batch,"2. Pinching will make you go up one floor",20,370);
        game.font.draw(game.batch,"3. Zooming will make you go down one floor",20,340);
        game.font.draw(game.batch,"4. {Continue Instructions}",20,310);
        game.font.draw(game.batch,"5. Tap to go back to the game",20,280);
        game.font.getData().setScale(1,1);
        game.font.draw(game.batch,"V e r s i o n :  0 . 0 . 5",600,450);
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

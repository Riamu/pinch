package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
        bigLayout = new GlyphLayout(game.bigfont,"controls");
        bigWidth = bigLayout.width;

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

        // BATCHES YET AGAIN WOOOOOOO
        game.batch.begin();
            game.bigfont.draw(game.batch, "controls",(screenWidth-bigWidth)/2,450);
            game.font.draw(game.batch,"1. Hold down your finger until George starts to follow, then you can drag and he will follow",20,360);
            game.font.draw(game.batch,"2. Stand on top of things and tap the screen to interact with them.",20,330);
            game.font.draw(game.batch,"3. Pinching and Zooming (pinchzooming) will allow you to change floors",20,300);
            game.font.draw(game.batch,"4. Follow the tutorial to learn more",20,270);
            game.font.draw(game.batch,"5. Tap the screen to go back.",20,240);
            // License: https://creativecommons.org/licenses/by/3.0/
            game.font.draw(game.batch,"Character by Sheep: http://opengameart.org/users/sheep",20,100);
        game.batch.end();
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            game.setScreen(new ButtonScreen(game));
            dispose();
        }
    }
    @Override
    public boolean tap(float x, float y, int count, int button) {
        game.setScreen(new ButtonScreen(game));
        dispose();
        return false;
    }

    /*
     * unused
     */
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
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

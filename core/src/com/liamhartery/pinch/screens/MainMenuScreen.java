package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.liamhartery.pinch.*;

public class MainMenuScreen implements Screen, GestureDetector.GestureListener{
    private final PinchGame game;
    private OrthographicCamera camera;
    private int screenWidth, screenHeight;
    private final GlyphLayout bigLayout, lilLayout;
    private float bigWidth, lilWidth;

    /*
        The constructor in a Screen acts similarly to the create() method
        in libGDX
     */
    @Override
    public void dispose(){
    }

    public MainMenuScreen(final PinchGame pinch){
        game = pinch;
        game.adsController.showBannerAd();
        // camera stuff
        screenWidth = 800;
        screenHeight = 480;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,screenWidth,screenHeight);

        // this is so we can dick around with fonts
        game.bigfont.getData().setScale(1,1);
        game.font.getData().setScale(0.5f,0.5f);
        bigLayout = new GlyphLayout(game.bigfont,"p i n c h");
        bigWidth = bigLayout.width;
        lilLayout = new GlyphLayout(game.font,"Tap to Begin");
        lilWidth = lilLayout.width;

        // This is so we can use gestures
        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(33/255f,30/255f,39/255f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update the camera and look at stuff using the camera
        camera.update();

        // game is called because we use the batch created in PinchGame
        game.batch.setProjectionMatrix(camera.combined);

        // draw our fonts
        game.batch.begin();
            game.bigfont.draw(game.batch, "p i n c h",(screenWidth-bigWidth)/2,280);
            game.font.draw(game.batch,"Tap to Begin",(screenWidth-lilWidth)/2,150);
            game.font.draw(game.batch,"Version 0.3.0",650,450);
        game.batch.end();
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            dispose();
            Gdx.app.exit();
        }
    }

    // if tapped we go to the ButtonScreen (really should be called MainMenu but suck it.)
    @Override
    public boolean tap(float x, float y, int count, int button) {
        game.setScreen(new ButtonScreen(game));
        game.adsController.showBannerAd();
        dispose();
        return false;
    }



    /*
     * Unused
     */
    @Override
    public void show(){

    }

    @Override
    public void hide(){

    }

    @Override
    public void resume(){

    }
    @Override
    public void resize(int x, int y){

    }

    @Override
    public void pause(){

    }

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
}

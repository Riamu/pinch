package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.liamhartery.pinch.*;

// TODO Main Menu Buttons!
public class MainMenuScreen extends Stage implements Screen, GestureDetector.GestureListener{
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
        // camera stuff
        screenWidth = 800;
        screenHeight = 480;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,screenWidth,screenHeight);

        // this is so we can dick around with fonts
        game.bigfont.getData().setScale(3,3);
        game.font.getData().setScale(1,1);
        bigLayout = new GlyphLayout(game.bigfont,"P I N C H");
        bigWidth = bigLayout.width;
        lilLayout = new GlyphLayout(game.font,"Tap to begin");
        lilWidth = lilLayout.width;

        // This is so we can use gestures
        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update the camera and look at stuff using the camera
        camera.update();
        // game is called because we use the batch created in PinchGame
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
            game.bigfont.draw(game.batch, "P I N C H",(screenWidth-bigWidth)/2,280);
            game.font.draw(game.batch,"Tap to begin",(screenWidth-lilWidth)/2,200);
            game.font.draw(game.batch,"V e r s i o n :  0 . 0 . 5",600,450);
            // License: https://creativecommons.org/licenses/by/3.0/
            game.font.draw(game.batch,"Character by Sheep: http://opengameart.org/users/sheep",20,20);
        game.batch.end();

    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        game.setScreen(new InstructionsScreen(game));
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

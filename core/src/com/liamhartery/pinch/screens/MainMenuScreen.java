package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.liamhartery.pinch.*;

public class MainMenuScreen implements Screen{
    final PinchGame game;
    OrthographicCamera camera;
    int screenWidth, screenHeight;
    final GlyphLayout bigLayout, lilLayout;
    float bigWidth, lilWidth;

    /*
        The constructor in a Screen acts similarly to the create() method
        in libGDX
     */
    public MainMenuScreen(final PinchGame pinch){
        screenWidth = 800;
        screenHeight = 480;
        game = pinch;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,screenWidth,screenHeight);

        /*
            this gives us some values so that we can center our fonts
            TODO probably bad and I'll change this later
        */
        bigLayout = new GlyphLayout(game.bigfont,"P I N C H");
        bigWidth = bigLayout.width;
        lilLayout = new GlyphLayout(game.font,"Touch to Begin");
        lilWidth = lilLayout.width;
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
            game.font.draw(game.batch,"Touch to Begin",(screenWidth-lilWidth)/2,200);
        game.batch.end();

        if(Gdx.input.isTouched()){
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void dispose(){

    }

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
}

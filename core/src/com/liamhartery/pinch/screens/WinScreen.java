package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.PinchGame;

public class WinScreen implements Screen,GestureDetector.GestureListener {
    final private PinchGame game;
    private int screenWidth, screenHeight;
    private GlyphLayout bigLayout, lilLayout, lilLayout2;
    float lilWidth,bigWidth;
    private OrthographicCamera camera;
    private float time;
    public WinScreen(final PinchGame pinch, float time) {
        game = pinch;

        this.time = time;
        // camera setup
        screenWidth = 800;
        screenHeight = 480;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,screenWidth,screenHeight);

        // message setups
        bigLayout = new GlyphLayout(game.bigfont,"Congratulations you've won!");
        game.font.getData().setScale(2,2);

        // This is so we can use gestures
        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
            game.bigfont.draw(game.batch,bigLayout,(screenWidth-bigLayout.width)/2,280);
            game.font.draw(game.batch,"Final Time: "+time,100,200);
            game.font.draw(game.batch,"Tap to return to menu, long-press to exit",100,160);
        game.batch.end();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        game.setScreen(new MainMenuScreen(game));
        dispose();
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        dispose();
        Gdx.app.exit();
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

}

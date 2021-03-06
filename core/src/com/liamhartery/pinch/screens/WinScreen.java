package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.PinchGame;

public class WinScreen implements Screen,GestureDetector.GestureListener {
    final private PinchGame game;
    private int screenWidth, screenHeight;
    private GlyphLayout bigLayout,lilLayout;
    private OrthographicCamera camera;

    // time in this case is the player's final time
    private float time;
    private Sound winJingle = Gdx.audio.newSound(
            Gdx.files.internal("sound/effects/winJingle.mp3"));

    public WinScreen(final PinchGame pinch, float time) {
        game = pinch;

        this.time = time;
        // camera setup
        screenWidth = 800;
        screenHeight = 480;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,screenWidth,screenHeight);

        // message setups
        bigLayout = new GlyphLayout(game.bigfont,"w i n");
        // This is so we can use gestures
        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
        if(game.soundEffects)
            winJingle.play(0.5f);
        game.musicFile.setLooping(false);
    }

    @Override
    public void dispose() {
        game.timer=0;
        winJingle.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(33/255f,30/255f,39/255f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // draw e'erythang
        game.batch.begin();
            game.font.getData().setScale(0.5f);
            game.bigfont.draw(game.batch,bigLayout,(screenWidth-bigLayout.width)/2,290);
            lilLayout = new GlyphLayout(game.font,"Final Time: "+time);
            game.font.draw(game.batch,"Final Time: "+time,(screenWidth-lilLayout.width)/2,200);
            lilLayout = new GlyphLayout(game.font,"Tap to return to menu, long-press to exit");
            game.font.draw(game.batch,"Tap to return to menu, long-press to exit",
                    (screenWidth-lilLayout.width)/2,160);
        game.batch.end();
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            game.setScreen(new ButtonScreen(game));
            dispose();
        }
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

    /*
     * unused
     */
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
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1,
                         Vector2 pointer2) {
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

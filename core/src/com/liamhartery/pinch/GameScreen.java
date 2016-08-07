package com.liamhartery.pinch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.liamhartery.pinch.entities.Player;

//TODO Implement .tmx map files
    //TODO implement hit detection using .tmx files
public class GameScreen implements Screen,GestureListener {
    final PinchGame game;
    private String message = "";
    Texture playerImage;
    Player player;

    OrthographicCamera camera;

    Vector3 touchPos = new Vector3();

    // constructor is just like the create() method
    public GameScreen(final PinchGame pinch) {
        game = pinch;

        // load images
        playerImage = new Texture(Gdx.files.internal("player.jpg"));

        // create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,480);

        // create a Rectangle to logically represent Player
        player = new Player(playerImage);
        player.setPosition(200,200);

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Camera needs to be told to update
        camera.update();

        // Set the sprite batch to render using the camera's coordinates
        game.batch.setProjectionMatrix(camera.combined);

        // Sprite Batch
        game.batch.begin();
        game.font.draw(game.batch,message,50,50);
        game.batch.draw(playerImage,player.getX(),player.getY());
        game.batch.end();


        //TODO Fix fling() and isTouched() interfering with each other
        if(Gdx.input.isTouched()){
            // If more than one finger is on the screen do nothing
            if((Gdx.input.isTouched(1))||(Gdx.input.isTouched(2))){

            }else {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                player.moveTowards(touchPos, Gdx.graphics.getDeltaTime());
            }
        }

    }
    // Gesture Handlers that are actually used

    @Override
    public boolean zoom(float initialDistance, float distance) {
        message = "Zoom performed, initial Distance:" + Float.toString(initialDistance) +
                " Distance: " + Float.toString(distance);
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                         Vector2 pointer1, Vector2 pointer2) {
        message = "Pinch performed";
        return true;
    }

    @Override
    public void pinchStop(){
        return;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        message = "Fling performed, velocity:" + Float.toString(velocityX) +
                "," + Float.toString(velocityY);
        return true;
    }

    // None of the following are used
    @Override
    public void show(){

    }

    @Override
    public void hide(){

    }

    @Override
    public void pause(){

    }
    @Override
    public void resume(){
    }

    @Override
    public void resize(int x, int y){

    }

    @Override
    public void dispose(){
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return true;
    }

    @Override
    public boolean panStop(float a, float b, int c, int d){
        return true;
    }
}

package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.liamhartery.pinch.PinchGame;
import com.liamhartery.pinch.entities.Player;

//TODO enable switching layers of the dungeon
//TODO enable level loading
//TODO create enemies
public class GameScreen implements Screen,GestureListener {
    private final PinchGame game;
    private String message = "";
    private int currentLayer = 1;
    private Texture playerImage;
    private TextureRegion playerRegion;
    private Player player;
    private TiledMapTileLayer.Cell cell;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private OrthographicCamera camera;

    private Vector3 touchPos = new Vector3();

    // dispose any resource that needs disposing of
    @Override
    public void dispose(){
        tiledMap.dispose();
        playerImage.dispose();
        game.dispose();

    }
    // constructor is just like the create() method
    public GameScreen(final PinchGame pinch) {
        game = pinch;

        // load images
        playerImage = new Texture(Gdx.files.internal("player.jpg"));

        // create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false,320,180);

        //Load the map
        tiledMap = new TmxMapLoader().load("levels/testlevel1/level1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // create a new Player object for the player using the playerImage
        player = new Player(playerImage,(TiledMapTileLayer)tiledMap.getLayers().get(currentLayer));

        // fucking make sure that the camera gets centred on the player at the beginning
        // otherwise it fucks shit up
        // this is a shitty fucking work around but I'm done with it now
        camera.position.x = 150;
        camera.position.y = 150;
        player.setPosition(150,150);
        // reset the font size because our camera actually changed
        game.font.getData().setScale(0.5f,0.5f);

        // set the gesture detector and input processor to that gesture detector
        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0.13f,0.7f,0.17f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Camera needs to be told to update
        camera.update();

        // Set the sprite batch to render using the camera's coordinates
        game.batch.setProjectionMatrix(camera.combined);

        // Render the map (again using the camera)
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        // Sprite Batch
        game.batch.begin();
            // we draw the player using it's midpoint instead of bottom left
            game.batch.draw(playerImage,
                player.getX()-player.getWidth()/2,
                player.getY()-player.getWidth()/2);
        game.batch.end();


        //TODO Fix fling() and isTouched() interfering with each other
        if(Gdx.input.isTouched()){
            // If more than one finger is on the screen do nothing
            if((Gdx.input.isTouched(1))){
                // do nothing
            }else {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                player.moveTowards(touchPos, Gdx.graphics.getDeltaTime());
            }
        }else{
            player.setSpeed(0f);
        }

        /* moving the camera when the player comes too close to the edge
         * Yo so when it's hitting one of the x bounds we translate based on player speed in dir
         * same thing for the y axis
         */
        // TODO make this less choppy
        // TODO adjust how close to the edge the player must be
        // TODO fix screen tearing (so far only happens on Samsung S5 Neo)
        // player is on the right bound
        if(player.pos.x-camera.position.x>camera.viewportWidth-camera.viewportWidth/1.2){
            camera.translate((player.getSpeed()*player.getDir().x)*delta,
                    0);
        // player is on the left bound
        }
        else if(player.pos.x-camera.position.x<camera.viewportWidth/1.2-camera.viewportWidth){
            camera.translate((player.getSpeed()*player.getDir().x)*delta,
                    0);
        }
        // player is on the bottom bound
        if(player.pos.y-camera.position.y>camera.viewportHeight-camera.viewportHeight/1.2){
            camera.translate(0,
                    (player.getSpeed()*player.getDir().y)*delta);
        // player is on the top bound
        }
        else if(player.pos.y-camera.position.y<camera.viewportHeight/1.2-camera.viewportHeight){
            camera.translate(0,
                    (player.getSpeed()*player.getDir().y)*delta);
        }
    }

    /*
     * Gesture handlers that are actually used
     * some of them aren't you see...
     */
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

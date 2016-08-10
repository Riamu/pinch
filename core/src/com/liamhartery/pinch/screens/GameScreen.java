package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.liamhartery.pinch.PinchGame;
import com.liamhartery.pinch.entities.Player;

//TODO enable switching layers of the dungeon
//TODO enable level loading and progression
//TODO create enemies
public class GameScreen implements Screen,GestureListener{
    private final PinchGame game;
    private String message = "";
    private int currentLayer = 3;
    private Texture playerImage;
    private Player player;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private final OrthographicCamera camera;

    private Vector3 touchPos = new Vector3();

    private float elapsedTime = 0;

    private float pinchDistance;

    private boolean longPressBool = false;

    // dispose any resource that needs disposing of
    @Override
    public void dispose(){
        tiledMap.dispose();
        playerImage.dispose();
        player.dispose();

    }
    // constructor is just like the create() method
    public GameScreen(final PinchGame pinch) {
        game = pinch;

        // load images
        playerImage = new Texture(Gdx.files.internal("entities/player.jpg"));

        // create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false,320,180);

        //Load the map
        tiledMap = new TmxMapLoader().load("levels/testlevel1/level2.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // create a new Player object for the player using the playerImage
        player = new Player(playerImage,(TiledMapTileLayer)tiledMap.getLayers().get(currentLayer),6,this);

        // fucking make sure that the camera gets centred on the player at the beginning
        // otherwise it fucks shit up
        // this is a shitty fucking work around but I'm done with it now
        camera.position.x = 166;
        camera.position.y = 166;
        player.setPosition(166,166);
        // reset the font size because our camera actually changed
        game.font.getData().setScale(0.5f,0.5f);

        // set the gesture detector and input processor to that gesture detector
        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);

        // this is a hack to find the winning tile
    }

    @Override
    public void render(float delta){
        // elapsed time for the animation and score tracking
        elapsedTime += delta;
        Gdx.gl.glClearColor(13/255f,7/255f,17/255f,0);
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
            // TODO get a freakin' 16x16 spritesheet
            game.batch.draw(player.getAnimation().getKeyFrame(elapsedTime,true),
                player.getX()-player.getWidth()/2-5,
                player.getY()-player.getHeight()/2);
            // draw hearts on top corner of screen
            for(int i=0; i<player.hearts.length; i++){
                game.batch.draw(player.hearts[i],camera.position.x-130+(9*i),camera.position.y+70);
            }
            // FPS counter
            // game.font.draw(game.batch,"FPS: "+1f/delta,camera.position.x-144,camera.position.y+86);
        game.batch.end();

        //TODO Fix fling() and isTouched() interfering with each other
        // If the screen is touched with 1 finger we move the player towards that point
        if(Gdx.input.isTouched()){
            // If more than one finger is on the screen do nothing (most likely a pinch or zoom)
            if((Gdx.input.isTouched(1))){
                // do nothing
            }else {
                // we moveTowards the unprojected touchPos here
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                player.moveTowards(touchPos, delta);
            }
        }else{
            player.setIdle();
        }

        /* moving the camera when the player comes too close to the edge
         * Yo so when it's hitting one of the x bounds we translate based on player speed with dir
         * same thing for the y axis
         * TODO make this a method?
         */
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

        // check if the player is dead
        if(player.getHealth()<=0){
            lose();
        }
    }
    // called whenever a pinchstop is registered,
    // takes the distance fingers travelled for pinch/zoom gesture
    // TODO check if you're in a void tile and if you are, die
    public void changeFloor(float distance){
        tiledMap.getLayers().get(1).setVisible(true);
        if(distance<0){
            if(currentLayer-2>=1) {
                tiledMap.getLayers().get(currentLayer).setVisible(false);
                tiledMap.getLayers().get(currentLayer-1).setVisible(false);
                currentLayer -= 2;
                tiledMap.getLayers().get(currentLayer).setVisible(true);
                tiledMap.getLayers().get(currentLayer-1).setVisible(true);
                player.updateLayer((TiledMapTileLayer) tiledMap.getLayers().get(currentLayer));
                if(player.isNextToVoid()){
                    player.kill();
                    lose();
                }
            }
        }else{
            if(currentLayer+2<tiledMap.getLayers().getCount()) {
                tiledMap.getLayers().get(currentLayer).setVisible(false);
                tiledMap.getLayers().get(currentLayer-1).setVisible(false);
                currentLayer += 2;
                tiledMap.getLayers().get(currentLayer).setVisible(true);
                tiledMap.getLayers().get(currentLayer-1).setVisible(true);
                player.updateLayer((TiledMapTileLayer) tiledMap.getLayers().get(currentLayer));
                if(player.isNextToVoid()){
                    player.kill();
                    lose();
                }
            }
        }
    }
    public void win(){
        game.setScreen(new WinScreen(game,elapsedTime));
        dispose();
    }
    public void lose(){
        game.setScreen(new LoseScreen(game));
        dispose();
    }
    /*
     * Gesture handlers that are actually used
     * some of them aren't you see...
     */

    @Override
    public boolean longPress(float x, float y) {
        longPressBool = true;
        return true;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        pinchDistance = initialDistance-distance;
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                         Vector2 pointer1, Vector2 pointer2) {
        return true;
    }

    @Override
    public void pinchStop(){
        changeFloor(pinchDistance);
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Gdx.app.log("tapped!","");
        /*
        TiledMapTileLayer tempLayer = (TiledMapTileLayer)(tiledMap.getLayers().get(currentLayer));
        if(tempLayer.getCell((int)(x/tempLayer.getTileWidth()),(int)(y/tempLayer.getTileHeight())).getTile().getProperties().containsKey("win"))
            win();
            */
        return false;
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
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return true;
    }

    @Override
    public boolean panStop(float a, float b, int c, int d){
        return true;
    }

}

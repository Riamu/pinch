package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.liamhartery.pinch.entities.Entity;
import com.liamhartery.pinch.entities.Player;
import com.liamhartery.pinch.entities.enemies.BlobEnemy;

// TODO implement dynamic level loading  and place sprites at appropriate positions

public class GameScreen implements Screen,GestureListener,InputProcessor {
    private final PinchGame game;
    private int currentLayer = 5;
    private Texture playerImage;
    private Player player;

    private InputMultiplexer inputMultiplexer;
    private GestureDetector gd;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private final OrthographicCamera camera;

    private Vector3 touchPos = new Vector3();

    private float elapsedTime = 0;

    private float pinchDistance;

    private boolean longPressBool = false;
    private BlobEnemy blob;

    private float invulTimer = 0;
    // dispose any resource that needs disposing of
    @Override
    public void dispose(){
        tiledMap.dispose();
        playerImage.dispose();
        player.dispose();
        for(int i=0;i<Entity.getEntities().size();i++){
            Entity.getEntities().get(i).dispose();
        }

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
        tiledMap = new TmxMapLoader().load("levels/level1/level1.1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // create a new Player object for the oldPlayer using the playerImage
        player = new Player(playerImage,
                new TextureAtlas("entities/charles/charles.pack"),
                (TiledMapTileLayer)tiledMap.getLayers().get(currentLayer),
                this,
                new Vector2(0,0));

        // fucking make sure that the camera gets centred on the oldPlayer at the beginning
        // otherwise it fucks shit up
        // this is a shitty fucking work around but I'm done with it now
        camera.position.x = 400;
        camera.position.y = 400;
        player.setPosition(400,400);
        // reset the font size because our camera actually changed
        game.font.getData().setScale(0.5f,0.5f);

        // set the gesture detector and input processor to that gesture detector
        gd = new GestureDetector(this);
        gd.setLongPressSeconds(0.05f);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gd);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
        for(int i=0;i<1;i++){
            blob = new BlobEnemy(playerImage,new TextureAtlas(Gdx.files.internal("entities/enemies/blob.pack")),
                    (TiledMapTileLayer)tiledMap.getLayers().get(currentLayer+3),this, new Vector2(23*16,23*16)
            );
        }
    }

    @Override
    public void render(float delta){
        if(invulTimer>1){
            player.setInvulnerable(false);
            invulTimer=0;
        }
        if(player.getInvulnerable()){
            invulTimer+=delta;
        }
        // elapsed time for the animation and score tracking
        elapsedTime += delta;
        Gdx.gl.glClearColor(33/255f,30/255f,39/255f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Camera needs to be told to update
        camera.update();

        // Set the sprite batch to render using the camera's coordinates
        game.batch.setProjectionMatrix(camera.combined);

        // Render the map (again using the camera)
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();


        // Update all entities
        for(int i=0; i<Entity.getEntities().size(); i++){
            Entity.getEntities().get(i).update(delta);
            int damage = Entity.getEntities().get(i).playerDamage(player);
            player.takeDamage(damage);
            if(damage>0){
                player.setInvulnerable(true);
            }
        }
        player.updateHearts();

        // Sprite Batch
        game.batch.begin();
            // Draw all entities
            for(int i=0; i<Entity.getEntities().size();i++){
                Entity tempEntity = Entity.getEntities().get(i);
                if(tempEntity.getCollisionLayer()!=getCurrentLayer()) {
                    // do nothing
                }else {
                    game.batch.draw(tempEntity.getAnimation().getKeyFrame(elapsedTime, true),
                            tempEntity.getX() - tempEntity.getWidth() / 2,
                            tempEntity.getY() - tempEntity.getHeight() / 2);
                }
            }
            // we draw the player using it's midpoint instead of bottom left
            // TODO get a freakin' 16x16 spritesheet
            game.batch.draw(player.getAnimation().getKeyFrame(elapsedTime,true),
                player.getX()- player.getWidth()/2-5,
                player.getY()- player.getHeight()/2);

            // draw hearts on top corner of screen
            for(int i = 0; i< player.getHearts().size(); i++){
                game.batch.draw(player.getHearts().get(i),camera.position.x-150+(9*i),camera.position.y+75);
            }

            // FPS counter
        //game.font.draw(game.batch,"FPS: "+1f/delta,camera.position.x-144,camera.position.y+86);
        game.batch.end();

        // If the screen is touched with 1 finger we move the oldPlayer towards that point
        if(Gdx.input.isTouched()){
            // If more than one finger is on the screen do nothing (most likely a pinch or zoom)
            if((Gdx.input.isTouched(1))){
                // do nothing
            }else if(longPressBool){
                // we moveTowards the un-projected touchPos here
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                player.update(touchPos.x,touchPos.y, delta);
            }
        }else{
            player.setIdle();
        }

        /* moving the camera when the oldPlayer comes too close to the edge
         * Yo so when it's hitting one of the x bounds we translate based on oldPlayer speed with dir
         * same thing for the y axis
         * TODO make this a method?
         */
        // oldPlayer is on the right bound
        if(player.getPosition().x-camera.position.x>camera.viewportWidth-camera.viewportWidth/1.2){
            camera.translate((player.getSpeed()* player.getDirection().x)*delta,
                    0);
        // oldPlayer is on the left bound
        }
        else if(player.getPosition().x-camera.position.x<camera.viewportWidth/1.2-camera.viewportWidth){
            camera.translate((player.getSpeed()* player.getDirection().x)*delta,
                    0);
        }
        // oldPlayer is on the bottom bound
        if(player.getPosition().y-camera.position.y>camera.viewportHeight-camera.viewportHeight/1.2){
            camera.translate(0,
                    (player.getSpeed()* player.getDirection().y)*delta);
        // oldPlayer is on the top bound
        }
        else if(player.getPosition().y-camera.position.y<camera.viewportHeight/1.2-camera.viewportHeight){
            camera.translate(0,
                    (player.getSpeed()* player.getDirection().y)*delta);
        }

        // check if the oldPlayer is dead
        if(player.getHealth()<=0){
            lose();
        }
    }
    // called whenever a pinchStop is registered,
    // takes the distance fingers travelled for pinch/zoom gesture
    // TODO check if you're in a void tile and if you are, die
    public void changeFloor(float distance){
        tiledMap.getLayers().get(currentLayer).setVisible(false);
        if(distance<0){
            if(currentLayer-3>=2) {
                tiledMap.getLayers().get(currentLayer-1).setVisible(false);
                tiledMap.getLayers().get(currentLayer-2).setVisible(false);
                currentLayer -= 3;
                tiledMap.getLayers().get(currentLayer).setVisible(false);
                tiledMap.getLayers().get(currentLayer-1).setVisible(true);
                tiledMap.getLayers().get(currentLayer-2).setVisible(true);
                player.updateLayer((TiledMapTileLayer) tiledMap.getLayers().get(currentLayer));
                if(player.isNextToVoid()){
                    player.kill();
                    lose();
                }
            }
        }else{
            if(currentLayer+3<=tiledMap.getLayers().getCount()) {
                tiledMap.getLayers().get(currentLayer-1).setVisible(false);
                tiledMap.getLayers().get(currentLayer-2).setVisible(false);
                currentLayer += 3;
                tiledMap.getLayers().get(currentLayer).setVisible(false);
                tiledMap.getLayers().get(currentLayer-1).setVisible(true);
                tiledMap.getLayers().get(currentLayer-2).setVisible(true);
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

    // Keyboard input for changing floors
    @Override
    public boolean keyDown(int keycode) {
        if(Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)){
            changeFloor(-1);
        }else if(Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)){
            changeFloor(1);
        }else if(Gdx.input.isKeyPressed(Input.Keys.W)){
            win();
        }
        return false;
    }

    // resets the longPressBool
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        longPressBool = false;
        //Gdx.app.log("touchUP","");
        return false;
    }

    // sets the longPressBool
    @Override
    public boolean longPress(float x, float y) {
        longPressBool = true;
        return false;
    }

    // sets the distance for a pinch
    @Override
    public boolean zoom(float initialDistance, float distance) {
        pinchDistance = initialDistance-distance;
        return false;
    }
    // actually doesn't do anything
    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                         Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
    // calls the changeFloor method with the distance determined in the zoom method
    @Override
    public void pinchStop(){
        changeFloor(pinchDistance);
    }

    // when the user taps the game checks the spaces all around the player for a win
    // items soon
    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(player.isNextToWin())win();
        return false;
    }

    // this will be used to attack
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        //Gdx.app.log("Fling performed",""+velocityX+","+velocityY);
        return false;
    }

    public TiledMapTileLayer getCurrentLayer(){
        return player.getCollisionLayer();
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
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float a, float b, int c, int d){
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

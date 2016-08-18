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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.liamhartery.pinch.PinchGame;
import com.liamhartery.pinch.entities.Entity;
import com.liamhartery.pinch.entities.Player;
import com.liamhartery.pinch.entities.enemies.BlobEnemy;

public class GameScreen implements Screen,GestureListener,InputProcessor {
    private final PinchGame game;
    private int currentLayer = 5;
    //private Texture playerImage;
    private Player player;

    private InputMultiplexer inputMultiplexer;
    private GestureDetector gd;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private final OrthographicCamera camera;

    private Vector3 touchPos = new Vector3();

    private float elapsedTime = 0;

    private float pinchDistance;
    private float startingPlayerX;
    private float startingPlayerY;

    private boolean longPressBool = false;

    private float invulTimer = 0;
    private boolean firstFrame = true;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    // dispose any resource that needs disposing of
    @Override
    public void dispose(){
        tiledMap.dispose();
        //playerImage.dispose();
        player.dispose();
        for(int i=0;i<Entity.getEntities().size();i++){
            Entity.getEntities().get(i).dispose();
        }

    }
    // constructor is just like the create() method
    public GameScreen(final PinchGame pinch) {
        game = pinch;

        // load images
        //playerImage = new Texture(Gdx.files.internal("entities/player2.jpg"));

        // create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false,320,180);

        //Load the map
        tiledMap = new TmxMapLoader().load("levels/level1/level1.1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        setUpTiledMap();
        // create a new Player object for the Player using the playerImage
        player = new Player(
                new TextureAtlas("entities/charles/charles.pack"),
                (TiledMapTileLayer)tiledMap.getLayers().get(currentLayer),
                this,
                new Vector2(startingPlayerX,startingPlayerY));

        // at startup make sure the camera is centred on the player
        // otherwise some badshit(tm) can happen
        camera.position.x = startingPlayerX;
        camera.position.y = startingPlayerY;
        player.setPosition(startingPlayerX,startingPlayerY);
        // reset the font size because our camera actually changed
        game.font.getData().setScale(0.5f,0.5f);

        // set the gesture detector and input processor to that gesture detector
        gd = new GestureDetector(this);
        gd.setLongPressSeconds(0.05f);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gd);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta){
        if(firstFrame){
            delta = 0;
            firstFrame = false;
        }
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
                            tempEntity.getX(),
                            tempEntity.getY());
                }
            }
            // we draw the player using it's midpoint instead of bottom left
            // TODO get a new character spritesheet
            game.batch.draw(player.getAnimation().getKeyFrame(elapsedTime,true),
                player.getX(),
                player.getY()
            );
            // draw hearts on top corner of screen
            for(int i = 0; i< player.getHearts().size(); i++){
                game.batch.draw(player.getHearts().get(i),camera.position.x-150+(9*i),camera.position.y+75);
            }
        // FPS counter
        //game.font.getData().setScale(0.25f);
        //game.font.draw(game.batch,"FPS: "+1f/delta,camera.position.x+100,camera.position.y+86);
        game.batch.end();


        shapeRenderer.setProjectionMatrix(camera.combined);

        // Our bounding boxes are all out of whack
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0,1,0,1);
            shapeRenderer.rect(
                    player.getBoundingRectangle().getX(),
                    player.getBoundingRectangle().getY(),
                    player.getBoundingRectangle().getWidth(),
                    player.getBoundingRectangle().getHeight()
            );
            for(int i=0;i<Entity.getEntities().size();i++){
                shapeRenderer.rect(
                        Entity.getEntities().get(i).getBoundingRectangle().getX(),
                        Entity.getEntities().get(i).getBoundingRectangle().getY(),
                        Entity.getEntities().get(i).getBoundingRectangle().getWidth(),
                        Entity.getEntities().get(i).getBoundingRectangle().getHeight()
                );
            }
        shapeRenderer.end();
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
         * Yo so when it's hitting one of the x bounds we translate based on Player speed with dir
         * same thing for the y axis
         * TODO make this a method
         */
        // Player is on the right bound
        if(player.getPosition().x-camera.position.x>camera.viewportWidth-camera.viewportWidth/1.2){
            camera.translate((player.getSpeed()* player.getDirection().x)*delta,
                    0);
        // Player is on the left bound
        }
        else if(player.getPosition().x-camera.position.x<camera.viewportWidth/1.2-camera.viewportWidth){
            camera.translate((player.getSpeed()* player.getDirection().x)*delta,
                    0);
        }
        // Player is on the top bound
        if(player.getPosition().y-camera.position.y>camera.viewportHeight-camera.viewportHeight/1.05){
            camera.translate(0,
                    (player.getSpeed()* player.getDirection().y)*delta);

        }
        // Player is on the bottom bound
        else if(player.getPosition().y-camera.position.y<camera.viewportHeight/1.2-camera.viewportHeight){
            camera.translate(0,
                    (player.getSpeed()* player.getDirection().y)*delta);
        }

        // check if the Player is dead
        if(player.getHealth()<=0){
            lose();
        }
    }
    // called whenever a pinchStop is registered,
    // takes the distance fingers travelled for pinch/zoom gesture
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
                if(player.isNextTo("void")){
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
                if(player.isNextTo("void")){
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
            player.attack();
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
        if(player.isNextTo("win"))win();
        return false;
    }

    // this will be used to attack
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        //Gdx.app.log("Fling performed",""+velocityX+","+velocityY);

        player.attack(velocityX,velocityY);
        return false;
    }

    public TiledMapTileLayer getCurrentLayer(){
        return player.getCollisionLayer();
    }

    // put anything that's in the tiledMap that needs a sprite in here
    public void setUpTiledMap(){
        for(int layerNum=0;layerNum<tiledMap.getLayers().getCount();layerNum++) {
            TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(layerNum);
            for(int x=0;x<layer.getWidth();x++){
                for(int y=0;y<layer.getHeight();y++){
                    TiledMapTileLayer.Cell cell = layer.getCell(x,y);
                    if(cell==null)break;
                    MapProperties properties = cell.getTile().getProperties();
                    // "start" is the player starting position property
                    if(properties.containsKey("start")){
                        startingPlayerX = x*layer.getTileWidth();
                        startingPlayerY = y*layer.getTileHeight();
                        currentLayer = layerNum;
                    }
                    // "blob" is a blob enemy
                    else if(properties.containsKey("blob")){
                        new BlobEnemy(new TextureAtlas(Gdx.files.internal("entities/enemies/blob.pack")),
                                (TiledMapTileLayer)tiledMap.getLayers().get(layerNum),this,
                                new Vector2(x*layer.getTileWidth(),y*layer.getTileHeight()));
                    }
                }
            }
        }
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

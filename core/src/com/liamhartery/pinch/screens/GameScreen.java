package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.liamhartery.pinch.PinchGame;
import com.liamhartery.pinch.entities.enemies.ArmouredBlob;
import com.liamhartery.pinch.entities.enemies.FastBlob;
import com.liamhartery.pinch.entities.enemies.FollowBlob;
import com.liamhartery.pinch.entities.interactives.Chest;
import com.liamhartery.pinch.entities.Entity;
import com.liamhartery.pinch.entities.Player;
import com.liamhartery.pinch.entities.Projectile;
import com.liamhartery.pinch.entities.enemies.BlobEnemy;
import com.liamhartery.pinch.entities.interactives.Door;
import com.liamhartery.pinch.entities.interactives.Key;

import java.util.ArrayList;

public class GameScreen implements Screen,GestureListener,InputProcessor {
    private final PinchGame game;

    // Tiled Map Variables
    private int currentLayer = 0;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private int currentLevelNum;
    private int currentLevelDir;

    // Camera Stuff
    private final OrthographicCamera camera;
    private int screenWidth = 400;
    private int screenHeight = 225;

    // Gesture & Input Stuff
    private InputMultiplexer inputMultiplexer;
    private GestureDetector gd;
    private Vector3 touchPos = new Vector3();
    private float pinchDistance;
    private boolean longPressBool = false;
    private boolean pinchJustStopped = false;

    // Projectile and Player stuff
    private Player player;
    private ArrayList<Projectile> projectiles;
    private float startingPlayerX;
    private float startingPlayerY;
    private float invulTimer = 0;
    private boolean firstFrame = true;
    private boolean attackIsOffCoolDown;
    private float coolDownCounter = 0;

    private float elapsedTime = 0;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    // Cheats
    private boolean devMode = false;

    // player position broadcast
    private Vector2 playerPos;

    // dispose any resource that needs disposing of
    @Override
    public void dispose(){
        tiledMap.dispose();
        if(devMode){
            shapeRenderer.dispose();
        }
        for(int i=0;i<Entity.getEntities().size();i++){
            Entity.getEntities().get(i).dispose();
        }
        Entity.getEntities().clear();
    }

    // constructor is just like the create() method
    // the above phrase makes less and less sense since it's been forever since I used create()
    // for anything
    public GameScreen(final PinchGame pinch, int levelDirectory, int levelNum) {
        game = pinch;

        // projectile things
        projectiles = new ArrayList<Projectile>();
        attackIsOffCoolDown = true;

        // create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false,screenWidth,screenHeight);

        // just in case followblobs update gets called early
        playerPos = new Vector2(0,0);

        //Load the map
        currentLevelNum = levelNum;
        currentLevelDir = levelDirectory;
        tiledMap = new TmxMapLoader().load("levels/"+levelDirectory+"/"+levelNum+".tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        setUpTiledMap();

        // create a new Player object for the Player using the playerImage
        player = new Player(
                new TextureAtlas("entities/charles/charles.pack"),
                (TiledMapTileLayer)tiledMap.getLayers().get(currentLayer),
                this,
                new Vector2(startingPlayerX,startingPlayerY));
        playerPos = new Vector2(startingPlayerX,startingPlayerY);
        // at startup make sure the camera is centred on the player
        // otherwise some badShit(tm) can happen
        camera.position.x = startingPlayerX;
        camera.position.y = startingPlayerY;
        player.setPosition(startingPlayerX,startingPlayerY);

        // reset the font size because our camera actually changed
        game.font.getData().setScale(0.5f,0.5f);

        // set the gesture detector and input processor to that gesture detector
        gd = new GestureDetector(this);
        gd.setLongPressSeconds(0.1f);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gd);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
    public GameScreen(final PinchGame pinch, int levelDirectory, int levelNum, Player player) {
        game = pinch;

        // projectile things
        projectiles = new ArrayList<Projectile>();
        attackIsOffCoolDown = true;


        // create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false,screenWidth,screenHeight);

        // just in case followblobs update gets called early
        playerPos = new Vector2(0,0);

        //Load the map
        currentLevelNum = levelNum;
        currentLevelDir = levelDirectory;
        tiledMap = new TmxMapLoader().load("levels/"+levelDirectory+"/"+levelNum+".tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        setUpTiledMap();

        // make sure everything with our player is peachy, and then transfer ownership of him
        player.setMaxHealth(player.getMaxHealth()+1);
        player.setHealth(player.getMaxHealth());
        player.updateHearts();
        player.setGameScreen(this);
        this.player = player;
        playerPos = new Vector2(player.getPosition());

        // at startup make sure the camera is centred on the player
        // otherwise some badShit(tm) can happen
        camera.position.x = startingPlayerX;
        camera.position.y = startingPlayerY;
        player.setPosition(startingPlayerX,startingPlayerY);

        // reset the font size because our camera actually changed
        game.font.getData().setScale(0.5f,0.5f);

        // set the gesture detector and input processor to that gesture detector
        gd = new GestureDetector(this);
        gd.setLongPressSeconds(0.1f);
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gd);
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);

        // this fixes the bug where when you start the next level the collision map is wrong
        // no idea why that happens yet but TODO remove these when bug is fixed
        changeFloor(-1);
        changeFloor(1);
    }

    @Override
    public void render(float delta) {

        /*
         * LOGIC SECTION RIGHT HERE BOYS
         */
        game.timer+=delta;
        // elapsed time for the animation
        elapsedTime += delta;
        // if it's the first frame we set delta to 0 since we might get some buggy behaviour if
        // load times were too long
        if (firstFrame) {
            delta = 0;
            firstFrame = false;
        }
        if(delta>1/15f){
            delta=1/60f;
        }
        // check if the invulnerability timer is over our specified amount of (1)
        // TODO make increased invulnerability times a powerup
        if (invulTimer > 1) {
            player.setInvulnerable(false);
            invulTimer = 0;
        }
        // if the player is invulnerable still we increase the timer for how long he has been
        if (player.getInvulnerable()) {
            invulTimer += delta;
        }

        // figures out if the attack should still be off CD
        if (coolDownCounter > 0) {
            coolDownCounter -= delta;
        } else if (coolDownCounter <= 0) {
            attackIsOffCoolDown = true;
            coolDownCounter = 0;
        }


        // Update all entities
        for (int i = 0; i < Entity.getEntities().size(); i++) {
            Entity.getEntities().get(i).update(delta);
            if(player.getBoundingRectangle().overlaps(Entity.getEntities().get(i).getBoundingRectangle())
                    &&player.getCollisionLayer()==Entity.getEntities().get(i).getCollisionLayer()){
                int damage = Entity.getEntities().get(i).playerDamage();
                player.takeDamage(Entity.getEntities().get(i).playerDamage());
                if(damage>0){
                    player.setInvulnerable(true);
                }
            }
        }
        // Update all projectiles
        for (int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).update(delta);
        }
        player.updateHearts();

        // If the screen is touched with 1 finger we move the oldPlayer towards that point
        if (Gdx.input.isTouched()) {
            // If more than one finger is on the screen do nothing (most likely a pinch or zoom)
            if ((Gdx.input.isTouched(1))) {
                // do nothing
            } else if (longPressBool) {
                // we moveTowards the un-projected touchPos here
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                player.update(touchPos.x, touchPos.y, delta);
                playerPos = player.getPosition();
            }
        } else {
            player.setIdle();
        }
        /* moving the camera when the oldPlayer comes too close to the edge
         * Yo so when it's hitting one of the x bounds we translate based on Player speed with dir
         * same thing for the y axis
         * TODO make this a method
         */

        // Player is on the right bound
        if (player.getPosition().x - camera.position.x > camera.viewportWidth - camera.viewportWidth / 1.2) {
            camera.translate((player.getSpeed() * player.getDirection().x) * delta,
                    0);
            // Player is on the left bound
        } else if (player.getPosition().x - camera.position.x < camera.viewportWidth / 1.2 - camera.viewportWidth) {
            camera.translate((player.getSpeed() * player.getDirection().x) * delta,
                    0);
        }
        // Player is on the top bound
        if (player.getPosition().y - camera.position.y > camera.viewportHeight - camera.viewportHeight / 1.05) {
            camera.translate(0,
                    (player.getSpeed() * player.getDirection().y) * delta);

        }
        // Player is on the bottom bound
        else if (player.getPosition().y - camera.position.y < camera.viewportHeight / 1.2 - camera.viewportHeight) {
            camera.translate(0,
                    (player.getSpeed() * player.getDirection().y) * delta);
        }

        // check if the Player is dead
        if (player.getHealth() <= 0) {
            lose();
        }

        /*
         * DRAW SECTION RIGHT HERE BOYS
         */
        Gdx.gl.glClearColor(33 / 255f, 30 / 255f, 39 / 255f, 0);
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

        // Draw all entities
        for (int i = 0; i < Entity.getEntities().size(); i++) {
            Entity tempEntity = Entity.getEntities().get(i);
            if (tempEntity.getCollisionLayer() != getCurrentLayer()) {
                // do nothing
            } else {
                game.batch.draw(tempEntity.getAnimation().getKeyFrame(elapsedTime, true),
                        tempEntity.getX(),
                        tempEntity.getY());
            }
        }

        // here we draw all our projectiles
        //Gdx.app.log("projectiles.size",""+projectiles.size());
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile tempProjectile = projectiles.get(i);
            if (tempProjectile.getCollisionLayer() != getCurrentLayer()) {
                // do nothing
            } else {
                game.batch.draw(tempProjectile.getAnimation().getKeyFrame(elapsedTime, true),
                        tempProjectile.getX(),
                        tempProjectile.getY());
            }
        }

        // TODO get a new character spritesheet
        // draw the player
        game.batch.draw(player.getAnimation().getKeyFrame(elapsedTime, true),
                player.getX(),
                player.getY()
        );

        // draw hearts on top corner of screen
        for (int i = 0; i < player.getHearts().size(); i++) {
            game.batch.draw(player.getHearts().get(i), camera.position.x - screenWidth / 2 + 9 + (9 * i),
                    camera.position.y + screenHeight / 2 - 20);
        }
        //draw keys below hearts
        for(int i = 0; i<player.getKeys().size();i++){
            game.batch.draw(player.getKeys().get(i),camera.position.x-screenWidth/2+9+(9*i),
            camera.position.y+screenHeight/2-40);
        }
        if(devMode) {
            // FPS counter
            game.font.getData().setScale(0.25f);
            game.font.draw(game.batch, "FPS: " + 1f / delta, camera.position.x + 120, camera.position.y + 86);
            game.font.draw(game.batch,"Player Stats: ",camera.position.x-190,camera.position.y+86);
            game.font.draw(game.batch,"CoolDown: "+player.getCoolDown(),camera.position.x-190,camera.position.y+76);
            game.font.draw(game.batch,"ProjeTTK: "+player.getProjectileTTK(),camera.position.x-190,camera.position.y+66);
            game.font.draw(game.batch,"Speed: "+player.getSpeed(),camera.position.x-190,camera.position.y+56);
            game.font.draw(game.batch,"PrDMG: "+player.getProjectileDamage(),camera.position.x-190,camera.position.y+46);
        }
        game.batch.end();

        // this renders bounding boxes on player and enemies
        if (devMode) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 1, 0, 1);
            shapeRenderer.rect(
                    player.getBoundingRectangle().getX(),
                    player.getBoundingRectangle().getY(),
                    player.getBoundingRectangle().getWidth(),
                    player.getBoundingRectangle().getHeight()
            );
            for (int i = 0; i < Entity.getEntities().size(); i++) {
                shapeRenderer.rect(
                        Entity.getEntities().get(i).getBoundingRectangle().getX(),
                        Entity.getEntities().get(i).getBoundingRectangle().getY(),
                        Entity.getEntities().get(i).getBoundingRectangle().getWidth(),
                        Entity.getEntities().get(i).getBoundingRectangle().getHeight()
                );
            }
            for(int i=0; i<projectiles.size();i++){
                shapeRenderer.rect(
                        projectiles.get(i).getBoundingRectangle().getX(),
                        projectiles.get(i).getBoundingRectangle().getY(),
                        projectiles.get(i).getBoundingRectangle().getWidth(),
                        projectiles.get(i).getBoundingRectangle().getHeight()
                );
            }
            shapeRenderer.end();
        }
    }

    // Win and Loss conditions
    public void win(){
        // TODO make more levels so we can change this number to 5
        if(currentLevelNum<4){
            dispose();
            game.setScreen(new GameScreen(game,currentLevelDir,currentLevelNum+1,player));
        }else {
            game.setScreen(new WinScreen(game, game.timer));
            dispose();
            player.dispose();
        }
    }
    public void lose(){
        game.setScreen(new LoseScreen(game));
        dispose();
        player.dispose();
    }

    // Some Projectile Methods
    public void addProjectile(Projectile newProjectile){
        projectiles.add(newProjectile);
    }
    public void removeProjectile(Projectile oldProjectile){
        projectiles.remove(oldProjectile);
    }

    /*
     * THINGS TO DO WITH TILED MAPS
     */
    public TiledMapTileLayer getCurrentLayer(){
        return player.getCollisionLayer();
    }
    public TiledMap getTiledMap(){
        return tiledMap;
    }
    public int getCurrentLayerInt(){
        return currentLayer;
    }
    // called whenever a pinchStop is registered,
    // takes the distance fingers travelled for pinch/zoom gesture
    // if the distance is negative they go up one level, if positive they go down
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
                if(player.isOnTile("void")||player.isOnTile("blocked")){
                    player.kill();
                    resetLevel();
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
                if(player.isOnTile("void")||player.isOnTile("blocked")){
                    player.kill();
                    resetLevel();
                }
            }
        }
    }

    // put anything that's in the tiledMap that needs a sprite in here
    public void setUpTiledMap(){
        // three nested for loops to iterate through every layer, and every x,y coordinate on each
        for(int layerNum=0;layerNum<tiledMap.getLayers().getCount();layerNum++) {
            TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(layerNum);
            for(int x=0;x<layer.getWidth();x++){
                for(int y=0;y<layer.getHeight();y++){
                    TiledMapTileLayer.Cell cell = layer.getCell(x,y);
                    // only if the cell isn't null can we check its properties
                    if(cell!=null) {
                        MapProperties properties = cell.getTile().getProperties();
                        // "start" is the player starting position property
                        if (properties.containsKey("start")) {
                            startingPlayerX = x * layer.getTileWidth();
                            startingPlayerY = y * layer.getTileHeight();
                            currentLayer = layerNum;
                        }else if(properties.containsKey("chest")){
                            new Chest(new TextureAtlas(Gdx.files.internal("entities/chest/chest.pack")),
                                    (TiledMapTileLayer)tiledMap.getLayers().get(layerNum),this,
                                    new Vector2(x*layer.getTileWidth(),y*layer.getTileHeight())
                                    );
                        }
                        // "blob" is a blob enemy
                        else if (properties.containsKey("blob")) {
                            new BlobEnemy(new TextureAtlas(Gdx.files.internal("entities/enemies/blob/blob.pack")),
                                    (TiledMapTileLayer) tiledMap.getLayers().get(layerNum), this,
                                    new Vector2(x * layer.getTileWidth(), y * layer.getTileHeight()));
                        }else if(properties.containsKey("key")){
                            new Key(
                                    new TextureAtlas(Gdx.files.internal("entities/key/key.pack")),
                                    (TiledMapTileLayer)tiledMap.getLayers().get(layerNum),
                                    this,
                                    new Vector2(x*layer.getTileWidth(),y*layer.getTileHeight()),
                                    player
                            );
                        }else if(properties.containsKey("door")){
                            new Door(
                                    new TextureAtlas(Gdx.files.internal("entities/door/door.pack")),
                                    (TiledMapTileLayer)tiledMap.getLayers().get(layerNum),
                                    this,
                                    new Vector2(x*layer.getTileWidth(),y*layer.getTileHeight()),
                                    tiledMap
                            );
                        }else if(properties.containsKey("followblob")){
                            new FollowBlob(
                              new TextureAtlas(Gdx.files.internal("entities/enemies/followblob/blob.pack")),
                                    (TiledMapTileLayer)tiledMap.getLayers().get(layerNum),this,
                                    new Vector2(x*layer.getTileWidth(),y*layer.getTileHeight())
                            );
                        }else if(properties.containsKey("fastblob")){
                            new FastBlob(
                                    new TextureAtlas(Gdx.files.internal("entities/enemies/fastblob/blob.pack")),
                                    (TiledMapTileLayer)tiledMap.getLayers().get(layerNum),this,
                                    new Vector2(x*layer.getTileWidth(),y*layer.getTileHeight())
                            );
                        }else if(properties.containsKey("armouredblob")){
                            new ArmouredBlob(
                                    new TextureAtlas(Gdx.files.internal("entities/enemies/armouredblob/blob.pack")),
                                    (TiledMapTileLayer)tiledMap.getLayers().get(layerNum),this,
                                    new Vector2(x*layer.getTileWidth(),y*layer.getTileHeight())
                            );
                        }
                    }
                    // key
                    // lockedDoor
                    // other enemies
                }
            }
        }
    }

    // TODO balance dying by falling and make it better
    public void resetLevel(){
        game.setScreen(new GameScreen(game,currentLevelDir,currentLevelNum));
    }
    /*
     * INPUT LISTENERS
     */

    // when a touchDown event is registered the pinchJustStopped boolean is false, this prevents
    // flings from being registered after pinch and zoom gestures
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if(pinchJustStopped){
            pinchJustStopped=false;
        }
        return false;
    }
    // this will be used to attack
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        // if a pinch just stopped then we don't want to attack
        if(pinchJustStopped){
            return false;
        }
        // also if the thing is long pressed we don't want to attack
        if(longPressBool){
            longPressBool=false;
            return false;
        }
        // ALSO ALSO if the velocity of our fling is 0 we don't want to attack
        if(Math.abs(velocityX)==0&&Math.abs(velocityY)==0){
            return false;
        }
        // FINALLY if the attack is off cool down we can attack
        if(attackIsOffCoolDown){
            attackIsOffCoolDown = false;
            coolDownCounter = player.getCoolDown();
            player.attack(velocityX,velocityY);

        }
        return false;
    }
    // when the user taps the game checks the spaces all around the player for a win
    // items soon
    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(player.isNextTo("win"))win();
        // we check if the player is next to any of the following
        if(player.isNextTo("chest")){
            // they're all entities so we need to go through the entity arraylist to find them
            for(int i=0;i<Entity.getEntities().size();i++){
                // if we find a chest do the following
                if(Entity.getEntities().get(i) instanceof Chest){
                    // cast the entity to a chest
                    Chest tempChest = (Chest)Entity.getEntities().get(i);
                    // are the player and chest colliding? if they are and also on the same level
                    // open the chest and give the player an item from it
                    if(tempChest.getBoundingRectangle().overlaps(player.getBoundingRectangle())&&
                            player.getCollisionLayer().equals(tempChest.getCollisionLayer())) {
                        if(tempChest.openChest()>0)player.receiveRandomItem();
                        // TODO comment out above line and replace with below line once powerups are in
                        // player.giveItem(tempChest.openChest());
                        // or maybe
                        // player.givePowerUp(tempChest.openChest());
                        // can the giveItem method take a String sometimes instead to give a specific item?
                        // of course it can silly. This is Java(tm)
                    }

                }
            }
        }
        if(player.isNextTo("key")){
            for(int i=0;i<Entity.getEntities().size();i++){
                if(Entity.getEntities().get(i)instanceof Key){
                    Key tempKey = (Key)Entity.getEntities().get(i);
                    if(tempKey.getBoundingRectangle().overlaps(player.getBoundingRectangle())
                            &&player.getCollisionLayer().equals(tempKey.getCollisionLayer())){
                        tempKey.playerGotMe();
                        player.giveKey();
                    }
                }
            }
        }
        if(player.isNextTo("door")){
            for(int i=0;i<Entity.getEntities().size();i++){
                if(Entity.getEntities().get(i) instanceof Door){
                    Door tempDoor = (Door)Entity.getEntities().get(i);
                    if(tempDoor.getBoundingRectangle().overlaps(player.getBoundingRectangle())
                    &&player.getCollisionLayer().equals(tempDoor.getCollisionLayer())
                            &&tempDoor.isLocked()) {
                        if(player.useKey()){
                            tempDoor.unlock();
                        }else{
                            tempDoor.playLockedSound();
                        }
                    }
                }
            }
        }
        return false;
    }

    public Vector2 getPlayerPos(){
        return playerPos;
    }
    // Keyboard input for changing floors TODO take this out on android release
    @Override
    public boolean keyDown(int keycode) {
        if(Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)){
            changeFloor(-1);
        }else if(Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)){
            changeFloor(1);
        }else if(Gdx.input.isKeyPressed(Input.Keys.W)){
            win();
        }else if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            player.setMaxHealth(player.getMaxHealth()+1);
            player.setHealth(player.getMaxHealth());
            player.updateHearts();
        }
        return false;
    }

    // does nothing actually
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        longPressBool = false;
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
        return true;
    }

    // calls the changeFloor method with the distance determined in the zoom method
    @Override
    public void pinchStop(){
        changeFloor(pinchDistance);
        pinchJustStopped = true;
    }

    // None of the following are used
    // funny that the gesture listener "pinch" doesn't actually get used, it just doesn't report
    // the easiest values for this kind of thing
    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                         Vector2 pointer1, Vector2 pointer2) {
        return true;
    }

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

package com.liamhartery.pinch.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.screens.GameScreen;
import java.util.ArrayList;

/*
 * Entity is the superclass of every entity in the game
 *
 * default max health for entities is 3
 *
 * all entities get added to the entities ArrayList
 *
 * Entity provides position and direction vectors
 * * an Animation
 * * a TextureAtlas
 * * a final GameScreen
 * * health
 * * attack damage
 */

public abstract class Entity extends Sprite {
    // this holds a static public list of every entity for rendering
    private static ArrayList<Entity> entities = new ArrayList<Entity>(1);

    // Movement Variables
    private Vector2 position;
    private Vector2 oldPosition;
    private Vector2 dir;

    // Collision Variables
    private TiledMapTileLayer collisionLayer;
    private float tileWidth,tileHeight;
    private boolean collisionX,collisionY;
    private TiledMapTile tempTile;

    // animations
    private TextureAtlas textureAtlas;
    private Animation animation; // some things will not have an animation

    // properties
    private int maxHealth;
    private int health;
    private int attackDamage;

    // GameScreen
    private GameScreen gameScreen;

    // this is the only constructor that should be used in most cases
    public Entity(TextureAtlas atlas,TiledMapTileLayer layer, GameScreen screen, Vector2 pos){
        // Calls the sprite constructor with an atlas region so hit boxes are better
        super(atlas.getRegions().get(0));
        dir = new Vector2(0,0);
        oldPosition = new Vector2(0,0);
        collisionLayer = layer;
        gameScreen = screen;
        tileWidth = collisionLayer.getTileWidth();
        tileHeight = collisionLayer.getTileHeight();

        // health defaults
        maxHealth = 3;
        health = maxHealth;
        // damage defaults
        attackDamage = 1;

        position = pos;
        textureAtlas = atlas;
    }

    // update will almost always get an override
    public void update(float delta){
    }

    // by default entities cannot damage the player
    // this needs to be defined in the derived class
    public int playerDamage(Player player){
        return 0;
    }

    // attack will almost always get an override
    public void attack(){
    }

    // entities can take damage and die by default. If this is not wanted behaviour please override
    // (chests, keys, doors)
    public void takeDamage(int dmg){
        health-=dmg;
        if(health<=0){
            kill();
        }
    }

    // removes the entity from entities and gets rid of the textureAtlas
    public void dispose(){
        textureAtlas.dispose();
        entities.remove(this);

    }

    // add int health to current health
    public void addHealth(int health){
        this.health += health;
        if(health>maxHealth){
            this.health = maxHealth;
        }
    }

    // kill is literally a wrapper for dispose();
    public void kill(){
        dispose();
    }

    // set max possible health
    public void setMaxHealth(int newMaxHealth){
        maxHealth = newMaxHealth;
    }

    // static method that returns the entities arraylist for manipulation
    public static ArrayList<Entity> getEntities(){
        return entities;
    }

    // wall x collision detection
    public boolean collisionDetectionX(){
        // towards left
        if (dir.x < 0f) {
            // we basically ask it for the tile at a certain location and return true if blocked
            tempTile = collisionLayer.getCell(
                    (int) ((getX())/tileWidth),
                    (int) (((getY()+getHeight()/2)/tileHeight))).getTile();
            return tempTile.getProperties().containsKey("blocked");
        }
        // towards right
        else if (dir.x > 0f) {
            tempTile = collisionLayer.getCell(
                    (int) ((getX()+getWidth()) / tileWidth),
                    (int) (((getY()+getHeight()/2) / tileHeight))).getTile();
            return tempTile.getProperties().containsKey("blocked");
        }
        return false;
    }
    // wall y collision detection
    public boolean collisionDetectionY(){
        // if moving downward (negative y)
        if (dir.y < 0f) {
            tempTile = collisionLayer.getCell(
                    (int) ((getX()+getWidth()/2) / tileWidth),
                    (int) ((getY()) / tileHeight)
            ).getTile();
            return tempTile.getProperties().containsKey("blocked");
        }
        // if moving upward (positive y)
        else if (dir.y > 0f) {
            tempTile = collisionLayer.getCell(
                    (int) ((getX()+getWidth()/2) / tileWidth),
                    (int) ((getY() + getHeight()) / tileHeight)
            ).getTile();
            return tempTile.getProperties().containsKey("blocked");
        }
        return false;
    }

    // direction getter and setter
    public Vector2 getDirection(){
        return dir;
    }
    public void setDirection(Vector2 newDirection){
        dir = newDirection;
    }
    public void setDirection(float x, float y){
        dir.x = x;
        dir.y = y;
    }

    // position getter and setter
    public Vector2 getPosition(){
        return position;
    }
    public void setPosition(float x, float y){
        position.x = x;
        position.y = y;
        setX(x);
        setY(y);
    }
    public void setPosition(Vector2 newPosition){
        position = newPosition;
        setX(position.x);
        setY(position.y);
    }

    // old position getter and setter
    public void setOldPosition(float x, float y){
        oldPosition.x = x;
        oldPosition.y = y;
    }
    public void setOldPosition(Vector2 oldPosition){
        this.oldPosition = oldPosition;
    }
    public Vector2 getOldPosition(){
        return oldPosition;
    }

    // TextureAtlas getter and setter
    public void setTextureAtlas(TextureAtlas atlas){
        textureAtlas = atlas;
    }
    public TextureAtlas getTextureAtlas(){
        return textureAtlas;
    }

    // health getter setter
    public int getHealth(){
        return health;
    }
    public void setHealth(int newHealth){
        if(newHealth>maxHealth){
            health = maxHealth;
        }
        health = newHealth;
    }
    public int getMaxHealth(){
        return maxHealth;
    }

    // tiled map getter and setter stuff
    public TiledMapTileLayer getCollisionLayer(){
        return collisionLayer;
    }
    public float getTileWidth(){
        return tileWidth;
    }
    public float getTileHeight(){
        return tileHeight;
    }
    public void updateLayer(TiledMapTileLayer newLayer){
        collisionLayer = newLayer;
    }

    // Animation getter setter
    public Animation getAnimation(){
        return animation;
    }
    public void setAnimation(Animation newAnimation){
        animation = newAnimation;
    }

    // game getter setter
    public GameScreen getGame(){
        return gameScreen;
    }
    public void setGameScreen(GameScreen newGameScreen){
        gameScreen = newGameScreen;
    }

    // is next to returns true if the entity is next to a tile with the property string
    public boolean isNextTo(String string){
        TiledMapTile tempTile;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                tempTile = getCollisionLayer().getCell(
                        (int)(getX()/getTileWidth()-1+i),
                        (int)(getY()/getTileHeight()+1+j))
                        .getTile();
                if(tempTile.getProperties().containsKey(string))
                    return true;
            }
        }
        return false;
    }

    // returns true if the tile that the entity is on has the property string
    public boolean isOnTile(String string){
        TiledMapTile tempTile;
        tempTile = getCollisionLayer().getCell(
                (int)((getX()+getWidth()/2)/getTileWidth()),
                (int)((getY()+getHeight()/2)/getTileHeight()))
                .getTile();
                if(tempTile.getProperties().containsKey(string))
                    return true;
        return false;
    }
}

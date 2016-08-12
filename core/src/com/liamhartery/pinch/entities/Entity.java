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
    private final GameScreen gameScreen;


    /*
     * Constructor takes many things and this is probably bad
     */
    public Entity(Texture texture, TextureAtlas atlas,
                  TiledMapTileLayer layer, GameScreen screen, Vector2 pos){
        // textures are mostly useless here I think actually, probably don't even need
        // to extend Sprite at this point
        super(texture);
        dir = new Vector2(0,0);
        oldPosition = new Vector2(0,0);
        collisionLayer = layer;
        gameScreen = screen;
        tileWidth = collisionLayer.getTileWidth();
        tileHeight = collisionLayer.getTileHeight();
        maxHealth = 3;
        health = maxHealth;
        attackDamage = 1;
        position = pos;
        textureAtlas = atlas;
    }

    public void update(float delta){
    }

    public void attack(){
    }

    public void takeDamage(){
    }

    public void setHealth(){
    }

    public void dispose(){
        textureAtlas.dispose();
        entities.clear();

    }
    public void addHealth(int health){
        this.health += health;
        if(health>maxHealth){
            this.health = maxHealth;
        }
    }
    public void kill(){
        health = 0;
    }

    public void setMaxHealth(int newMaxHealth){
        maxHealth = newMaxHealth;
    }

    public static ArrayList<Entity> getEntities(){
        return entities;
    }

    public boolean collisionDetectionX(){
        if (dir.x < 0f) {
            tempTile = collisionLayer.getCell(
                    (int) ((getX() - getWidth() / 2) / tileWidth),
                    (int) ((getY()) / tileHeight)).getTile();
            return tempTile.getProperties().containsKey("blocked");
        }
        // if we're instead moving towards the right (positive x)
        else if (dir.x > 0f) {
            tempTile = collisionLayer.getCell(
                    (int) ((getX() + getWidth() / 2) / tileWidth),
                    (int) (((getY()) / tileHeight))).getTile();
            return tempTile.getProperties().containsKey("blocked");
        }
        return false;
    }
    public boolean collisionDetectionY(){
        // if moving downward (negative y)
        if (dir.y < 0f) {
            tempTile = collisionLayer.getCell(
                    (int) ((getX()) / tileWidth),
                    (int) ((getY() - getHeight() / 2) / tileHeight)
            ).getTile();
            return tempTile.getProperties().containsKey("blocked");
        }
        // if moving upward (positive y)
        else if (dir.y > 0f) {
            tempTile = collisionLayer.getCell(
                    (int) ((getX()) / tileWidth),
                    (int) ((getY() + getHeight() / 2) / tileHeight)
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
    public int getMaxHealth(){
        return maxHealth;
    }
    public Animation getAnimation(){
        return animation;
    }
    public void setAnimation(Animation newAnimation){
        animation = newAnimation;
    }
}

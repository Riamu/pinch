package com.liamhartery.pinch.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.screens.GameScreen;

import java.util.ArrayList;
// TODO Better player Sprite
// TODO give the player an attack
// TODO inventory / Powerups
// TODO Key and locked door mechanic

public class Player extends Entity{
    // Textures
    private Texture heartTexture = new Texture(Gdx.files.internal("icons/fullheart.png"));
    private Texture emptyHeartTexture = new Texture(Gdx.files.internal("icons/emptyheart.png"));
    private ArrayList<Texture> hearts;

    // Movement variables
    private float speed;
    private float distance;

    // animations
    private Animation animation;
    private Animation downAnimation;
    private Animation upAnimation;
    private Animation leftAnimation;
    private Animation rightAnimation;
    private Animation idleAnimation;
    private Animation teleportUpAnimation;
    private Animation teleportDownAnimation;

    // booleans
    private boolean invulnerable = false;
    private boolean hasKey = false;

    public Player(TextureAtlas atlas, TiledMapTileLayer layer,
                  GameScreen screen, Vector2 pos){
        super(atlas,layer,screen,pos);
        animationSetup();
        hearts = new ArrayList<Texture>();
        setMaxHealth(20);
        setHealth(20);
        updateHearts();
        setOriginCenter();

    }
    public void update(float x, float y, float delta){

        Vector2 end = new Vector2(x-getHeight()/2,y-getHeight()/2);
        setPosition(getX(), getY());
        distance = getPosition().dst(end);

        // Speed is relative to how far away you touch. (for more precise control)
        // We also cap the speed
        speed = distance * 3;
        if (speed > 150) {
            speed = 150;
        }
        // set the direction vector equal to the line between touch and sprite
        setDirection(end.x - getPosition().x, end.y - getPosition().y);
        // normalize the vector (make its length equal to 1)
        setDirection(getDirection().nor());
        // keep our old position for the collisionDetection() method
        setOldPosition(getX(), getY());
        // update the position
        setPosition(getPosition().x += getDirection().x * delta * speed,
                    getPosition().y += getDirection().y * delta * speed);


        // animate() decides which animation to use if any
        animate();
        // collisionDetection will fix our position if we collide with anything
        if(collisionDetectionX()){
            setPosition(getOldPosition().x,getPosition().y);
        }
        if(collisionDetectionY()){
            setPosition(getPosition().x,getOldPosition().y);
        }
        // finally once we've done all our math set X and Y to the position vector
        setX(getPosition().x);
        setY(getPosition().y);
        getBoundingRectangle().setSize(500);
        updateHearts();
    }

    public void animationSetup(){
        setTextureAtlas(new TextureAtlas(
                Gdx.files.internal("entities/charles/charles.pack")));
        TextureAtlas tempTextureAtlas = getTextureAtlas();
        downAnimation = new Animation(0.1f,
                tempTextureAtlas.findRegion("4"),
                tempTextureAtlas.findRegion("14"),
                tempTextureAtlas.findRegion("4"),
                tempTextureAtlas.findRegion("10")
        );
        upAnimation = new Animation(0.1f,
                tempTextureAtlas.findRegion("0"),
                tempTextureAtlas.findRegion("2"),
                tempTextureAtlas.findRegion("0"),
                tempTextureAtlas.findRegion("13")
        );
        leftAnimation = new Animation(0.1f,
                tempTextureAtlas.findRegion("5"),
                tempTextureAtlas.findRegion("15"),
                tempTextureAtlas.findRegion("5"),
                tempTextureAtlas.findRegion("9")
        );
        rightAnimation = new Animation(0.1f,
                tempTextureAtlas.findRegion("1"),
                tempTextureAtlas.findRegion("3"),
                tempTextureAtlas.findRegion("1"),
                tempTextureAtlas.findRegion("12")
        );
        idleAnimation = new Animation(0,
                tempTextureAtlas.findRegion("4")
        );
        animation = idleAnimation;
    }
    // called whenever the player moves
    private void animate(){

        // if x,y are positive
        if(getDirection().x>0&&getDirection().y>0){
            if(getDirection().x>getDirection().y){
                animation = rightAnimation;
            }else{
                animation = upAnimation;
            }
            // pos x neg y
        }else if(getDirection().x>0&&getDirection().y<0){
            if(getDirection().x>Math.abs(getDirection().y)){
                animation = rightAnimation;
            }else{
                animation = downAnimation;
            }
            // neg x neg y
        }else if(getDirection().x<0&&getDirection().y<0){
            if(getDirection().x<getDirection().y){
                animation = leftAnimation;
            }else{
                animation = downAnimation;
            }
            // neg x pos y WORKING
        }else if(getDirection().x<0&&getDirection().y>0){
            if(Math.abs(getDirection().x)>getDirection().y){
                animation = leftAnimation;
            }else{
                animation = upAnimation;
            }
        }
    }

    public void setIdle(){
        animation = idleAnimation;
    }
    public Animation getAnimation(){
        return animation;
    }
    public boolean isNextTo(String string){
        TiledMapTile tempTile;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                tempTile = getCollisionLayer().getCell(
                        (int)(getX()/getTileWidth()-1+i),
                        (int)(getY()/getTileHeight()-1+j))
                        .getTile();
                if(tempTile.getProperties().containsKey(string))
                    return true;
            }
        }
        return false;
    }
    public void updateHearts(){
        //Gdx.app.log("size",""+hearts.size());
        if(hearts.size()<getMaxHealth()){
            for(int i=0;i<getMaxHealth();i++){
                hearts.add(i,emptyHeartTexture);
            }
        }
        for(int i=0;i<getHealth();i++){
            hearts.set(i,heartTexture);
        }
        for(int i=getHealth();i<getMaxHealth();i++){
            hearts.set(i,emptyHeartTexture);
        }
    }

    public ArrayList<Texture> getHearts(){
        return hearts;
    }

    public float getSpeed(){
        return speed;
    }

    public void setInvulnerable(boolean state){
        invulnerable = state;
    }
    public boolean getInvulnerable(){
        return invulnerable;
    }

    public void takeDamage(int dmg){
        if(!invulnerable){
            setHealth(getHealth()-dmg);
        }
    }
    public void attack(float velX, float velY){
        // Find the direction the fling occured in dir = new Vector2(velocityX,velocityY)
        // Normalize the direction vector dir.nor()
        // Spawn a new projectile at the player's position and add it to the projectile list
        // it will then move towards the normalized direction vector
        // if it collides with something we deal damage to it and kill the projectile
        //     should we check every projectile with every entity?
        //         We could do a prelim check on x and y coords for each
    }
}

package com.liamhartery.pinch.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector3;

// TODO get a custom player image
// TODO give the player an attack
// TODO give the player an inventory
// TODO give the player health
// TODO allow the player to die

public class Player extends Sprite {

    // movement
    public Vector3 pos = new Vector3();
    private Vector3 oldPos = new Vector3();
    private Vector3 dir = new Vector3();
    private float speed = 200;
    private float distance;

    // collision
    private float tileWidth,tileHeight;
    private TiledMapTileLayer collisionLayer;
    private boolean collisionX = false, collisionY = false;

    // animation
    private TextureAtlas textureAtlas;
    private Animation animation;
    private Animation downAnimation;
    private Animation upAnimation;
    private Animation leftAnimation;
    private Animation rightAnimation;
    private Animation idleAnimation;

    public Player(Texture TextureAtlas, TiledMapTileLayer layer){
        super(TextureAtlas);
        collisionLayer = layer;
        tileWidth = this.collisionLayer.getTileWidth();
        tileHeight = this.collisionLayer.getTileHeight();

        // setting up animations
        textureAtlas = new TextureAtlas(
                Gdx.files.internal("entities/charles/charles.pack"));
        downAnimation = new Animation(0.1f,
                textureAtlas.findRegion("4"),
                textureAtlas.findRegion("14"),
                textureAtlas.findRegion("4"),
                textureAtlas.findRegion("10")
                );
        upAnimation = new Animation(0.1f,
                textureAtlas.findRegion("0"),
                textureAtlas.findRegion("2"),
                textureAtlas.findRegion("0"),
                textureAtlas.findRegion("13")
                );
        leftAnimation = new Animation(0.1f,
                textureAtlas.findRegion("5"),
                textureAtlas.findRegion("15"),
                textureAtlas.findRegion("5"),
                textureAtlas.findRegion("9")
                );
        rightAnimation = new Animation(0.1f,
                textureAtlas.findRegion("1"),
                textureAtlas.findRegion("3"),
                textureAtlas.findRegion("1"),
                textureAtlas.findRegion("12")
                );
        idleAnimation = new Animation(0,
                textureAtlas.findRegion("4")
                );
        animation = idleAnimation;
    }

    public void dispose(){
        textureAtlas.dispose();
    }

    /*
    * This method acts as an update() method in a way, since the only time certain
    * checks have to be made is when the player actually moves, which will happen
    * with almost every action the user performs.
     */
    public void moveTowards(Vector3 end, float delta){
        // set the position Vector3
        //TODO convert all to Vector2
        pos.set(getX(),getY(),0);
        distance = pos.dst(end);

        // Speed is relative to how far away you touch. (for more precise control)
        // We also cap the speed
        speed = distance*3;
        if(speed>100){
            speed = 100;
        }
        if(distance<=1) {
            pos.set(end.x,end.y,0);
        return;
        }
        dir.set(end.x - pos.x, end.y - pos.y, 0);
        dir.nor();
        // keep our old position in case we collide with anything
        oldPos.set(getX(),getY(),0);
        // update the position
        pos.set(pos.x+=dir.x*delta*speed,pos.y+=dir.y*delta*speed,0);

        /*
         * Deciding which animation to use
         * TODO make this a method?
         */
        // if x,y are positive
        if(dir.x>0&&dir.y>0){
            if(dir.x>dir.y){
                animation = rightAnimation;
            }else{
                animation = upAnimation;
            }

        // pos x neg y WORKING
        }else if(dir.x>0&&dir.y<0){
            if(dir.x>Math.abs(dir.y)){
                animation = rightAnimation;
            }else{
                animation = downAnimation;
            }
        // neg x neg y
        }else if(dir.x<0&&dir.y<0){
            if(dir.x<dir.y){
                animation = leftAnimation;
            }else{
                animation = downAnimation;
            }
        // neg x pos y WORKING
        }else if(dir.x<0&&dir.y>0){
            if(Math.abs(dir.x)>dir.y){
                animation = leftAnimation;
            }else{
                animation = upAnimation;
            }
        }

        /* Collision detection using tiled map
        TODO clipping into things at certain angles
        TODO make this a method?
        */
        // if moving towards the left (negative x)
        if(dir.x<0){
            collisionX = collisionLayer.getCell(
                    (int)((getX()-getWidth()/2)/tileWidth),
                    (int)((getY())/tileHeight))
                        .getTile().getProperties().containsKey("blocked");
        }
        // if we're instead moving towards the right (positive x)
        else if(dir.x>0){
            collisionX = collisionLayer.getCell(
                    (int)((getX()+getWidth()/2)/tileWidth),
                    (int)(((getY())/tileHeight)))
            .getTile().getProperties().containsKey("blocked");
        }
        // if moving downward (negative y)
        if(dir.y<0){
            collisionY = collisionLayer.getCell(
                    (int)((getX())/tileWidth),
                    (int)((getY()-getHeight()/2)/tileHeight))
                    .getTile().getProperties().containsKey("blocked");
        }
        // if moving upward (positive y)
        else if(dir.y>0){
            collisionY = collisionLayer.getCell(
                    (int)((getX())/tileWidth),
                    (int)((getY()+getHeight()/2)/tileHeight))
                    .getTile().getProperties().containsKey("blocked");
        }
        if(collisionX){
            pos.set(oldPos.x,pos.y,0);
        }
        if(collisionY){
            pos.set(pos.x,oldPos.y,0);
        }
        setX(pos.x);
        setY(pos.y);

    }
    public float getSpeed(){
        return speed;
    }
    public void setSpeed(float speed){
        this.speed = speed;
    }
    public Vector3 getDir(){
        return dir.nor();
    }
    public Animation getAnimation(){return animation;}
    public void setIdle(){animation = idleAnimation;}
}

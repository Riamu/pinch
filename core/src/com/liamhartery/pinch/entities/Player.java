package com.liamhartery.pinch.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector3;

public class Player extends Sprite {
    private Vector3 pos = new Vector3();
    private Vector3 oldPos = new Vector3();
    private Vector3 dir = new Vector3();
    private float speed = 200;
    private float distance;
    private float tileWidth,tileHeight;
    private TiledMapTileLayer collisionLayer;
    private boolean collisionX = false, collisionY = false;

    public Player(Texture texture, TiledMapTileLayer layer){
        super(texture);
        collisionLayer = layer;
        tileWidth = this.collisionLayer.getTileWidth();
        tileHeight = this.collisionLayer.getTileHeight();
    }

    //TODO make the sprite collide with map stuff
    //TODO adjust speed based on distance from point touched
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
        * Collision detection and position updates
        * TODO figure which is more efficient ( |= OR if() )
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
}

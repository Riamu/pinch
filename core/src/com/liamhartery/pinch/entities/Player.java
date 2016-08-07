package com.liamhartery.pinch.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class Player extends Sprite {
    Vector3 pos = new Vector3();
    Vector3 dir = new Vector3();
    float speed = 200;
    float distance;

    public Player(Texture texture){
        super(texture);
    }

    //TODO make the sprite move based on the center not the bottom-left
    public void moveTowards(Vector3 end, float delta){
        // set the pos Vector3
        pos.set(getX(),getY(),0);
        distance = pos.dst(end);

        if(distance<=1) {
        //pos.set(end.x,end.y,0);
        return;
        }
        dir.set(end.x - pos.x, end.y - pos.y, 0);
        dir.nor();
        // this is ugly as fuck but it works
        pos.set(pos.x+=dir.x*delta*speed,pos.y+=dir.y*delta*speed,0);
        setX(pos.x);
        setY(pos.y);
    }
    public float getXDistance(float x){
        float distance = x-getX();
        return distance;
    }
    public float getYDistance(float y){
        float distance = getY()-y;
        return distance;
    }
}

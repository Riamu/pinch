package com.liamhartery.pinch.entities.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.entities.Entity;
import com.liamhartery.pinch.entities.Player;
import com.liamhartery.pinch.screens.GameScreen;


/*
 * FollowBlob can follow the player around once he gets in a certain radius of him. He's also faster
 * HP: 1
 * SPEED: 75
 * DAMAGE: 1
 */

public class FollowBlob extends Entity{
    private int hp = 1;
    private int speed = 25;
    private int damage = 1;

    private RandomXS128 random = new RandomXS128();

    public FollowBlob(TextureAtlas atlas, TiledMapTileLayer layer,
                     GameScreen gameScreen, Vector2 position){
        super(atlas,layer,gameScreen,position);
        setSize(16,10);
        setMaxHealth(hp);
        setHealth(hp);
        setPosition(position);
        setDirection(0,0);
        chooseDirection();
        setX(position.x);
        setY(position.y);
        Entity.getEntities().add(this);
        setAnimation(new Animation(0.5f,
                getTextureAtlas().getRegions().get(0),
                getTextureAtlas().getRegions().get(1)
        ));
    }

    public void chooseDirection(){
        if(getGame().getTiledMap().getLayers().get(getGame().getCurrentLayerInt())
                .equals(getCollisionLayer())) {
            setDirection(new Vector2(getGame().getPlayerPos().x - getPosition().x,
                                    getGame().getPlayerPos().y - getPosition().y).nor());
        }
        else{
            setDirection(0,0);
        }
    }
    public void update(float delta){
        if(getGame().getCurrentLayer()!=getCollisionLayer()){
            return;
        }
        if(!(Math.abs(getGame().getPlayerPos().x-getPosition().x)<150&&
                Math.abs(getGame().getPlayerPos().y-getPosition().y)<150)){
            return;
        }
        chooseDirection();
        setOldPosition(getX(),getY());
        getPosition().x+=delta*speed*getDirection().x;
        getPosition().y+=delta*speed*getDirection().y;
        if (collisionDetectionX()) {
            setPosition(getOldPosition().x, getPosition().y);
        }
        if (collisionDetectionY()) {
            setPosition(getPosition().x, getOldPosition().y);
        }
        //resolveEntityCollisions();
        setX(getPosition().x);
        setY(getPosition().y);
    }
    public void dispose(){
        getEntities().remove(this);
        getTextureAtlas().dispose();
    }

    public int playerDamage(){
        return damage;
    }
}

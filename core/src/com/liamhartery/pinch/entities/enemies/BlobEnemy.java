package com.liamhartery.pinch.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.entities.Entity;
import com.liamhartery.pinch.entities.Player;
import com.liamhartery.pinch.screens.GameScreen;
import com.badlogic.gdx.math.RandomXS128;

/*
 * The blob enemy is a very simple enemy that moves more or less randomly
 * The blob enemy does damage to the player when it touches him
 * it does very little damage and can be killed easily
 *
 * HP: 2
 * Speed: 50
 * Damage: 1
 *
 */

public class BlobEnemy extends Entity {
    private int hp = 2;
    private int speed = 50;
    private int damage = 1;

    private RandomXS128 random = new RandomXS128();

    public BlobEnemy( TextureAtlas atlas, TiledMapTileLayer layer,
                     GameScreen gameScreen, Vector2 position){
        super(atlas,layer,gameScreen,position);
        setSize(16,10);
        setMaxHealth(hp);
        setHealth(hp);
        setPosition(position);
        setDirection(1,1);
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
        float x = random.nextFloat()*2-0.5f;
        float y = random.nextFloat()*2-0.5f;
        setDirection(x,y);
        setDirection(getDirection().nor());
    }
    public void update(float delta){
        if(getGame().getCurrentLayer()!=getCollisionLayer()){
            return;
        }
        setOldPosition(getX(),getY());
        getPosition().x+=delta*speed*getDirection().x;
        getPosition().y+=delta*speed*getDirection().y;
        if (collisionDetectionX()) {
            setPosition(getOldPosition().x, getPosition().y);
            chooseDirection();
        }
        if (collisionDetectionY()) {
            setPosition(getPosition().x, getOldPosition().y);
            chooseDirection();
        }
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


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
 * it damages the player when it touches him, where it then jumps away from the player
 * it doesn't do much damage but has a surprising amount of health
 *
 * Stats:
 *  5hp
 *  1dmg
 *  50 movement speed
 *
 *  TODO better movement
 *
 */

public class BlobEnemy extends Entity {
    private int speed = 25;
    private RandomXS128 random = new RandomXS128();
    private int damage = 1;

    public BlobEnemy( TextureAtlas atlas, TiledMapTileLayer layer,
                     GameScreen gameScreen, Vector2 position){
        super(atlas,layer,gameScreen,position);
        setMaxHealth(5);
        setHealth(5);
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

    public int playerDamage(Player player){
        if(player.getCollisionLayer()==this.getCollisionLayer()) {
            if (player.getBoundingRectangle().overlaps(this.getBoundingRectangle())) {
                return damage;
            }
        }
        return 0;
    }

}


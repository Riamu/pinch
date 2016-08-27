package com.liamhartery.pinch.entities.interactives;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.entities.Entity;
import com.liamhartery.pinch.entities.Player;
import com.liamhartery.pinch.screens.GameScreen;

// TODO better key animation
// TODO different key sprite
public class Key extends Entity {

    public Key(TextureAtlas atlas,TiledMapTileLayer layer,
               GameScreen screen,
               Vector2 pos,
               Player player
                ){
        super(atlas,layer,screen,pos);
        getEntities().add(this);
        setAnimation(new Animation(0.1f,
                atlas.findRegion("key0"),
                atlas.findRegion("key1"),
                atlas.findRegion("key2"),
                atlas.findRegion("key3"),
                atlas.findRegion("key4")
                ));
        setX(pos.x);
        setY(pos.y);
    }

    public void playerGotMe(){
        getEntities().remove(this);
    }

    @Override
    public void takeDamage(int damage){
        // do nothing
    }
}

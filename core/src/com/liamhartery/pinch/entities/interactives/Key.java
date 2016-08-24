package com.liamhartery.pinch.entities.interactives;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.entities.Entity;
import com.liamhartery.pinch.entities.Player;
import com.liamhartery.pinch.screens.GameScreen;

// TODO find a key animation
public class Key extends Entity {
    private Player player;
    public Key(TextureAtlas atlas,TiledMapTileLayer layer,
               GameScreen screen,
               Vector2 pos,
               Player player
                ){
        super(atlas,layer,screen,pos);
        getEntities().add(this);
        this.player = player;
        setAnimation(new Animation(0.1f,
                atlas.findRegion("0")
                ));
    }

    @Override
    public void update(float delta){
        if(player.getBoundingRectangle().overlaps(getBoundingRectangle())
        &&player.getCollisionLayer()==getCollisionLayer()
        ){
            player.giveKey();
            getEntities().remove(this);
        }
    }

}

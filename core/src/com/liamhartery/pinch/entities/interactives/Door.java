package com.liamhartery.pinch.entities.interactives;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.entities.Entity;
import com.liamhartery.pinch.screens.GameScreen;

public class Door extends Entity {
    private Animation openDoor;
    private TiledMapTileLayer layer;
    private TiledMap map;
    private boolean locked = true;

    private Sound unlockedSound = Gdx.audio.newSound
            (Gdx.files.internal("sound/effects/keyhole-lockbox-unlock-04.wav"));
    private Sound lockedSound = Gdx.audio.newSound
            (Gdx.files.internal("sound/effects/keyhole-lockbox-turn-04.wav"));
    public Door(TextureAtlas atlas,
                TiledMapTileLayer layer,
                GameScreen screen,
                Vector2 pos,
                TiledMap map
                ){
        super(atlas,layer,screen,pos);

        this.layer = layer;
        this.map = map;
        if(layer.getName().equals("Collision0")){
            setAnimation(new Animation(0.1f,atlas.findRegion("green_door_closed")));
            openDoor = new Animation(0.1f,atlas.findRegion("green_door_open"));
        }else if(layer.getName().equals("Collision1")){
            setAnimation(new Animation(0.1f,atlas.findRegion("brown_door_closed")));
            openDoor = new Animation(0.1f,atlas.findRegion("brown_door_open"));

        }else{
            setAnimation(new Animation(0.1f,atlas.findRegion("blue_door_closed")));
            openDoor = new Animation(0.1f,atlas.findRegion("blue_door_open"));

        }

        setX(pos.x);
        setY(pos.y);
        getEntities().add(this);
    }

    public void unlock(){
        layer.getCell((int)(getX()/getTileHeight()),(int)(getY()/getTileHeight())+1)
                .setTile(map.getTileSets().getTile(99));
        setAnimation(openDoor);
        locked = false;
        unlockedSound.play(0.5f);

    }
    public boolean isLocked(){
        return locked;
    }

    @Override
    public void dispose(){
        unlockedSound.dispose();
        lockedSound.dispose();
        super.dispose();
    }
    public void playLockedSound(){
        lockedSound.play(0.5f);
    }

}

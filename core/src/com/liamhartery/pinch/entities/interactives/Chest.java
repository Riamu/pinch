package com.liamhartery.pinch.entities.interactives;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.entities.Entity;
import com.liamhartery.pinch.screens.GameScreen;

public class Chest extends Entity {
    private Animation open;
    private Animation closed;
    private Sound chestOpen;

    public Chest(TextureAtlas atlas, TiledMapTileLayer layer, GameScreen screen, Vector2 pos) {
        super(atlas, layer, screen, pos);
        chestOpen = Gdx.audio.newSound(Gdx.files.internal("sound/effects/chestOpen.wav"));
        closed = new Animation(1f,getTextureAtlas().findRegion("chest_closed"));
        open = new Animation(1f,getTextureAtlas().findRegion("chest_open"));
        setAnimation(closed);
        getEntities().add(this);
        setX(getPosition().x);
        setY(getPosition().y);
    }

    public int openChest(){
        if(getAnimation().equals(closed)){
            setAnimation(open);
            chestOpen.play();
            // returns an int equal to the number of items the player should receive
            // this number is then passed to the player.receiveItems(int num) method
            // which will generate a random "num" number of items for the player
            // this is the roguelike element
            return 1;
        }
        return 0;
    }
    public void dispose(){
        chestOpen.dispose();
        super.dispose();
    }
    @Override
    public void update(float delta){
        //setX(getPosition().x);
        //setY(getPosition().y);
    }
    @Override
    public void takeDamage(int dmg){
        // do nothing as the chest can't take damage
    }
}

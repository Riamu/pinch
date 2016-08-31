package com.liamhartery.pinch.entities.interactives;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.liamhartery.pinch.entities.Entity;
import com.liamhartery.pinch.screens.GameScreen;


public class DialogPopper extends Entity {

    private String dialogText;
    private Skin skin;
    private TiledMapTileLayer layer;
    private boolean isLocked = false;
    public DialogPopper(String dialogText,
                        Skin skin,
                        float x,float y,
                        TiledMapTileLayer layer,
                        TextureAtlas textureAtlas,
                        GameScreen screen){

        super(textureAtlas,layer,screen,new Vector2(x,y));
        this.dialogText = dialogText;
        this.skin = skin;
        this.layer = layer;
        setPosition(x,y);
        setSize(16,16);

        setAnimation(new Animation(2f,
                textureAtlas.findRegion("dialogPopper0"),
                textureAtlas.findRegion("dialogPopper1")
                ));
        getEntities().add(this);
    }

    public Dialog popDialog(){
        return
        new Dialog("Tutorial",skin){
            {
                text(dialogText).setScale(Gdx.graphics.getWidth()/500,Gdx.graphics.getHeight()/281);
                button("OK").setScale(Gdx.graphics.getWidth()/500,Gdx.graphics.getHeight()/281);
                isLocked = true;
                getGame().isPaused = true;
            }

            @Override
            protected void result(final Object object){
                isLocked = false;
                getGame().isPaused = false;
            }
        };
    }

    public TiledMapTileLayer getLayer() {
        return layer;
    }

    @Override
    public void takeDamage(int dmg) {
        //do nothing
    }

    public boolean isLocked(){
        return isLocked;
    }
}

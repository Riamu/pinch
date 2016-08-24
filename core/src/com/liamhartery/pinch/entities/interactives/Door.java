package com.liamhartery.pinch.entities.interactives;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.entities.Entity;
import com.liamhartery.pinch.entities.Player;
import com.liamhartery.pinch.screens.GameScreen;

public class Door extends Entity {

    public Door(TextureAtlas atlas,
                TiledMapTileLayer layer,
                GameScreen screen,
                Vector2 pos,
                Player player
                ){
        super(atlas,layer,screen,pos);

    }
}

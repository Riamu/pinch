package com.liamhartery.pinch.items;

import com.badlogic.gdx.graphics.Texture;
import com.liamhartery.pinch.entities.Player;

// TODO add textures to the powerUps

public abstract class Item  {
    private Texture texture;

    public Item(Player player){
        // do things to the player in the override constructors
        // (modify values associated with the item)
    }

    public Texture getTexture(){
        return texture;
    }
}

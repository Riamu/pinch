package com.liamhartery.pinch.items;

import com.badlogic.gdx.graphics.Texture;
import com.liamhartery.pinch.entities.Player;

public class ProjectileDamageUp extends Item{
    private Texture texture;
    public ProjectileDamageUp(Player player){
        super(player);
        player.setProjectileDamage(player.getProjectileDamage()+1);

    }
    public Texture getTexture(){
        return texture;
    }
}

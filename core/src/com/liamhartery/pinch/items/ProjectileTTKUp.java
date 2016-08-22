package com.liamhartery.pinch.items;

import com.badlogic.gdx.graphics.Texture;
import com.liamhartery.pinch.entities.Player;

public class ProjectileTTKUp extends Item{
    private Texture texture;
    public ProjectileTTKUp(Player player){
        super(player);
        player.setProjectileTTK(player.getProjectileTTK()+0.1f);
    }
    public Texture getTexture(){
        return texture;
    }
}

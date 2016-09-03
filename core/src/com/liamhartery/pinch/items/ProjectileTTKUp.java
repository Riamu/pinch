package com.liamhartery.pinch.items;

import com.badlogic.gdx.graphics.Texture;
import com.liamhartery.pinch.entities.Player;

public class ProjectileTTKUp extends Item{
    private Texture texture;

    private float ttkIncrease = 0.1f;
    public ProjectileTTKUp(Player player){
        super(player);
        player.setProjectileTTK(player.getProjectileTTK()+ttkIncrease);
        player.setTempTTK(player.getTempTTK()+ttkIncrease);
    }
    public Texture getTexture(){
        return texture;
    }
}

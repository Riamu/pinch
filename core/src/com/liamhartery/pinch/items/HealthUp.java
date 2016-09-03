package com.liamhartery.pinch.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.liamhartery.pinch.entities.Player;

/*
 * HealthUp gives the player +1 max health and heals him to full
 */
public class HealthUp extends Item{
    Texture texture = new Texture(Gdx.files.internal("icons/halfheart.png"));

    public HealthUp(Player player){
        super(player);
        player.setMaxHealth(player.getMaxHealth()+1);
        player.setTempMaxHP(player.getTempMaxHP()+1);
        player.setHealth(player.getMaxHealth());
        player.updateHearts();
    }

    public Texture getTexture(){
        return texture;
    }
}

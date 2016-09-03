package com.liamhartery.pinch.items;

import com.liamhartery.pinch.entities.Player;

public class ProjectileSpeedUp extends Item{
    int speed = 25;
    public ProjectileSpeedUp(Player player){
        super(player);
        player.setProjectileSpeed(player.getProjectileSpeed()+speed);
        player.setTempSpeed(player.getTempSpeed()+speed);
    }

}

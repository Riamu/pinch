package com.liamhartery.pinch.items;

import com.liamhartery.pinch.entities.Player;

/**
 * Created by liamh on 31-Aug-16.
 */
public class ProjectileSpeedUp extends Item{
    public ProjectileSpeedUp(Player player){
        super(player);
        player.setProjectileSpeed(player.getProjectileSpeed()+25);
    }

}

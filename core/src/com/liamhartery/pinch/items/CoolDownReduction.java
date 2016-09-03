package com.liamhartery.pinch.items;

import com.liamhartery.pinch.entities.Player;

public class CoolDownReduction extends Item{

    float cdredux = 0.1f;
    public CoolDownReduction(Player player){
        super(player);
        player.setProjectileCoolDown(player.getProjectileCoolDown()-cdredux);
        player.setTempCD(player.getTempCD()-cdredux);
    }
}

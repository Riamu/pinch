package com.liamhartery.pinch.items;

import com.liamhartery.pinch.entities.Player;

public class CoolDownReduction extends Item{

    public CoolDownReduction(Player player){
        super(player);
        player.setProjectileCoolDown(player.getProjectileCoolDown()-0.1f);
    }
}

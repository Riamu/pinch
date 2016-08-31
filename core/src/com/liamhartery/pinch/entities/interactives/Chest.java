package com.liamhartery.pinch.entities.interactives;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.liamhartery.pinch.entities.Entity;
import com.liamhartery.pinch.entities.Player;
import com.liamhartery.pinch.screens.GameScreen;

public class Chest extends Entity {
    private Animation open;
    private Animation closed;
    private Sound chestOpen = Gdx.audio.newSound(Gdx.files.internal("sound/effects/chestOpen.wav"));
    private Dialog dialog;
    private Player player;

    public Chest(TextureAtlas atlas, TiledMapTileLayer layer, GameScreen screen, Vector2 pos) {
        super(atlas, layer, screen, pos);
        closed = new Animation(1f,getTextureAtlas().findRegion("chest_closed"));
        open = new Animation(1f,getTextureAtlas().findRegion("chest_open"));
        setAnimation(closed);
        getEntities().add(this);
        setX(getPosition().x);
        setY(getPosition().y);
        dialog = new Dialog("Chest",new Skin(Gdx.files.internal("ui/experimental/uiskin.json"))){
            {
                text("Choose an Item");
                button("Potion","POT");
                button("Damage","DMG");
                button("TTK","TTK");
            }
            @Override
            public void result(Object object){
                if (object.equals("POT")) {
                    player.giveItem("POT");
                } else if (object.equals("DMG")) {
                    player.giveItem("DMG");
                } else if (object.equals("TTK")) {
                    player.giveItem("TTK");
                }
            }
        };
    }
    public int openChest(Player player){
        if(getAnimation().equals(closed)){
            setAnimation(open);
            if((getGame().game.soundEffects)){
                chestOpen.play();
                dialog.setScale(Gdx.graphics.getWidth()/400);
                dialog.show(getGame().stage);
                dialog.setPosition(0,0);
                this.player = player;
            }
            return 1;
        }
        return 0;
    }
    public void dispose(){
        chestOpen.dispose();
        super.dispose();
    }
    @Override
    public void update(float delta){
        //setX(getPosition().x);
        //setY(getPosition().y);
    }
    @Override
    public void takeDamage(int dmg){
        // do nothing as the chest can't take damage
    }
}

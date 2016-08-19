package com.liamhartery.pinch.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.liamhartery.pinch.screens.GameScreen;

/*
 *  Should the projectile class extend Entity or perhaps only Sprite?
 *  If I want advanced things like collision with walls for bouncing projectiles, and automatic
 *  Projectile updating in the render loop then using the entity class would be the way to go.
 *  On the other hand having them be entities will require a lot of cleanup which might result in
 *  sluggish performance on android.
 *
 *  Maybe we can fix that by implementing some sort of hiding that hides the projectile instead of
 *  destroying it, and then resets its position to be ready for the next thing (positional reset
 *  could be done by taking up 1 render cycle) additionally if no projectiles are "ready" a new one
 *  could be created. This is similar to an asset manager but on a smaller scale.
 *
 *  Projectiles should probably be added to an arraylist individual to the entity that spawned it as
 *  well so we can access only those projectiles that come from the player or only those projectiles
 *  that come from enemies, for damage calculating purposes.
 */
public class Projectile extends Entity{
    private Rectangle tempRect;
    private int damage;
    private boolean belongsToPlayer;
    private float speed;
    private Player player;

    public Projectile(TextureAtlas atlas, TiledMapTileLayer layer, GameScreen screen,
                      Vector2 pos, Vector2 dir, Player player){
        super(atlas, layer, screen, pos);
        this.player = player;
        setDirection(dir);
        damage = player.getProjectileDamage(); // change to player.getProjectileDamage();
        speed = player.getProjectileSpeed(); // change to player.getProjectileSpeed();

        setAnimation(new Animation(
                0.1f,
                getTextureAtlas().findRegion("0"),
                getTextureAtlas().findRegion("1")
        )); // TODO get a texture atlas for this thing
        getEntities().add(this);
    }

    public void update(float delta){
        getDirection();
        setPosition(
                getPosition().x+delta*speed*getDirection().x,
                getPosition().y+delta*speed*getDirection().y
        );
        setX(getPosition().x);
        setY(getPosition().y);
        hitSprite();
        if(hitWall()){
            dispose();
        }
    }

    // returns true if the tile the projectile is currently in is a "blocked" tile (wall)
    public boolean hitWall(){
        if(getCollisionLayer().getCell((int)(getX()/getTileWidth()),(int)(getY()/getTileHeight()))
                .getTile().getProperties().containsKey("blocked")){
            return true;
        }
        return false;
    }

    public void dispose(){
        getEntities().remove(this);
        getTextureAtlas().dispose();
        getTexture().dispose();
    }

    // currently checks every single sprite for bounding rectangle collision
    // side question: how efficient is the bounding rectangle.overlaps() method?
    // does it do a check to see if the two boxes are far too far first?
    public void hitSprite(){
        for(int i=0;i<Entity.getEntities().size();i++){
            tempRect = Entity.getEntities().get(i).getBoundingRectangle();
            if(getBoundingRectangle().overlaps(tempRect)){
                Entity.getEntities().get(i).takeDamage(damage);
            }
        }
    }
}

package com.liamhartery.pinch.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
 *  that come from enemies, for damage calculating purposes. We could also just have to different
 *  classes for enemy projectiles and player projectiles
 *
 *  TODO Decide whether to keep current implementation where no checks are made for layer:
 *      Options: - Making the projectile pause like any other entity
 *               - Destroying the projectile upon changing layers
 *               - Keeping it the same as it currently is
 */
public class Projectile extends Sprite{
    private Rectangle tempRect;
    private int damage;
    private boolean belongsToPlayer;
    private float speed;

    private float elapsedTime;
    private float TTK;

    private Animation animation;

    private TextureAtlas textureAtlas;

    private Vector2 direction,position,oldposition;

    private TiledMapTileLayer collisionLayer;

    //private Player player;

    private GameScreen game;
    // So many things in this constructor because the player has control of all of them
    public Projectile(TextureAtlas atlas, TiledMapTileLayer layer, GameScreen screen,
                      Vector2 pos, Vector2 dir, int dmg, int spd,float ttk, Player ply){
        super(atlas.getRegions().get(0));
        //Gdx.app.log("projectile created","");
        textureAtlas = atlas;
        speed = spd;
        direction = dir;
        damage = dmg;
        position = pos;
        setPosition(position.x,position.y);
        collisionLayer = layer;
        elapsedTime = 0;
        game = screen;
        TTK = ttk;
        animation = new Animation(
                0.1f,
                textureAtlas.findRegion("projectile0"),
                textureAtlas.findRegion("projectile1"),
                textureAtlas.findRegion("projectile2"),
                textureAtlas.findRegion("projectile3"),
                textureAtlas.findRegion("projectile4"),
                textureAtlas.findRegion("projectile5"),
                textureAtlas.findRegion("projectile6"),
                textureAtlas.findRegion("projectile7"),
                textureAtlas.findRegion("projectile8")
        );
    }

    // currently projectiles don't actually check if they're on the current layer
    // which actually introduces an interesting skill based combat mechanic that I might keep
    public void update(float delta){
        elapsedTime+=delta;
        if(elapsedTime>TTK){
            dispose();
        }
        position.x +=delta*speed*direction.x;
        position.y+=delta*speed*direction.y;
        setX(position.x+delta*speed*direction.x);
        setY(position.y+delta*speed*direction.y);
        hitSprite();
        if(offMap()){
            dispose();
        }
    }

    // returns true if the tile the projectile is currently in is a "blocked" tile (wall)
    public boolean offMap(){
        if(collisionLayer.getCell((int)(getX()/16),(int)(getY()/16))==null){
            return true;
        }
        return false;
    }

    public void dispose(){
        game.removeProjectile(this);
        textureAtlas.dispose();
    }

    // currently checks every single sprite for bounding rectangle collision
    // side question: how efficient is the bounding rectangle.overlaps() method?
    // does it do a check to see if the two boxes are far too far first?
    public void hitSprite(){
        for(int i=0;i<Entity.getEntities().size();i++){
            tempRect = Entity.getEntities().get(i).getBoundingRectangle();
            if(getBoundingRectangle().overlaps(tempRect)
                    && this.getCollisionLayer()==Entity.getEntities().get(i).getCollisionLayer()){
                Entity.getEntities().get(i).takeDamage(damage);
                game.removeProjectile(this);
            }
        }
    }

    public Animation getAnimation(){
        return animation;
    }
    public TiledMapTileLayer getCollisionLayer(){
        return collisionLayer;
    }
}

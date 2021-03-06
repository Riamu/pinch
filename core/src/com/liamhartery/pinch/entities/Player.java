package com.liamhartery.pinch.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;

import com.liamhartery.pinch.items.CoolDownReduction;
import com.liamhartery.pinch.items.HealthUp;
import com.liamhartery.pinch.items.Item;
import com.liamhartery.pinch.items.ProjectileDamageUp;
import com.liamhartery.pinch.items.ProjectileSpeedUp;
import com.liamhartery.pinch.items.ProjectileTTKUp;
import com.liamhartery.pinch.screens.GameScreen;

import java.util.ArrayList;

public class Player extends Entity{
    // Textures
    private Texture heartTexture = new Texture(Gdx.files.internal("icons/fullheart.png"));
    private Texture emptyHeartTexture = new Texture(Gdx.files.internal("icons/emptyheart.png"));
    private Texture key = new Texture(Gdx.files.internal("entities/key/key_icon.png"));
    private TextureAtlas indicatorAtlas = new TextureAtlas(Gdx.files.internal("ui/indicator.pack"));
    private ArrayList<Texture> hearts;
    private ArrayList<Texture> keys;

    // Movement variables
    private float speed;
    private float distance;

    // animations
    private Animation animation;
    private Animation downAnimation;
    private Animation upAnimation;
    private Animation leftAnimation;
    private Animation rightAnimation;
    private Animation idleAnimation;

    private Animation indicator;
    private Animation indicator0;
    private Animation indicator1;
    private Animation indicator2;

    private Animation teleportUpAnimation;
    private Animation teleportDownAnimation;

    // booleans
    private boolean invulnerable = false;

    // projectile stuff (TTK = time to kill)
    private float coolDown;
    private int projectileDamage;
    private int projectileSpeed;
    private float projectileTTK;

    // powerups for current stage only
    private float tempCD;
    private int tempDamage;
    private int tempSpeed;
    private float tempTTK;
    private int tempMaxHP;

    // items stuff
    private ArrayList<Item> items = new ArrayList<Item>();
    RandomXS128 random = new RandomXS128();

    // sounds
    private Sound keySound = Gdx.audio.newSound
            (Gdx.files.internal("sound/effects/keys-rattle-03.wav"));
    private Sound hitSound = Gdx.audio.newSound
            (Gdx.files.internal("sound/effects/hitSound.mp3"));
    private Sound swooshSound = Gdx.audio.newSound(
            Gdx.files.internal("sound/effects/projectileSwoosh.wav"));
    private Sound resetJingle = Gdx.audio.newSound(
            Gdx.files.internal("sound/effects/resetJingle.mp3"));

    private float elapsedTime = 0;

    public Player(TextureAtlas atlas, TiledMapTileLayer layer,
                  GameScreen screen, Vector2 pos){
        super(atlas,layer,screen,pos);
        // this is a method that sets up player animations for us
        animationSetup();
        // get our hearts ArrayList ready
        hearts = new ArrayList<Texture>();
        // get our key ArrayList ready;
        keys = new ArrayList<Texture>();
        // when we have a new player it's clearly the start of a set of levels so health should be
        // at its minimum value
        setMaxHealth(3);
        setHealth(3);
        // updateHearts will update the hearts ArrayList because our max and current health changed
        updateHearts();
        // not sure if this line does anything, will figure that out eventually
        setOriginCenter();
        // projectile variables!
        projectileDamage = 1;
        projectileSpeed = 125;
        projectileTTK = 1;
        coolDown = 0.5f;

    }

    @Override
    public void dispose(){
        heartTexture.dispose();
        emptyHeartTexture.dispose();
        key.dispose();
        keySound.dispose();
        hitSound.dispose();
        swooshSound.dispose();
        resetJingle.dispose();
        super.dispose();
    }
    public void update(float x, float y, float delta){
        Vector2 end = new Vector2(x-getWidth()/2,y-getHeight()/2);
        setPosition(getX(), getY());
        // Speed is relative to how far away you touch. (for more precise control)
        // We also cap the speed
        distance = getPosition().dst(end);
        speed = distance * 3;
        if (speed > 150) {
            speed = 150;
        }
        // set the direction vector equal to the line between touch and sprite
        setDirection(end.x - getPosition().x, end.y - getPosition().y);
        // normalize the vector (make its length equal to 1)
        setDirection(getDirection().nor());
        // keep our old position for the collisionDetection() method
        setOldPosition(getX(), getY());
        // update the position
        setPosition(getPosition().x += getDirection().x * delta * speed,
                    getPosition().y += getDirection().y * delta * speed);


        // animate() decides which animation to use if any
        animate();
        // collisionDetection will fix our position if we collide with anything
        if(collisionDetectionX()){
            setPosition(getOldPosition().x,getPosition().y);
        }
        if(collisionDetectionY()){
            setPosition(getPosition().x,getOldPosition().y);
        }
        // finally once we've done all our math set X and Y to the position vector
        setX(getPosition().x);
        setY(getPosition().y);
        //updateHearts(); // I don't think this needs to be done

        if(getCollisionLayer().getName().equals("Collision0")){
            indicator = indicator0;
        }else if(getCollisionLayer().getName().equals("Collision1")){
            indicator = indicator1;
        }else if(getCollisionLayer().getName().equals("Collision2")){
            indicator = indicator2;
        }
    }

    public void animationSetup(){
        // to avoid making too many calls to getTextureAtlas() we make a temp one
        TextureAtlas tempTextureAtlas = getTextureAtlas();

        // here we just set up our different animations
        downAnimation = new Animation(0.1f,
                tempTextureAtlas.findRegion("4"),
                tempTextureAtlas.findRegion("14"),
                tempTextureAtlas.findRegion("4"),
                tempTextureAtlas.findRegion("10")
        );
        upAnimation = new Animation(0.1f,
                tempTextureAtlas.findRegion("0"),
                tempTextureAtlas.findRegion("2"),
                tempTextureAtlas.findRegion("0"),
                tempTextureAtlas.findRegion("13")
        );
        leftAnimation = new Animation(0.1f,
                tempTextureAtlas.findRegion("5"),
                tempTextureAtlas.findRegion("15"),
                tempTextureAtlas.findRegion("5"),
                tempTextureAtlas.findRegion("9")
        );
        rightAnimation = new Animation(0.1f,
                tempTextureAtlas.findRegion("1"),
                tempTextureAtlas.findRegion("3"),
                tempTextureAtlas.findRegion("1"),
                tempTextureAtlas.findRegion("12")
        );
        idleAnimation = new Animation(0,
                tempTextureAtlas.findRegion("4")
        );
        // by default our animation is idle of course
        animation = idleAnimation;

        // Indicator animations
        indicator0 = new Animation(0f, indicatorAtlas.findRegion("indicator0"));
        indicator1 = new Animation(0f, indicatorAtlas.findRegion("indicator1"));
        indicator2 = new Animation(0f, indicatorAtlas.findRegion("indicator2"));
        indicator = indicator1;
    }
    // called whenever the player moves
    // basically just calls animation = (whatever animation corresponds to direction)
    private void animate(){

        // if x,y are positive
        if(getDirection().x>0&&getDirection().y>0){
            if(getDirection().x>getDirection().y){
                animation = rightAnimation;
            }else{
                animation = upAnimation;
            }
            // pos x neg y
        }else if(getDirection().x>0&&getDirection().y<0){
            if(getDirection().x>Math.abs(getDirection().y)){
                animation = rightAnimation;
            }else{
                animation = downAnimation;
            }
            // neg x neg y
        }else if(getDirection().x<0&&getDirection().y<0){
            if(getDirection().x<getDirection().y){
                animation = leftAnimation;
            }else{
                animation = downAnimation;
            }
            // neg x pos y WORKING
        }else if(getDirection().x<0&&getDirection().y>0){
            if(Math.abs(getDirection().x)>getDirection().y){
                animation = leftAnimation;
            }else{
                animation = upAnimation;
            }
        }
    }
    // sets current animation to idle
    public void setIdle(){
        animation = idleAnimation;
    }
    public Animation getAnimation(){
        return animation;
    }
    /*
    public boolean isNextTo(String string){
        TiledMapTile tempTile;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                tempTile = getCollisionLayer().getCell(
                        (int)(getX()/getTileWidth()-1+i),
                        (int)(getY()/getTileHeight()-1+j))
                        .getTile();
                if(tempTile.getProperties().containsKey(string))
                    return true;
            }
        }
        return false;
    }
    */

    // update the hearts texture array
    public void updateHearts(){
        // this changes how many slots we have in the hearts ArrayList
        if(hearts.size()<getMaxHealth()){
            for(int i=hearts.size();i<getMaxHealth();i++){
                hearts.add(i,emptyHeartTexture);
            }
        }
        // we make sure that we have enough filled hearts
        for(int i=0;i<getHealth();i++){
            hearts.set(i,heartTexture);
        }
        // any damage is represented with an empty heart texture
        for(int i=getHealth();i<getMaxHealth();i++){
            hearts.set(i,emptyHeartTexture);
        }
    }

    // return the hearts ArrayList
    public ArrayList<Texture> getHearts(){
        return hearts;
    }

    public float getSpeed(){
        return speed;
    }
    public void setInvulnerable(boolean state){
        invulnerable = state;
    }
    public boolean getInvulnerable(){
        return invulnerable;
    }

    // take damage only if not invulnerable
    public void takeDamage(int dmg){
        if(!invulnerable){
            setHealth(getHealth()-dmg);
        }
    }
    public void playHitSound(){
        if(getGame().game.soundEffects)
            hitSound.play(0.25f);
    }
    // the attack method
    public void attack(float velX, float velY){
        // if it collides with something we deal damage to it and kill the projectile
        //     should we check every projectile with every entity?
        //         We could do a prelim check on x and y coords for each
        // TODO be able to shoot more than 1 projectile from you at once as a powerup
        Vector2 proDir = new Vector2(velX,-velY);
        proDir.nor();
        Vector2 proPos = new Vector2(getPosition());
        //proPos.x+=8;
        //proPos.y+=8;
        getGame().addProjectile(new Projectile(new TextureAtlas(
                Gdx.files.internal("entities/projectile/projectile.pack")),
                getCollisionLayer(),
                getGame(),
                proPos,
                proDir,
                projectileDamage,
                projectileSpeed,
                projectileTTK,
                this
        ));
        if(getGame().game.soundEffects)
            swooshSound.play(0.5f);
    }
    // Receive a random buff/item
    public void receiveRandomItem(){
        int randomInt = random.nextInt(4);

        if(randomInt==0){
            //Gdx.app.log("Player got","Health up");
            items.add(new HealthUp(this));
        }else if(randomInt==1){
            //Gdx.app.log("Player got","Damage up");
            items.add(new ProjectileDamageUp(this));
        }else if(randomInt==2){
            //Gdx.app.log("Player got","TTK up");
            items.add(new ProjectileTTKUp(this));
        }else if(randomInt==3){
            items.add(new ProjectileSpeedUp(this));
        }
    }
    public void giveItem(String str){
        if(str.equals("POT")){
            items.add(new HealthUp(this));
        }else if(str.equals("TTK")){
            items.add(new ProjectileTTKUp(this));
        }else if(str.equals("DMG")){
            items.add(new ProjectileDamageUp(this));
        }else if(str.equals("SPD")){
            items.add(new ProjectileSpeedUp(this));
        }else if(str.equals("CDN")){
            // if cooldown is already at 0 I don't want to cheat the player out of an item
            if(coolDown<=0){
                receiveRandomItem();
                return;
            }
            items.add(new CoolDownReduction(this));
        }
    }
    public void giveKey(){
        keys.add(key);
        if(getGame().game.soundEffects)
            keySound.play();
    }
    public boolean useKey(){
        if (keys.size() != 0) {
            keys.remove(keys.size()-1);
            return true;
        }
        else{
            return false;
        }
    }
    public void loseAllKeys(){
        keys.clear();
    }
    public ArrayList<Texture> getKeys(){
        return keys;
    }
    // returns the coolDown time for attacks
    public float getCoolDown(){
        return coolDown;
    }
    public void setCoolDown(float newCoolDown){
        coolDown = newCoolDown;
    }
    public void setProjectileTTK(float newTTK){
        projectileTTK = newTTK;
    }
    public float getProjectileTTK(){
        return projectileTTK;
    }
    public int getProjectileDamage(){
        return projectileDamage;
    }
    public void setProjectileDamage(int dmg){
        projectileDamage = dmg;
    }
    public void setProjectileSpeed(int speed){projectileSpeed = speed;}
    public int getProjectileSpeed(){return projectileSpeed;}
    public float getProjectileCoolDown(){return coolDown;}
    public void setProjectileCoolDown(float cd){coolDown=cd;}
    public float getElapsedTime(){return elapsedTime;}


    public void resetStage(){
        loseAllKeys();
        projectileSpeed-=tempSpeed;
        setProjectileCoolDown(getProjectileCoolDown()-tempCD);
        projectileDamage-=tempDamage;
        setMaxHealth(getMaxHealth()-tempMaxHP);
        setHealth(getMaxHealth());
        hearts.clear();
        projectileTTK-=tempTTK;
        updateHearts();

        tempSpeed = 0;
        tempCD = 0;
        tempDamage = 0;
        tempMaxHP = 0;
        tempTTK = 0;
        if(getGame().game.soundEffects)
            resetJingle.play();
    }
    public void stagePassed(){
        tempCD = 0;
        tempMaxHP = 0;
        tempSpeed = 0;
        tempTTK = 0;
        tempDamage = 0;
        setMaxHealth(getMaxHealth()+1);
    }
    // temp stage updoots
    // temp speed
    public int getTempSpeed(){return tempSpeed;}
    public void setTempSpeed(int speed){tempSpeed = speed;}
    // temp CD
    public float getTempCD(){return tempCD;}
    public void setTempCD(float CD){tempCD = CD;}
    // tempDamage
    public int getTempDamage(){return tempDamage;}
    public void setTempDamage(int dmg){tempDamage=dmg;}
    // temp Max HP
    public int getTempMaxHP(){return tempMaxHP;}
    public void setTempMaxHP(int HP){tempMaxHP=HP;}
    // temp TTK
    public float getTempTTK(){return tempTTK;}
    public void setTempTTK(float TTK){tempTTK=TTK;}

    public Animation getIndicator(){return indicator;}
}
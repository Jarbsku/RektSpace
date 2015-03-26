package objects;

/**
 * Created by Jarbsku on 11.2.2015.
 */

import java.util.ArrayList;

import resources.Globals;
import resources.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Player extends GameObject{

    private Timer timer;
    private Animation animation, steerUp, steerDown;
    private float accelY;
    private float animationTime = 0;

    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private Bullet removeBullet;

    public Player(Vector2 position){

        this.position = position;
        this.atlas = Globals.playerAtlas;
        this.speed = 2;

        timer = new Timer();
        initAnimations();
        createPolygon();

    }

    private void initAnimations(){

        animation = new Animation(1/8f, this.atlas.findRegions("spasesip"));
        steerUp = new Animation(1/8f, this.atlas.findRegions("steerUp"));
        steerDown = new Animation(1/8f, this.atlas.findRegions("steerDown"));

        animation.setPlayMode(PlayMode.LOOP_PINGPONG);
        steerUp.setPlayMode(PlayMode.LOOP_PINGPONG);
        steerDown.setPlayMode(PlayMode.LOOP_PINGPONG);

        this.sprite = new Sprite(animation.getKeyFrame(0));
    }

    private void movement(){
        accelY = Gdx.input.getAccelerometerY();
        alphaY = (int)(this.speed * -accelY);

        if(((position.y + sprite.getHeight()+alphaY) > Globals.VIEWPORT_HEIGHT) || ((position.y + alphaY < 0))){
            alphaY = 0;
        }
    }

    public void update(SpriteBatch batch, float delta){
        playerAnimation(delta);
        movement();

        position.x += alphaX;
        position.y += alphaY;

        updatePolygon();
        batch.draw(sprite, position.x, position.y);
        alphaY = 0;
        alphaX = 0;
    }

    private void playerAnimation(float delta){

        animationTime += delta;
        if(accelY < -2){
            sprite.setRegion(steerUp.getKeyFrame(animationTime));
        }else if(accelY > 2){
            sprite.setRegion(steerDown.getKeyFrame(animationTime));
        }else{
            sprite.setRegion(animation.getKeyFrame(animationTime));
        }
    }

    //calculates the fire rate of player
    public void shooting(){

        if(!timer.isRunning()){

            timer.start();
            bullets.add(new Bullet(new Vector2((position.x + sprite.getWidth()), (position.y + (sprite.getHeight() / 2))), 6, Globals.playerBullet));
        }else if(timer.isPassed(200)){

            timer.pause();
        }
    }

    //updates all players bullets on screen
    public void updateBullets(SpriteBatch batch){

        for(Bullet b : bullets){
            if(!b.distanceToLive()){
                removeBullet = b;
            }else{
                b.update(batch);
            }
        }

        if(removeBullet != null){
            bullets.remove(removeBullet);
            removeBullet = null;
        }
    }

    public void removeBullet(Bullet b){
        bullets.remove(b);
    }

    public ArrayList<Bullet> getBullets(){
        return bullets;
    }

    private void createPolygon(){
        boundVertecies = new float[]{(position.x + 10), position.y, 0,
                (position.x + sprite.getWidth()), (position.y + (sprite.getHeight() / 2)), 0,
                (position.x + 10), (position.y + sprite.getHeight()), 0};

        bounds = new Polygon(boundVertecies);
    }

    private void updatePolygon(){
        boundVertecies = new float[]{(position.x + 20), position.y,
                (position.x + sprite.getWidth()), (position.y + (sprite.getHeight() / 2)),
                (position.x + 20), (position.y + sprite.getHeight())};

        bounds.setVertices(boundVertecies);
    }

    public boolean isPlayerReady(){
        if(accelY < -2){
            return true;
        }else if(accelY > 2){
            return true;
        }else{
            return false;
        }
    }
}

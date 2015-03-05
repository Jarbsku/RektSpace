package objects;

/**
 * Created by Jarbsku on 11.2.2015.
 */

import resources.Globals;
import resources.Timer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends GameObject {

    private Animation animation;
    private float animationTime = 0;

    private Timer timer;
    private float shootSpeed;
    private float bulletSpeed;

    public Enemy(Vector2 startPosition, float speed, float shootSpeed, float bulletSpeed){
        position = startPosition;
        alphaX = speed;
        atlas = Globals.enemyAtlas;

        timer = new Timer();
        this.shootSpeed = shootSpeed;
        this.bulletSpeed = bulletSpeed;
        initAnimations();
        createPolygon();
    }

    private void initAnimations(){

        animation = new Animation(1/8f, this.atlas.findRegions("spasesip"));
        animation.setPlayMode(PlayMode.LOOP_PINGPONG);
        this.sprite = new Sprite(animation.getKeyFrame(0));

    }

    private void animation(float delta){

        animationTime += delta;
        sprite.setRegion(animation.getKeyFrame(animationTime));

    }

    public void update(SpriteBatch batch, float delta){

        animation(delta);
        position.x -= alphaX;
        sprite.setX(position.x);
        sprite.setY(position.y);
        sprite.flip(true, false);

        if(!timer.isRunning()){
            Globals.enemyBullets.add(new Bullet(new Vector2(position.x, position.y + (this.sprite.getHeight()/2)),
                    -bulletSpeed, new Texture(Gdx.files.internal("images/enemyBullet.png"))));
            timer.start();
        }else if(timer.isPassed(shootSpeed)){
            timer.pause();
        }

        updatePolygon();
        sprite.draw(batch);
    }

    private void createPolygon(){
        boundVertecies = new float[]{(position.x), (position.y + (sprite.getHeight() / 2)),
                (position.x + (sprite.getWidth()-20)), position.y,
                (position.x + (sprite.getWidth()-20)), (position.y + sprite.getHeight())};

        bounds = new Polygon(boundVertecies);
    }

    private void updatePolygon(){
        boundVertecies = new float[]{(position.x), (position.y + (sprite.getHeight() / 2)),
                (position.x + (sprite.getWidth()-20)), position.y,
                (position.x + (sprite.getWidth()-20)), (position.y + sprite.getHeight())};

        bounds.setVertices(boundVertecies);
    }

}
package objects;

/**
 * Created by Jarbsku on 11.2.2015.
 */

import resources.Globals;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;

public class Explosion extends GameObject {

    private Animation ex;
    private float animationTime = 0;
    private Sound explosionSound;
    private boolean soundStarted = false;

    public Explosion(Vector2 position){
        this.position = position;
        atlas = Globals.explosionAtlas;
        ex = new Animation(1/25f, atlas.findRegions("ex"));
        ex.setPlayMode(PlayMode.NORMAL);
        sprite = new Sprite(ex.getKeyFrame(animationTime));
        explosionSound = Globals.explosion;
    }

    public void update (SpriteBatch batch, float delta){

        animationTime += delta;
        sprite.setRegion(ex.getKeyFrame(animationTime));
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);

    }

    public boolean isDoneAnimating(){
        return ex.isAnimationFinished(animationTime);
    }

    public void disposeExplosion(){
        explosionSound.dispose();
    }

    public void playEffect(){
        if(!soundStarted){
            soundStarted = true;
            explosionSound.play();
        }
    }
}

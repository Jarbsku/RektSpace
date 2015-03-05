package objects;

/**
 * Created by Jarbsku on 11.2.2015.
 */

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

public class Bullet extends GameObject {

    private static final float BULLET_TRAVEL_DISTANCE = 500;
    private static final float ENEMY_BULLET_TRAVEL_DISTANCE = 800;

    private Vector2 measure;

    public Bullet(Vector2 startPosition, float bulletSpeed, Texture bulletTexture){

        this.position = new Vector2(startPosition);
        this.alphaX = bulletSpeed;

        this.sprite = new Sprite(bulletTexture);
        position.y = position.y - (sprite.getHeight()/2);
        sprite.setPosition(position.x, position.y);
        createBounds();

        measure = new Vector2(startPosition);
    }

    //bullet distance to live
    public boolean distanceToLive(){
        if((position.x - measure.x) < BULLET_TRAVEL_DISTANCE){
            return true;
        }else{
            return false;
        }
    }

    public boolean distanceToLiveEnemy(){
        if((measure.x - position.x) < ENEMY_BULLET_TRAVEL_DISTANCE){
            return true;
        }else{
            return false;
        }
    }


    public void update(SpriteBatch batch){
        position.x += alphaX;
        sprite.setX(position.x);
        updatePolygon();
        sprite.draw(batch);
    }

    public boolean hit(Polygon otherObject){
        if(Intersector.overlapConvexPolygons(bounds, otherObject)){
            return true;
        }else{
            return false;
        }
    }

    private void createBounds(){
        boundVertecies = new float[]{(position.x), (position.y),
                (position.x + sprite.getWidth()), position.y,
                (position.x + sprite.getWidth()), (position.y + sprite.getHeight()),
                (position.x), (position.y + sprite.getHeight())};

        bounds = new Polygon(boundVertecies);
    }

    private void updatePolygon(){
        boundVertecies = new float[]{(position.x), (position.y),
                (position.x + sprite.getWidth()), position.y,
                (position.x + sprite.getWidth()), (position.y + sprite.getHeight()),
                (position.x), (position.y + sprite.getHeight())};

        bounds.setVertices(boundVertecies);
    }

}

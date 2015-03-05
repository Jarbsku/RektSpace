package objects;


/**
 * Created by Jarbsku on 11.2.2015.
 */

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class GameObject {

    protected Vector2 position;
    protected float alphaX;
    protected float alphaY;
    protected float speed;

    protected Sprite sprite;
    protected TextureAtlas atlas;

    protected Polygon bounds;
    protected float[] boundVertecies;


    public Vector2 getPosition() { return position; }

    public float getAlphaX() { return alphaX; }

    public void setAlphaX(float alphaX){ this.alphaX = alphaX; }

    public float getAlphaY() { return alphaY; }

    public Sprite getSprite() { return sprite; }

    public Polygon getBounds() { return bounds; }

}

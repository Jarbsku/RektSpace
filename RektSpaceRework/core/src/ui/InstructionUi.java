package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


import com.badlogic.gdx.graphics.g2d.Animation;
import resources.Globals;

/**
 * Created by Jarbsku on 24.3.2015.
 */
public class InstructionUi {

    private TextureAtlas atlas;
    private Animation animation;
    private Sprite sprite;

    private BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/instFont.fnt"));

    private float animationTime = 0;

    public InstructionUi(int width, int height){ init(width,height);}

    public void render(float delta, SpriteBatch batch){
        updateAnimation(delta);
        batch.begin();
        sprite.draw(batch);
        font.draw(batch, "TILT TO START", (Globals.VIEWPORT_WIDTH / 2 ) - 91, (Globals.VIEWPORT_HEIGHT / 2) - 20);
        batch.end();
    }

    private void init(int width, int height){
        atlas = Globals.instructionAtlas;
        animation = new Animation(1/2f, this.atlas.findRegions("inst"));
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        sprite = new Sprite(animation.getKeyFrame(0));

        sprite.setPosition((width - sprite.getWidth()) / 2, (height - sprite.getHeight()) / 2);
    }

    private void updateAnimation(float delta){
        animationTime += delta;
        sprite.setRegion(animation.getKeyFrame(animationTime));

    }
}

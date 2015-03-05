package screens;

import resources.Globals;
import resources.ScreenStack;
import resources.TweenTool;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;

import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Jarbsku on 11.2.2015.
 */
public class SplashScreen implements Screen{

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Sprite logo;

    private TweenManager manager;


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        logo.draw(batch);
        batch.end();

        manager.update(delta);
    }

    @Override
    public void show() {

        batch = new SpriteBatch();
        manager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new TweenTool());

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Globals.VIEWPORT_WIDTH, Globals.VIEWPORT_HEIGHT);


        logo = new Sprite(new Texture(Gdx.files.internal("images/splash.png")));
        logo.setPosition(0 , 0);

        camera.position.x = logo.getOriginX();
        camera.position.y = logo.getOriginY();
        camera.update();

        Tween.set(logo, TweenTool.ALPHA).target(0).start(manager);
        Tween.to(logo, TweenTool.ALPHA, 1f).target(1).repeatYoyo(1, 1f).setCallback(new TweenCallback() {

            @Override
            public void onEvent(int arg0, BaseTween<?> arg1) {
                dispose();
                ScreenStack.setScreen(new GameScreen());
            }

        }).start(manager);

        manager.update(Float.MIN_VALUE);
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void hide() { }

    @Override
    public void pause() { }

    @Override
    public void resume() {	}

    @Override
    public void dispose() {
        manager = null;
        batch.dispose();
        logo.getTexture().dispose();
    }

}

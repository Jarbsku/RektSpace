package screens;

/**
 * Created by Jarbsku on 11.2.2015.
 */

import java.util.ArrayList;

import objects.Bullet;
import objects.Enemy;
import objects.Explosion;
import objects.Player;
import resources.DataSaver;
import resources.Globals;
import resources.Timer;
import ui.MainMenuUi;
import ui.OptionsUi;
import ui.ScoreUi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen{
    private static final float MAX_SCROLL_SPEED = 0.8f;
    private static final float MIN_SCROLL_SPEED = 0.15f;
    private static final int PUSH_ENEMY_OUT_FROM_SCREEN = 40;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private MainMenuUi mainMenuUi;
    private OptionsUi optionsUi;
    private ScoreUi scoreUi;

    //variables for scrolling background
    private float scrollTime = 0;
    private float scrollSpeed = 0.12f;
    private Texture background;
    private Sprite backgroundSprite;

    //variables and arrays for gameobjects
    private Player player;
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private ArrayList<Explosion> explosions = new ArrayList<Explosion>();

    private Enemy removedEnemy, spawnedEnemy, secondSpawnedEnemy;
    private Bullet removedBullet;
    private Explosion removedExplosion;

    private Music gameMusic;

    private Timer spawnTimer;

    private DataSaver dataSaver;

    private boolean secondSpawn = false;

    private BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/ingameFont.fnt"));

    private CharSequence cs;
    //for debug
    private ShapeRenderer sh = new ShapeRenderer();
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        scrollTime += delta * scrollSpeed;
        if(scrollTime>1.0f){
            scrollTime = 0.0f;
        }
        if(Globals.MUSIC_ON){
            gameMusic.setVolume(1);
        }else{
            gameMusic.setVolume(0);
        }

        backgroundSprite.setU(scrollTime);
        backgroundSprite.setU2(scrollTime+1);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        backgroundSprite.draw(batch);

        if(Globals.STAGE_KEY == Globals.GAME_STAGE){
            //updates goes here
            cs = ""+Globals.highScore;
            System.out.println(font.getBounds(cs).width);
            font.draw(batch, ""+Globals.score, (Globals.VIEWPORT_WIDTH / 2 ) - (font.getBounds(cs).width / 2), Globals.VIEWPORT_HEIGHT-25);
            if(player != null){
                player.update(batch, delta);
                player.updateBullets(batch);
            }

            for(Bullet b : Globals.enemyBullets){
                if(!b.distanceToLiveEnemy()){
                    removedBullet = b;
                }else{
                    b.update(batch);
                }
            }

            for(Enemy e : enemies){
                e.update(batch, delta);
            }

            for(Explosion e : explosions){
                e.update(batch, delta);
                if(Globals.SOUND_ON){
                    e.playEffect();
                }
            }
        }

        if(Globals.STAGE_KEY == Globals.DEATH_STAGE){

            //update all remaining effects and objects
            for(Bullet b : Globals.enemyBullets){
                b.update(batch);
            }

            for(Enemy e : enemies){
                e.update(batch, delta);
            }

            for(Explosion e : explosions){
                e.update(batch, delta);
                if(Globals.SOUND_ON){
                    e.playEffect();
                }
            }
            removeExplosions();
        }

        if(Globals.STAGE_KEY == Globals.POSTGAME_STAGE || Globals.STAGE_KEY == Globals.OPTION_STAGE){

            //update all remaining effects and objects
            for(Bullet b : Globals.enemyBullets){
                b.update(batch);
            }
            for(Enemy e : enemies){
                e.update(batch, delta);
            }
        }

        if(Globals.STAGE_KEY == Globals.PREGAME_STAGE){
            if(player != null){
                player.update(batch, delta);
            }
        }

        batch.end();

		/*/for drawing debug lines. Remove this debug when not needed
		sh.setProjectionMatrix(batch.getProjectionMatrix());
		sh.setColor(Color.GREEN);
		sh.begin(ShapeType.Line);

			if(player != null){
				sh.triangle(player.getBounds().getVertices()[0], player.getBounds().getVertices()[1], player.getBounds().getVertices()[2],
							player.getBounds().getVertices()[3], player.getBounds().getVertices()[4], player.getBounds().getVertices()[5]);
			}

			for(Enemy e : enemies){
				sh.triangle(e.getBounds().getVertices()[0], e.getBounds().getVertices()[1], e.getBounds().getVertices()[2],
							e.getBounds().getVertices()[3], e.getBounds().getVertices()[4], e.getBounds().getVertices()[5]);
			}

			for(Bullet b : Globals.enemyBullets){
				sh.polygon(b.getBounds().getVertices());
			}

		sh.end();
		//-------------------------------------------------------*/

        //THE MIGHTY GAME STAGE MACHINE!
        switch(Globals.STAGE_KEY){

            case Globals.MAIN_STAGE:
                mainMenuUi.render();
                break;

            case Globals.OPTION_STAGE:
                optionsUi.render();
                break;

            case Globals.PREGAME_STAGE:
                preGameAnimation();
                break;

            case Globals.GAME_STAGE:
                gamingStage();
                break;

            case Globals.DEATH_STAGE:

                if(spawnTimer.isRunning()){
                    spawnTimer.pause();
                }

                postGameAnimation();

                if(Globals.score > Globals.highScore){
                    Globals.highScore = Globals.score;
                    dataSaver.saveHighScore();
                }

                break;

            case Globals.POSTGAME_STAGE:
                scoreUi.render();
                break;
        }
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Globals.VIEWPORT_WIDTH, Globals.VIEWPORT_HEIGHT);
        camera.setToOrtho(false, Globals.VIEWPORT_WIDTH, Globals.VIEWPORT_HEIGHT);
        camera.update();

        initBackground();
        initGameObjects();

        dataSaver = new DataSaver();
        dataSaver.onLoad();

        spawnTimer = new Timer();
        mainMenuUi = new MainMenuUi();
        optionsUi = new OptionsUi(dataSaver);
        scoreUi = new ScoreUi();

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/gameMusic.mp3"));
        gameMusic.setLooping(true);
        gameMusic.play();
        if(!Globals.MUSIC_ON){
            gameMusic.setVolume(0);
        }
    }


    private void initBackground(){

        background = new Texture(Gdx.files.internal("images/bg.png"));
        background.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        backgroundSprite = new Sprite(background, 0 ,0 , 1000, 500);

    }

    private void initGameObjects(){
        player = new Player(new Vector2(-80, 208));
    }

    private void preGameAnimation(){

        if(player == null){
            player = new Player(new Vector2(-80, 208));
        }
        if(enemies.size() > 0){
            enemies.clear();
        }
        if(Globals.enemyBullets.size() > 0){
            Globals.enemyBullets.clear();
        }
        if(explosions.size() > 0){
            explosions.clear();
        }

        if(scrollSpeed < MAX_SCROLL_SPEED){
            scrollSpeed += 0.005;
        }else if(scrollSpeed > MAX_SCROLL_SPEED) {
            scrollSpeed = MAX_SCROLL_SPEED;
        }else if(scrollSpeed == MAX_SCROLL_SPEED){
            Globals.STAGE_KEY = Globals.GAME_STAGE;
        }else{
            //do nothing
        }

        if(player.getPosition().x < 10){
            player.setAlphaX(1);
        }
    }

    private void postGameAnimation(){
        if(scrollSpeed > MIN_SCROLL_SPEED){
            scrollSpeed -= 0.008;
        }else if(scrollSpeed < MIN_SCROLL_SPEED) {
            scrollSpeed = MIN_SCROLL_SPEED;
        }else if(scrollSpeed == MIN_SCROLL_SPEED ){
            Globals.STAGE_KEY = Globals.POSTGAME_STAGE;
        }else{
            //do nothing
        }
    }

    private void checkHits(){

        for(Bullet b : player.getBullets()){
            for(Enemy e : enemies){
                if(b.hit(e.getBounds())){
                    removedEnemy = e;
                    removedBullet = b;
                    explosions.add(new Explosion(e.getPosition()));
                    Globals.score++;
                }
            }
        }

        if(removedBullet != null && removedEnemy != null){
            player.removeBullet(removedBullet);
            enemies.remove(removedEnemy);
            removedBullet = null;
            removedEnemy = null;
        }

        for(Bullet b : Globals.enemyBullets){
            if(b.hit(player.getBounds())){
                Globals.STAGE_KEY = Globals.DEATH_STAGE;
                removedBullet = b;
                explosions.add(new Explosion(player.getPosition()));
            }
        }

        if(removedBullet != null){
            Globals.removeBullet(removedBullet);
            removedBullet = null;
        }

        if(Globals.STAGE_KEY == Globals.DEATH_STAGE){
            player = null;
        }

    }

    private void gamingStage(){

        if(player != null){
            player.shooting();
            checkHits();
            removeExplosions();
            enemySpawner();
        }
    }

    private void removeExplosions(){
        for(Explosion e : explosions){
            if(e.isDoneAnimating()){
                removedExplosion = e;
            }
        }

        if(removedExplosion != null){
            explosions.remove(removedExplosion);
            removedExplosion = null;
        }
    }

    private void enemySpawner(){
        if(Globals.STAGE_KEY == Globals.GAME_STAGE){
            if(!spawnTimer.isRunning()){
                spawnedEnemy = new Enemy(new Vector2(Globals.VIEWPORT_WIDTH + PUSH_ENEMY_OUT_FROM_SCREEN, player.getPosition().y), 3, 1200, 6);
                enemies.add(spawnedEnemy);
                spawnTimer.start();
            }
            else if(spawnTimer.isPassed(800) && !secondSpawn){
                if(Globals.VIEWPORT_HEIGHT - (spawnedEnemy.getPosition().y + spawnedEnemy.getSprite().getHeight()) > spawnedEnemy.getPosition().y){
                    float tempY = (Globals.VIEWPORT_HEIGHT - (spawnedEnemy.getPosition().y + spawnedEnemy.getSprite().getHeight())) / 2;
                    secondSpawnedEnemy = new Enemy(new Vector2(Globals.VIEWPORT_WIDTH + PUSH_ENEMY_OUT_FROM_SCREEN, tempY), 3, 1200, 6);
                    enemies.add(secondSpawnedEnemy);
                }else{
                    float tempY = spawnedEnemy.getPosition().y / 2;
                    secondSpawnedEnemy = new Enemy(new Vector2(Globals.VIEWPORT_WIDTH + PUSH_ENEMY_OUT_FROM_SCREEN, tempY), 3, 1200, 6);
                    enemies.add(secondSpawnedEnemy);
                }
                secondSpawn = true;
            }
            else if(spawnTimer.isPassed(1200)){
                secondSpawn = false;
                spawnTimer.pause();
            }
        }
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void hide() { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void dispose() {	}

}

package resources;

import java.util.ArrayList;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import objects.Bullet;


public class Globals {

    public static int score = 0;
    public static int highScore = 0;

    public static boolean MUSIC_ON = true;
    public static boolean SOUND_ON = true;

    public static int STAGE_KEY = 0;
    public static int TEMP_STAGE_KEY = 0;

    //game stages for stage machine
    public static final int MAIN_STAGE = 0;
    public static final int OPTION_STAGE = 1;
    public static final int PREGAME_STAGE = 2;
    public static final int GAME_STAGE = 3;
    public static final int DEATH_STAGE = 4;
    public static final int POSTGAME_STAGE = 5;

    public static final int VIEWPORT_WIDTH = 600;
    public static final int VIEWPORT_HEIGHT = 480;

    public static ActionResolver ar = null; //is set at androidLauncher

    public static Texture playerBullet = new Texture(Gdx.files.internal("images/bullet.png"));
    public static Texture enemyBullet = new Texture(Gdx.files.internal("images/enemyBullet.png"));
    public static TextureAtlas explosionAtlas = new TextureAtlas(Gdx.files.internal("atlases/newEx.pack"));
    public static TextureAtlas playerAtlas = new TextureAtlas(Gdx.files.internal("atlases/playerAtlas.pack"));
    public static TextureAtlas enemyAtlas = new TextureAtlas(Gdx.files.internal("atlases/enemy.pack"));
    public static TextureAtlas instructionAtlas = new TextureAtlas(Gdx.files.internal("atlases/instructions.pack"));

    public static ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>();

    public static Sound explosion = Gdx.audio.newSound(Gdx.files.internal("audio/explosion.mp3"));

    private static Bullet removeBullet;

    public static void update(SpriteBatch batch){
        for(Bullet b : enemyBullets){

            if(!b.distanceToLive()){
                removeBullet = b;
            }else{
                b.update(batch);
            }

        }

        if(removeBullet != null){
            enemyBullets.remove(removeBullet);
            removeBullet = null;
            System.out.println("asd");
        }
    }

    public static void removeBullet(Bullet b){
        enemyBullets.remove(b);
    }

}

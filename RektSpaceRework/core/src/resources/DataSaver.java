package resources;

/**
 * Created by Jarbsku on 11.2.2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class DataSaver {

    private static final String SAVE_FILE = "userData";
    private Preferences prefs;

    public DataSaver(){
        prefs = Gdx.app.getPreferences(SAVE_FILE);
    }

    public void onLoad(){

        if(prefs.contains("music") && prefs.contains("sound")){
            Globals.MUSIC_ON = prefs.getBoolean("music");
            Globals.SOUND_ON = prefs.getBoolean("sound");
        }
        else{
            prefs.putBoolean("music", Globals.MUSIC_ON);
            prefs.putBoolean("sound", Globals.SOUND_ON);
        }

        Globals.highScore = prefs.getInteger("highScore");
    }

    public void saveHighScore(){
        prefs.putInteger("highScore", Globals.highScore);
        prefs.flush();
    }

    public void saveSettings(){
        prefs.putBoolean("music", Globals.MUSIC_ON);
        prefs.putBoolean("sound", Globals.SOUND_ON);
        prefs.flush();
    }

}

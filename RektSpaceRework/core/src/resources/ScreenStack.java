package resources;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import screens.SplashScreen;

/**
 * Created by Jarbsku on 11.2.2015.
 */
public class ScreenStack {

    private static Game game;

    /*Initializes game and starts the splash screen*/
    public static void init(Game gaem){
        game = gaem;
        game.setScreen(new SplashScreen());
    }

    public static void setScreen(Screen screen){
        game.setScreen(screen);
    }
}

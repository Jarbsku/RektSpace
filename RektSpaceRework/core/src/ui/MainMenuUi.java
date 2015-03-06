package ui;

/**
 * Created by Jarbsku on 11.2.2015.
 */

import resources.Globals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MainMenuUi {

    //static variables
    private static final int VIEWPORT_WIDTH = 1200;
    private static final int VIEWPORT_HEIGHT = 720;

    private static final int BUTTON_WIDTH = 128;
    private static final int BUTTON_HEIGHT = 128;

    private static final int LOGO_WIDTH = 480;
    private static final int LOGO_HEIGHT = 250;

    private static final float PADDING = 100;

    private static final float BIG_PADDING = 308;

    private static final float X_MOVEMENT = 308;

    //resources for scene drawing
    private StretchViewport viewport;
    private Stage stage;
    private Table mainMenuArea, logoArea, rootTable, settingsArea;
    private OrthographicCamera camera;

    //resources for buttons
    private TextureAtlas buttonAtlas;
    private Button play, exit, settings;
    private Skin playSkin, exitSkin, settingsSkin;

    private Image logo;
    private Texture logoImage;

    private MoveToAction moveOffScreen, moveOnScreen;

    public MainMenuUi(){
        init();
    }

    public void render() {
        if(Gdx.input.getInputProcessor() != stage){
            Gdx.input.setInputProcessor(stage);
        }

        stage.act();
        //rootTable.drawDebug(stage);
        stage.draw();

    }

    private void init() {

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new StretchViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        stage = new Stage(viewport);
        stage.getViewport().setCamera(camera);

        rootTable = new Table();
        rootTable.setFillParent(true);

        logoArea = new Table();
        mainMenuArea = new Table();
        mainMenuArea.setX(308);
        mainMenuArea.setY(120);

        settingsArea = new Table();
        settingsArea.setX(1100);
        settingsArea.setY(120);

        initMenuActors();

        stage.addActor(rootTable);

        moveOffScreen = new MoveToAction();
        moveOffScreen.setPosition(-2000f, 0f);
        moveOffScreen.setDuration(1f);

        moveOnScreen = new MoveToAction();
    }


    private void initMenuActors(){

        //initialize logo
        logoImage = new Texture(Gdx.files.internal("images/logo.png"));
        logo = new Image(logoImage);

        //initialize button actors and their resources
        buttonAtlas = new TextureAtlas(Gdx.files.internal("atlases/lightButtons.pack"));

        playSkin = new Skin(Gdx.files.internal("jsons/playButton.json"), buttonAtlas);
        settingsSkin = new Skin(Gdx.files.internal("jsons/settingsButton.json"), buttonAtlas);
        exitSkin = new Skin(Gdx.files.internal("jsons/exitButton.json"), buttonAtlas);

        play = new Button(playSkin);
        settings = new Button(settingsSkin);
        exit = new Button(exitSkin);

        //add listeners to buttons
        addListeners();

        //add actors to right areas
        logoArea.add(logo).width(LOGO_WIDTH).height(LOGO_HEIGHT);

        mainMenuArea.add(play).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
        mainMenuArea.add(settings).padLeft(PADDING).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
        mainMenuArea.add(exit).padLeft(PADDING).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);

        //add areas to rootTable
        rootTable.add(logoArea).padBottom(PADDING);
        rootTable.row();
        rootTable.add(mainMenuArea);

    }

    private void addListeners(){
        play.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                Globals.STAGE_KEY = Globals.PREGAME_STAGE;
                Gdx.input.setInputProcessor(null);
            }
        });

        settings.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                Globals.TEMP_STAGE_KEY = Globals.STAGE_KEY;
                Globals.STAGE_KEY = Globals.OPTION_STAGE;
            }
        });

        exit.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                System.exit(0);
            }
        });
    }

    public void dispose(){

    }
}

package ui;

/**
 * Created by Jarbsku on 11.2.2015.
 */

import resources.DataSaver;
import resources.Globals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class OptionsUi {

    //static variables
    private static final int VIEWPORT_WIDTH = 1200;
    private static final int VIEWPORT_HEIGHT = 720;

    private static final int BUTTON_WIDTH = 128;
    private static final int BUTTON_HEIGHT = 128;

    private static final int LOGO_WIDTH = 480;
    private static final int LOGO_HEIGHT = 250;

    private static final float PADDING = 100;

    //resources for scene drawing
    private StretchViewport viewport;
    private Stage stage;
    private Table uiArea, logoArea, rootTable;
    private OrthographicCamera camera;

    //resources for buttons
    private TextureAtlas buttonAtlas;
    private Button back, music, sound;
    private Skin musicSkin, soundSkin, backSkin;
    private Image logo;
    private Texture logoImage;

    //misc
    private DataSaver dataSaver;

    public OptionsUi(DataSaver dataSaver){
        this.dataSaver = dataSaver;
        init();
    }

    public void render() {
        if(Gdx.input.getInputProcessor() != stage){
            Gdx.input.setInputProcessor(stage);
            stage.addAction(Actions.sequence(Actions.fadeIn(.3f)));
        }

        stage.act();
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

        uiArea = new Table();
        logoArea = new Table();

        initMenuActors();

        stage.addActor(rootTable);
    }


    private void initMenuActors(){

        //initialize logo
        logoImage = new Texture(Gdx.files.internal("images/logo.png"));
        logo = new Image(logoImage);

        //initialize button actors and their resources
        buttonAtlas = new TextureAtlas(Gdx.files.internal("atlases/lightButtons.pack"));

        musicSkin = new Skin(Gdx.files.internal("jsons/musicButton.json"), buttonAtlas);
        soundSkin = new Skin(Gdx.files.internal("jsons/soundButton.json"), buttonAtlas);
        backSkin = new Skin(Gdx.files.internal("jsons/backButton.json"), buttonAtlas);

        music = new Button(musicSkin);
        sound = new Button(soundSkin);
        back = new Button(backSkin);

        //add listeners to buttons
        addListeners();

        //add actors to right areas
        logoArea.add(logo).width(LOGO_WIDTH).height(LOGO_HEIGHT);

        uiArea.add(music).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
        uiArea.add(sound).padLeft(PADDING).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
        uiArea.add(back).padLeft(PADDING).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);

        //add areas to rootTable
        rootTable.add(logoArea).padBottom(PADDING);
        rootTable.row();
        rootTable.add(uiArea);

    }

    private void addListeners(){
        music.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                if(!Globals.MUSIC_ON){
                    Globals.MUSIC_ON = true;
                }else{
                    Globals.MUSIC_ON = false;
                }
            }
        });

        sound.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                if(!Globals.SOUND_ON){
                    Globals.SOUND_ON = true;
                }else{
                    Globals.SOUND_ON = false;
                }

            }
        });

        back.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                dataSaver.saveSettings();
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                if(Globals.TEMP_STAGE_KEY == Globals.MAIN_STAGE){
                    Globals.STAGE_KEY = Globals.MAIN_STAGE;
                }else{
                    Globals.STAGE_KEY = Globals.DEATH_STAGE;
                }
            }
        });

        if(!Globals.MUSIC_ON){
            music.setChecked(true);
        }else{
            music.setChecked(false);
        }

        if(!Globals.SOUND_ON){
            sound.setChecked(true);
        }else{
            sound.setChecked(false);
        }

    }

    public void dispose(){

    }
}

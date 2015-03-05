package ui;

/**
 * Created by Jarbsku on 11.2.2015.
 */

import resources.Globals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class ScoreUi {

    //static variables
    private static final int VIEWPORT_WIDTH = 1200;
    private static final int VIEWPORT_HEIGHT = 720;

    private static final int BUTTON_WIDTH = 128;
    private static final int BUTTON_HEIGHT = 128;

    private static final int SCOREBOARD_WIDTH = 600;
    private static final int SCOREBOARD_HEIGHT = 250;

    private static final float PADDING = 100;

    //resources for scene drawing
    private StretchViewport viewport;
    private Stage stage;
    private Table uiArea, scoreArea, rootTable;
    private OrthographicCamera camera;

    //resources for buttons
    private TextureAtlas buttonAtlas;
    private Button retry, exit, settings;
    private Skin retrySkin, exitSkin, settingsSkin, scoreBoardSkin;

    //resources for displaying score
    private Label scoreBoard;
    private TextureAtlas scoreAtlas;

    public ScoreUi(){
        init();
    }

    public void render() {
        if(Gdx.input.getInputProcessor() != stage){
            Gdx.input.setInputProcessor(stage);
        }

        scoreBoard.setText("Score: "+Globals.score+"\n\nBest:  "+Globals.highScore);
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
        scoreArea = new Table();

        initMenuActors();

        stage.addActor(rootTable);
    }


    private void initMenuActors(){


        //initialize button actors and their resources
        buttonAtlas = new TextureAtlas(Gdx.files.internal("atlases/lightButtons.pack"));
        scoreAtlas = new TextureAtlas(Gdx.files.internal("atlases/scoreBoard.pack"));

        retrySkin = new Skin(Gdx.files.internal("jsons/retryButton.json"), buttonAtlas);
        settingsSkin = new Skin(Gdx.files.internal("jsons/settingsButton.json"), buttonAtlas);
        exitSkin = new Skin(Gdx.files.internal("jsons/exitButton.json"), buttonAtlas);
        scoreBoardSkin = new Skin(Gdx.files.internal("jsons/scoreBoard.json"), scoreAtlas);


        retry = new Button(retrySkin);
        settings = new Button(settingsSkin);
        exit = new Button(exitSkin);
        scoreBoard = new Label("", scoreBoardSkin);

        //add listeners to buttons
        addListeners();

        uiArea.add(retry).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
        uiArea.add(settings).padLeft(PADDING).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
        uiArea.add(exit).padLeft(PADDING).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);

        scoreArea.add(scoreBoard).width(SCOREBOARD_WIDTH).height(SCOREBOARD_HEIGHT);

        //add areas to rootTable
        rootTable.add(scoreArea).padBottom(PADDING);
        rootTable.row();
        rootTable.add(uiArea);

    }

    private void addListeners(){
        retry.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                Globals.score = 0;
                Globals.enemyBullets.clear();
                Globals.STAGE_KEY = Globals.PREGAME_STAGE;
                Gdx.input.setInputProcessor(null);
            }
        });

        settings.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                Globals.STAGE_KEY_BUFFER = Globals.STAGE_KEY;
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
}

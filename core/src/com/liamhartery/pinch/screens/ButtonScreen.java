package com.liamhartery.pinch.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.liamhartery.pinch.PinchGame;

public class ButtonScreen extends Stage implements Screen {

    private Stage stage;
    private TextButton back,instructionsButton,levelButton,quitButton,optionsButton;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private PinchGame game;
    private OrthographicCamera camera;
    private float screenWidth,screenHeight;
    private Table table;
    private int padAmount,cellWidth;
    public ButtonScreen(PinchGame pinchGame){
        padAmount = 50;
        cellWidth = 500;
        game = pinchGame;
        table = new Table();
        //table.debug();

        // camera stuff
        screenWidth = 800;
        screenHeight = 480;
        camera = new OrthographicCamera();
        camera.setToOrtho(false,screenWidth,screenHeight);

        // stage setup
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);


        // fonts
        font = game.font;
        font.getData().setScale(2,2);

        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas"));
        skin.addRegions(buttonAtlas);

        // Text Button Style
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("fixed_purple_button");
        textButtonStyle.down = skin.getDrawable("fixed_purple_button_down");
        //textButtonStyle.checked = skin.getDrawable("fixed_purple_button_down"); // this make the button toggle

        back = new TextButton("Back",textButtonStyle);
        instructionsButton = new TextButton("Instructions",textButtonStyle);
        levelButton = new TextButton("Level Select",textButtonStyle);
        quitButton = new TextButton("Quit",textButtonStyle);
        optionsButton = new TextButton("Options",textButtonStyle);

        table.add(instructionsButton).width(cellWidth).expandX().padBottom(padAmount);
        table.row();
        table.add(levelButton).width(cellWidth).expandX().padBottom(padAmount);
        table.row();
        table.add(optionsButton).width(cellWidth).expandX().padBottom(padAmount);
        table.row();
        table.add(back).width(cellWidth).expandX().padBottom(padAmount);
        table.row();
        table.add(quitButton).width(cellWidth).expandX().padBottom(padAmount);


        table.setX(screenWidth/2+cellWidth/2);
        table.setY(screenHeight/2+100);
        stage.addActor(table);

        /*
         * Let's do some input processors down here for the buttons
         */
        instructionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new InstructionsScreen(game));
                game.font.getData().setScale(0.5f);
                dispose();
            }
        });
        levelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new LevelSelectScreen(game));
                //game.font.getData().setScale(0.5f);
                dispose();
            }
        });
        optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                // do nothing for now
            }
        });
        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dispose();
                Gdx.app.exit();
            }
        });
        back.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MainMenuScreen(game));
                game.font.getData().setScale(0.5f);
                dispose();
            }
        });
    }

    @Override
    public void dispose(){
        super.dispose();
        buttonAtlas.dispose();
        skin.dispose();
        stage.dispose();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(33/255f,30/255f,39/255f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update the camera and look at stuff using the camera
        camera.update();

        stage.draw();
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            game.setScreen(new MainMenuScreen(game));
            game.font.getData().setScale(0.5f);
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}

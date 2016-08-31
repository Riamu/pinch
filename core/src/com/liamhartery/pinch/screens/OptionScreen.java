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

public class OptionScreen extends Stage implements Screen {
    private PinchGame game;
    private Stage stage;
    private TextButton musicButton,effectsButton,back;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private Skin skin;
    TextureAtlas buttonAtlas;
    private OrthographicCamera camera;
    private float screenWidth,screenHeight;
    private Table table;
    private int padAmount,cellWidth;

    public OptionScreen(PinchGame pinchGame){
        game = pinchGame;
        padAmount = 50;
        cellWidth = 500;
        screenWidth = 800;
        screenHeight = 480;
        game.adsController.showBannerAd();

        table = new Table();
        //table.debug();

        // camera stuff
        camera = new OrthographicCamera();
        camera.setToOrtho(false,screenWidth,screenHeight);

        // stage setup
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // fonts
        font = game.font;

        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas"));
        skin.addRegions(buttonAtlas);

        // Text Button Style
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("fixed_purple_button");
        textButtonStyle.down = skin.getDrawable("fixed_purple_button_down");
        // this make the button toggleable
        textButtonStyle.checked = skin.getDrawable("fixed_purple_button_down");

        musicButton = new TextButton("Music",textButtonStyle);
        effectsButton = new TextButton("Sound Effects",textButtonStyle);
        back = new TextButton("Back",textButtonStyle);

        table.add(musicButton).width(cellWidth).expandX().padBottom(padAmount);
        table.row();
        table.add(effectsButton).width(cellWidth).expandX().padBottom(padAmount);
        table.row();
        table.add(back).width(cellWidth).expandX().padBottom(padAmount);

        stage.addActor(table);
        table.setFillParent(true);
        table.center();

        // check toggle states on our buttons
        if(!game.music){
            musicButton.setChecked(true);
        }else{
            musicButton.setChecked(false);
        }
        if(!game.soundEffects){
            effectsButton.setChecked(true);
        }else{
            effectsButton.setChecked(false);
        }
        musicButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(game.music){
                    game.music = false;
                    game.musicFile.stop();
                }else{
                    game.music = true;
                }
            }
        });
        effectsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(game.soundEffects){
                    game.soundEffects = false;
                }else{
                    game.soundEffects = true;
                }
            }
        });
        back.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new ButtonScreen(game));
                dispose();
            }
        });
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
            game.setScreen(new ButtonScreen(game));
            game.font.getData().setScale(0.5f);
            dispose();
        }
    }
    @Override
    public void dispose(){
        super.dispose();
        buttonAtlas.dispose();
        skin.dispose();
        stage.dispose();
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

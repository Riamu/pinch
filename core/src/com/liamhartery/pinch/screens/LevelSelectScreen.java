package com.liamhartery.pinch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.liamhartery.pinch.PinchGame;

public class LevelSelectScreen extends Stage implements Screen {
    private Stage stage;
    private TextButton tutorial,back,level1,level2,level3;
    private TextButton.TextButtonStyle textButtonStyle;
    private BitmapFont font;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private PinchGame game;
    private OrthographicCamera camera;
    private float screenWidth,screenHeight;
    private Table table;
    private int padAmount,cellWidth;

    public LevelSelectScreen(PinchGame pinchGame){
        padAmount = 50;
        cellWidth = 500;
        screenWidth = 800;
        screenHeight = 480;
        game = pinchGame;
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

        // skins
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas"));
        skin.addRegions(buttonAtlas);

        // Text Button Style
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("fixed_purple_button");
        textButtonStyle.down = skin.getDrawable("fixed_purple_button_down");
        //textButtonStyle.checked = skin.getDrawable("fixed_purple_button_down"); // this make the button toggle

        tutorial = new TextButton("Tutorial",textButtonStyle);
        back = new TextButton("Back",textButtonStyle);
        level1 = new TextButton("Level 1",textButtonStyle);
        level2 = new TextButton("Level 2",textButtonStyle);
        level3 = new TextButton("Level 3",textButtonStyle);

        table.add(tutorial).width(cellWidth).expandX().padBottom(padAmount);
        table.row();
        table.add(level1).width(cellWidth).expandX().padBottom(padAmount);
        table.row();
        table.add(level2).width(cellWidth).expandX().padBottom(padAmount);
        table.row();
        table.add(level3).width(cellWidth).expandX().padBottom(padAmount);
        table.row();
        table.add(back).width(cellWidth).expandX().padBottom(padAmount);

        table.setX(0);
        table.setY(0);
        stage.addActor(table);
        table.setFillParent(true);
        table.center();

        /*
         * Let's do some input processors down here for the buttons
         */
        tutorial.addListener(new ClickListener(){
           @Override
            public void clicked(InputEvent event, float x, float y){
               game.setScreen(new GameScreen(game,0,1));
               game.font.getData().setScale(0.5f);
               dispose();
           }
        });
        back.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                game.setScreen(new ButtonScreen(game));
                dispose();
            }
        });
        level1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new GameScreen(game,1,1));
                game.font.getData().setScale(0.5f);
                dispose();
            }
        });
        level2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                new Dialog("Level 2",new Skin(Gdx.files.internal("ui/experimental/uiskin.json"))){
                    {
                        if(!isLevelAvailable(2)){
                            text("Sorry, level 2 is locked right now. Beat level 1 first")
                                    .setScale(Gdx.graphics.getWidth()/500);
                            button("OK").setScale(Gdx.graphics.getWidth()/500);
                        }else{
                            game.setScreen(new GameScreen(game,2,1));
                            game.font.getData().setScale(0.5f);
                            dispose();
                        }
                    }
                    @Override
                    public void result(Object object){

                    }
                }.show(stage);
            }
        });
        level3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x,float y){
                new Dialog("Level 3",new Skin(Gdx.files.internal("ui/experimental/uiskin.json"))){
                    {
                        if(!isLevelAvailable(3)){
                            text("Sorry, level 3 is locked right now. Beat level 2 first")
                                    .setScale(Gdx.graphics.getWidth()/500);
                            button("OK").setScale(Gdx.graphics.getWidth()/500);
                        }else{
                            game.setScreen(new GameScreen(game,3,1));
                            game.font.getData().setScale(0.5f);
                            dispose();
                        }
                    }
                    @Override
                    public void result(Object object){

                    }
                }.show(stage);
            }
        });
        /*
        level2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new GameScreen(game,2,1));
                game.font.getData().setScale(0.5f);
                dispose();
            }
        });
        level3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new GameScreen(game,3,1));
                game.font.getData().setScale(0.5f);
                dispose();
            }
        });
        */
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

        stage.act();
        stage.draw();
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            game.setScreen(new ButtonScreen(game));
            dispose();
        }
    }

    public boolean isLevelAvailable(int level){
        FileHandle file = Gdx.files.local("pinchSave");
        if(game.isLocAvailable&&file.readString().charAt(level*4-2)=='f'){
            return false;
        }else{
            return true;
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

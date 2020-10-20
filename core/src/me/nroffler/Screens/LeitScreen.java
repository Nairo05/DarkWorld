package me.nroffler.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import me.nroffler.main.DarkWorld;
import me.nroffler.main.Statics;

public class LeitScreen implements Screen {

    private DarkWorld game;

    private int todesgrund;

    public LeitScreen(DarkWorld darkWorld, int todesgrund) {
        this.game = darkWorld;
        this.todesgrund = todesgrund;
    }

    @Override
    public void show() {
        System.out.println("navigate: MainMenu");
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isTouched() || Statics.level == 0 || game.assetManager.isFinished()){
            if (Statics.ersterStart){
                Statics.ersterStart = false;
                game.setScreen(new LogoScreen(game, todesgrund));
            } else {
                game.setScreen(new LadeScreen(game, todesgrund));
            }
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

    @Override
    public void dispose() {

    }
}

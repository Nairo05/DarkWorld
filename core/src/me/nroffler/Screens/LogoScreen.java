package me.nroffler.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.nroffler.main.DarkWorld;
import me.nroffler.main.Statics;

public class LogoScreen implements Screen {

    private OrthographicCamera gamecam;
    private Viewport gameport;

    private DarkWorld darkWorld;

    private int progress;

    private int todesgrund;

    private Texture texture;
    private Texture texturen;
    private Texture textureintro;

    private Sprite sprite, spriten, spriteIntro;

    LogoScreen(DarkWorld darkWorld, int todesgrund){
        this.darkWorld = darkWorld;
        this.todesgrund = todesgrund;

        gamecam = new OrthographicCamera();
        gameport = new FitViewport(Statics.V_BREITE, Statics.V_HOEHE, gamecam);

        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() /2, 0);

        //Aus den Texturen müssen Sprites gemacht werden, denn nur bei sprites kann man den Alpha-wert setzen
        texture = new Texture("splashs/gdx.png");
        sprite = new Sprite(texture);

        texturen = new Texture("splashs/nroffler.png");
        spriten = new Sprite(texturen);

        textureintro = new Texture("splashs/intro.png");
        spriteIntro = new Sprite(textureintro, 0,0,Statics.V_BREITE,Statics.V_HOEHE);
        spriteIntro.setAlpha(0.0f);

    }

    @Override
    public void show() {
        System.out.println("navigate: Loading");
    }


    private void update(){
        progress += 1;

        gamecam.update();


        if (progress >= 680 ||  Statics.debug) {
            darkWorld.batch.disableBlending();
            if (!Statics.dasSpielIstWirklichVorbei) {
                darkWorld.setScreen(new LadeScreen(darkWorld, todesgrund));
            }
        }
    }

    @Override
    public void render(float delta) {
        update();

        //Alles schwarz übermalen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        darkWorld.batch.enableBlending();

        //ich wünschte das ginge einfacher, ist aber die einzige möglichkeit

        if (progress == 89){
            sprite.setAlpha(0.9f);
        } else if (progress == 92){
            System.out.println("alpha update");
            sprite.setAlpha(0.8f);
        } else if (progress == 95){
            sprite.setAlpha(0.7f);
        } else if (progress == 98){
            sprite.setAlpha(0.6f);
        } else if (progress == 101){
            sprite.setAlpha(0.5f);
        } else if (progress == 103){
            sprite.setAlpha(0.4f);
        } else if (progress == 106){
            sprite.setAlpha(0.3f);
        } else if (progress == 109){
            sprite.setAlpha(0.2f);
        } else if (progress == 112){
            sprite.setAlpha(0.1f);
        } else if (progress == 115){
            sprite.setAlpha(0.0f);


        } else if (progress == 209){
            spriten.setAlpha(0.9f);
        } else if (progress == 212){
            spriten.setAlpha(0.8f);
        } else if (progress == 215){
            spriten.setAlpha(0.7f);
        } else if (progress == 218){
            spriten.setAlpha(0.6f);
        } else if (progress == 221){
            spriten.setAlpha(0.5f);
        } else if (progress == 223){
            spriten.setAlpha(0.4f);
        } else if (progress == 226){
            spriten.setAlpha(0.3f);
        } else if (progress == 229){
            spriten.setAlpha(0.2f);
        } else if (progress == 232){
            spriten.setAlpha(0.1f);
        } else if (progress == 236){
            spriten.setAlpha(0.0f);

            if (Statics.dasSpielIstWirklichVorbei){
                Gdx.app.exit();
            }

        } else if (progress == 250){
            spriteIntro.setAlpha(0.0f);
        } else if (progress == 253){
            spriteIntro.setAlpha(0.1f);
        } else if (progress == 256){
            spriteIntro.setAlpha(0.2f);
        } else if (progress == 259){
            spriteIntro.setAlpha(0.3f);
        } else if (progress == 262){
            spriteIntro.setAlpha(0.4f);
        } else if (progress == 265){
            spriteIntro.setAlpha(0.5f);
        } else if (progress == 268){
            spriteIntro.setAlpha(0.6f);
        } else if (progress == 271){
            spriteIntro.setAlpha(0.7f);
        } else if (progress == 274){
            spriteIntro.setAlpha(0.8f);
        } else if (progress == 277){
            spriteIntro.setAlpha(0.9f);
        } else if (progress == 280){
            spriteIntro.setAlpha(1f);
        }

        if (Statics.dasSpielIstWirklichVorbei && progress == 800){
            Gdx.app.exit();
        }

        //Textur Renderer
        darkWorld.batch.setProjectionMatrix(gamecam.combined);
        darkWorld.batch.begin();

        if (progress > 240){
            spriteIntro.draw(darkWorld.batch);
        } else if (progress > 120) {
            spriten.draw(darkWorld.batch);
        } else {
            sprite.draw(darkWorld.batch);
        }

        darkWorld.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width,height);
        gamecam.update();
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
        texture.dispose();
        texturen.dispose();
        textureintro.dispose();
    }
}

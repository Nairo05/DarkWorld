package me.nroffler.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.nroffler.main.DarkWorld;
import me.nroffler.main.Statics;

import static me.nroffler.main.Statics.ueberschreiben;

public class LadeScreen implements Screen {

    private OrthographicCamera spielKamera;
    private Viewport spielPort;

    private ShapeRenderer shapeRenderer;
    private DarkWorld darkWorld;

    private float fortschritt;
    private int todesgeund;

    private Texture textur;

    LadeScreen(DarkWorld darkWorld, int todesgrund){
        this.darkWorld = darkWorld;
        this.todesgeund = todesgrund;

        spielKamera = new OrthographicCamera();
        spielPort = new FitViewport(Statics.V_BREITE, Statics.V_HOEHE, spielKamera);

        spielKamera.position.set(spielPort.getWorldWidth() / 2, spielPort.getWorldHeight() /2, 0);

        fortschritt = 0.0f;
        shapeRenderer = new ShapeRenderer();

        if (Statics.ueberschreiben){
            speichern();
            ueberschreiben = false;
        } else {
            Preferences mypref = Gdx.app.getPreferences("DarkWorld");
            Statics.level = mypref.getInteger("level");
        }

    }

    @Override
    public void show() {
        System.out.println("navigate: Loading");
        ladeAssets();
    }


    private void update(float delta){

        spielKamera.update();

        if (Statics.dasSpielIstWirklichVorbei){
            fortschritt += 0.05f * delta;
        } else {
            fortschritt += 0.4f * delta;
        }
        //Wenn es fertig geladen wurde und die Balken animation durch ist
        if (darkWorld.assetManager.isFinished() && fortschritt >= 1f || (darkWorld.assetManager.isFinished() && Statics.debug)){
            System.out.println("level+" + Statics.level);
            if (!Statics.dasSpielIstWirklichVorbei) {
                darkWorld.setScreen(new SpielScreen(darkWorld));
            } else if (Statics.dasSpielIstWirklichVorbei){
                System.out.println("spiel zu ende");
                darkWorld.setScreen(new LogoScreen(darkWorld, -1));
            }
        }
    }

    private void speichern(){
        Preferences mypref = Gdx.app.getPreferences("DarkWorld");
        mypref.putString("DarkWorld", "localdata");
        mypref.putInteger("level", Statics.level);
        mypref.flush();
    }

    private void ladeAssets(){
        //alte Assets löschen um Grafikspeicher frei zu geben
        darkWorld.assetManager.clear();

        //Assets vorladen, damit sie während des Spielens nicht geladen werden müssen, denn das verursacht vor allem auf Mobilgeräten kurze "hänger"
        //Sprites
        darkWorld.assetManager.load("Sprites/player01.png", Texture.class);
        darkWorld.assetManager.load("Sprites/cage.png", Texture.class);
        darkWorld.assetManager.load("Sprites/cagebroken.png", Texture.class);
        darkWorld.assetManager.load("Sprites/player_broken.png", Texture.class);
        darkWorld.assetManager.load("particles/prt1.png", Texture.class);
        darkWorld.assetManager.load("statics/pausebtn.png", Texture.class);
        darkWorld.assetManager.load("statics/playbtn.png", Texture.class);
        darkWorld.assetManager.load("Sprites/bird_animation.png", Texture.class);
        darkWorld.assetManager.load("Sprites/bird_animation_player.png", Texture.class);

        //Partikel Effekte
        darkWorld.assetManager.load("particles/brake.pe", ParticleEffect.class);
        darkWorld.assetManager.load("particles/fine.pe", ParticleEffect.class);

        //Splashs
        for (int i = 0; i <= 12 ; i++){
            darkWorld.assetManager.load("splashs/"+i+".png", Texture.class);
        }

        //SpriteSheet
        darkWorld.assetManager.load("level/tileset_15.png", Texture.class);

        darkWorld.assetManager.finishLoading();

        //Assets vorbereiten
        darkWorld.assetManager.update();
    }

    @Override
    public void render(float delta) {
        update(delta);

        //Alles in schwarz übermalen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Texture-renderer
        darkWorld.batch.setProjectionMatrix(spielKamera.combined);
        darkWorld.batch.begin();

        if (todesgeund == 4) {
            textur = new Texture("splashs/energy.png");
        } else if (todesgeund == 2) {
            textur = new Texture("splashs/stacheln.png");
        } else if (todesgeund == 1){
            textur = new Texture("splashs/unten.png");
        } else if (todesgeund == 5){
            textur = new Texture("splashs/12-2.png");
        } else {
            textur = new Texture("splashs/next.png");
        }

        if (Statics.level > 0) {
            darkWorld.batch.draw(textur, 0, 0, Statics.V_BREITE, Statics.V_HOEHE);
        }

        darkWorld.batch.end();

        //Ladebalken zeichnen mit Shape-renderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(10,10 ,Gdx.graphics.getWidth()/2, 10);

        if (fortschritt <= 1) {
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(10, 10, Gdx.graphics.getWidth() / 2f * fortschritt, 10);
        } else {
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(10, 10, Gdx.graphics.getWidth() / 2f, 10);
        }

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        spielPort.update(width,height);
        spielKamera.update();
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
        shapeRenderer.dispose();
        textur.dispose();
    }
}

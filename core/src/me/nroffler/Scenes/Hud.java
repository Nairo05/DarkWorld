package me.nroffler.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import me.nroffler.Screens.SpielScreen;
import me.nroffler.main.Statics;

public class Hud implements Disposable {

    private float bildBreite = 15f;

    private SpielScreen spielScreen;

    public Stage stage;
    private Image bild, pauseBild, spielBild;
    private Table tabelle;
    private Texture textur, pauseTextur, playTextur;

    public Hud(final SpielScreen spielScreen){
        this.spielScreen = spielScreen;

        Viewport hudport = new FitViewport(Statics.BREITE, Statics.HOEHE, new OrthographicCamera());

        stage = new Stage(hudport, spielScreen.getBatch());

        //normal table
        tabelle = new Table();
        tabelle.top();
        tabelle.setFillParent(true);

        textur = spielScreen.getAssetManager().get("particles/prt1.png", Texture.class);
        pauseTextur = spielScreen.getAssetManager().get("statics/pausebtn.png", Texture.class);
        playTextur = spielScreen.getAssetManager().get("statics/playbtn.png", Texture.class);

        bild = new Image(textur);
        bild.setScaleX(bildBreite);

        spielBild = new Image(playTextur);
        pauseBild = new Image(pauseTextur);

        tabelle.add(bild).padTop(15).padLeft(15f);
        tabelle.add(new Actor()).expandX();
        tabelle.add(spielBild).padTop(15).padRight(15f);

        //
        stage.addActor(tabelle);
    }

    public void setBildBreite(float bildBreite){
        this.bildBreite = bildBreite;
        bild.setScaleX(bildBreite);
    }

    public void update(float delta){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            //ändern des Pauseknopfes
            spielScreen.pausieren();
            if (!spielScreen.istPausiert()){
                tabelle.clear();
                tabelle.top();
                tabelle.setFillParent(true);
                tabelle.add(bild).padTop(15).padLeft(15f);
                tabelle.add(new Actor()).expandX();
                tabelle.add(spielBild).padTop(15).padRight(15f);
            } else {
                tabelle.clear();
                tabelle.top();
                tabelle.setFillParent(true);
                tabelle.add(new Actor()).expandX();
                tabelle.add(pauseBild).padTop(15).padRight(15f);
            }
        }
    }

    @Override
    public void dispose() {
        textur.dispose();
        stage.dispose();
    }
}

package me.nroffler.DynamischeObjekte;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import me.nroffler.Screens.SpielScreen;
import me.nroffler.main.Statics;

public abstract class DynamischesObjekt extends Sprite {

    protected SpielScreen spielScreen;
    protected World welt;
    protected Vector2 bewegung;

    protected boolean benutzt;

    protected boolean sollzerstoertwerden;
    protected boolean zerstoert;

    protected Body body;

    public DynamischesObjekt(SpielScreen spielScreen, float x, float y){
        this.spielScreen = spielScreen;
        this.welt = spielScreen.getWelt();

        setPosition(x,y);
        setBounds(getX(), getY(), 16/ Statics.PPM, 16/Statics.PPM);
        definiereBody();

        sollzerstoertwerden = false;
        zerstoert = false;
        benutzt = false;
    }

    public abstract void definiereBody();

    //Diese Methoden werden benötigt, um sie im WorldContactListener aufzurufen
    //Allgemein hat jedes Dynamische Objekt meherer Phasen, die dazu dienen die Items bei verschiedenen Interactionen (mit der Welt, dem Spieler, den Stacheln, ...)
    //verschiedene Dinge tun zu lassen
    public abstract void benutzen();
    public abstract void zerstoeren();
    public abstract void beenden();

    //Für zerbrech Animationen, etc.
    public abstract void drawEffects(SpriteBatch batch);

    public void update(float dt){
        //Objekt zertören
        if (sollzerstoertwerden && !zerstoert){
            welt.destroyBody(body);
            zerstoert = true;
        }
    }

    //Die draw Methode von Sprite wird hier überschrieben, da eine Änderung nötig war
    //das Objekt soll nur sichtbar sein / gezeichnet werden wenn es nicht zertört wurde
    public void draw(Batch batch){
        if (!zerstoert){
            super.draw(batch);
        }
    }

    //Getter und Setter
    public void entfernen(){
        sollzerstoertwerden = true;
    }
    public void setbenutzt(){benutzt = true;}
    public boolean istbenutzt(){return benutzt;}
}

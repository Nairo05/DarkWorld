package me.nroffler.tiledobjekts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import me.nroffler.Screens.SpielScreen;

public class ZielPartikel implements Disposable {

    private ParticleEffect particleEffect;
    private float sclfkt = 0.002f;

    private SpielScreen spielScreen;

    public ZielPartikel(float x, float y, SpielScreen spielScreen){
        this.spielScreen = spielScreen;

        particleEffect = new ParticleEffect();

        //XML Datei laden, LibGDX erkennt von selbst, das der Effekt bereits durch den AssetManager vorgeladen wurde
        particleEffect.load(Gdx.files.internal("particles/fine.pe"),Gdx.files.internal("particles/"));

        //Attribute festlegen
        particleEffect.setDuration(30000000);
        particleEffect.getEmitters().first().setPosition(x,y);
        particleEffect.scaleEffect(sclfkt);
        particleEffect.start();
    }

    public void sclne(){
        sclfkt -= 0.0001f;
    }

    public void update(float dt){
        //Partikeleffect updaten
        if (spielScreen.getSpieler().getX() > 35.3f) {
            particleEffect.update(dt);
        }
    }

    public void draw(SpriteBatch batch){
        //Partikeleffect zeichnen / dem batch Container hinzufügen
        if (spielScreen.getSpieler().getX() > 35.3f) {
            particleEffect.draw(batch);
        }
    }

    @Override
    public void dispose() {
        //Grafikspeicher freigeben
        particleEffect.dispose();
    }
}

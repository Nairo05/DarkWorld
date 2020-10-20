package me.nroffler.DynamischeObjekte;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Disposable;

import me.nroffler.Screens.SpielScreen;
import me.nroffler.main.Bit_Filter;
import me.nroffler.main.Statics;

public class Kaefig extends DynamischesObjekt implements Disposable {

    private SpielScreen spielScreen;
    private boolean beruehrt = false;
    private ParticleEffect particleEffect;

    public Kaefig(SpielScreen spielScreen, float x, float y) {
        super(spielScreen, x, y);
        //Die Textur des Objektes setzen, es wird das vorgeladene Bild (--> LoadingScreen.class) verwendet
        setTexture(spielScreen.getAssetManager().get("Sprites/cage.png", Texture.class));
        setPosition(0,0);
        setRegionWidth(16);
        setRegionHeight(16);

        bewegung = new Vector2(0,0);
        body.setGravityScale(0);
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("particles/brake.pe"),Gdx.files.internal("particles/"));

        this.spielScreen = spielScreen;
    }

    @Override
    public void definiereBody() {

        BodyDef bdef = new BodyDef(); //Body defineiren
        bdef.position.set(getX()+getWidth()/2, getY()+getHeight()/2); //Position setzen
        bdef.type = BodyDef.BodyType.DynamicBody; //Art des Körpers
        body = welt.createBody(bdef); //Körper erschaffen

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8/ Statics.PPM); //Hitbox in Form eines Kreises mit dem Radius 8

        fdef.shape = shape;
        fdef.filter.categoryBits = Bit_Filter.KAEFIG_BIT; //ID / Gruppe dieses Objektes
        fdef.filter.maskBits = Bit_Filter.SPIELER_BIT | Bit_Filter.NORMALER_OBJEKT_BIT | Bit_Filter.STACHEL_BIT; //damit kann das Objekt interagieren
        body.setFixedRotation(false);
        body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void benutzen() {
        particleEffect.setPosition(body.getPosition().x,body.getPosition().y);
        particleEffect.getEmitters().first().setPosition(body.getPosition().x,body.getPosition().y); //von hier aus gehen die Effekte
        particleEffect.scaleEffect(0.006f);
        particleEffect.start();

        body.setGravityScale(1);
        body.applyLinearImpulse(new Vector2(270/Statics.PPM, -4/Statics.PPM), body.getWorldCenter(), true);

        spielScreen.addEnergie(3);

        //Die Textur des Objektes ersetzen, es wird das vorgeladene Bild (--> LoadingScreen.class) verwendet
        setTexture(spielScreen.getAssetManager().get("Sprites/cagebroken.png", Texture.class));

        setbenutzt();
    }

    @Override
    public void zerstoeren(){
        beruehrt = true;
    }

    @Override
    public void beenden() {
        particleEffect.reset();

        particleEffect.setPosition(body.getPosition().x, body.getPosition().y);
        particleEffect.getEmitters().first().setPosition(body.getPosition().x,body.getPosition().y);
        particleEffect.scaleEffect(0.006f);
        particleEffect.start();

        if (beruehrt) {
            entfernen();
        }
    }

    @Override
    public void drawEffects(SpriteBatch batch) {
        particleEffect.draw(batch);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        particleEffect.update(dt);
        setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - getHeight() /2);
        if (getY() < -30){
            entfernen();
        }
    }



    @Override
    public void dispose() {
        //Grafikspeicher frei geben
        particleEffect.dispose();
    }
}

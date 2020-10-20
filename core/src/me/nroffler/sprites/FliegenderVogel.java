package me.nroffler.sprites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import me.nroffler.Screens.SpielScreen;
import me.nroffler.main.Statics;

public class FliegenderVogel extends Vogel {

    private int bild, reihe;
    long bildzaehler;
    private Vector2 linearVelocity;

    public FliegenderVogel(float x, float y, SpielScreen spielScreen){
        super(x, y, spielScreen);

        this.spielScreen = spielScreen;

        linearVelocity = new Vector2(-0.7f,0);

        reihe = 1;
    }

    @Override
    public void update(float dt){
        //Animation durchlaufen
        bildzaehler++;
        if ((bildzaehler %4)==0) {
            bild++;
            if (bild > 7) {
                bild = 0;
            }
        }

        //Textur des Vogels updaten
        sprite.setRegion(regions[reihe][bild]);

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        setRegion(sprite);

        //Vogel bewegen
        b2body.setLinearVelocity(linearVelocity);
    }

    @Override
    public void defineBird(SpielScreen spielScreen) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x,y);
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = spielScreen.getWelt().createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / Statics.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        b2body.setGravityScale(0f);
        b2body.setSleepingAllowed(true);
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void dispose() {

    }
}

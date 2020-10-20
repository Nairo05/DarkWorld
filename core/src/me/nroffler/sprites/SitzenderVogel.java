package me.nroffler.sprites;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import java.util.Random;

import me.nroffler.Screens.SpielScreen;
import me.nroffler.main.Statics;

public class SitzenderVogel extends Vogel {

    private int frame, row;
    private int framecount;
    private boolean flipped;
    private Random zufall;
    private int rV;

    public SitzenderVogel(float x, float y, SpielScreen spielScreen) {
        super(x, y, spielScreen);

        flipped = false;
        row = 0;

        zufall = new Random();
        rV = zufall.nextInt(20);

    }

    @Override
    public void update(float dt) {
        //Animation durchlaufen
        framecount++;
        if (framecount > (120 + rV)){

            row = 2;

            if ((framecount%4)==0) {
                frame++;
                if (frame > 2) {
                    frame = 0;
                    framecount = 0;

                }
            }
        } else {

            if (framecount == (60 + rV+5)){
                if (new Random().nextInt(2) == 1){
                    flipped = !flipped;
                }
            }

            row = 0;

            if ((framecount)==0) {
                frame++;
                if (frame > 1){
                    frame = 0;
                }
            }
        }

        sprite.setRegion(regions[row][frame]);

        //für abwechslung ab und zu Textur spiegeln
        if (flipped){
            sprite.flip(true, false);
        }

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y);
        b2body.setGravityScale(0);
        setRegion(sprite);
    }

    @Override
    public void defineBird(SpielScreen spielScreen) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x,y+getHeight()/2);
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

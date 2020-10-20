package me.nroffler.tiledobjekts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import me.nroffler.Screens.SpielScreen;
import me.nroffler.main.Bit_Filter;
import me.nroffler.main.Statics;

public class BewegenderBlock extends BewegendesTiledObjekt {

    private float stateTime;
    /*private Animation flyAnimation;
    private Array<TextureRegion> frames;*/
    private TextureRegion region;
    private Texture sheet;

    public BewegenderBlock(SpielScreen spielScreen, float x, float y) {
        super(spielScreen, x, y);
        sheet = spielScreen.getAssetManager().get("level/tileset_15.png", Texture.class);
        region = new TextureRegion(sheet, 48, 16, 16, 16);
        setBounds(getX(), getY(), 16 / Statics.PPM, 16 / Statics.PPM);
    }

    @Override
    public void update(float dt) {
        if (!getDestroyed()){
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight()/2);
            setRegion(region);

        }
    }

    public void draw(Batch batch){
        if (!getDestroyed()){
            super.draw(batch);
        }
    }

    @Override
    protected void defineTile() {
        //Hitbox erstellen
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() , getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        //CircleShape shape = new CircleShape();
        //shape.setRadius(7 / Statics.PPM);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(8 / Statics.PPM, 8 / Statics.PPM);

        //Dieses Objekt kann durch Bit_Filter.PPLAYER_BIT identifiziert werden
        fdef.filter.categoryBits = Bit_Filter.BEWEGENDER_BIT;

        //Dieses Objekt kann mit Bit_Filter.COLLIDE_OBJEKT_BIT | Bit_Filter.LIGHT_BIT | Bit_Filter.DEATH_BIT interagieren
        fdef.filter.maskBits = Bit_Filter.NORMALER_OBJEKT_BIT | Bit_Filter.LICHT_BIT | Bit_Filter.SPIELER_BIT;

        fdef.restitution = 1.005f;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }
}

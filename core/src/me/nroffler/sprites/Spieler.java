package me.nroffler.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import me.nroffler.Screens.SpielScreen;
import me.nroffler.main.Bit_Filter;
import me.nroffler.main.Statics;

public class Spieler extends Sprite implements Disposable {

    //Varibeln deklarieren
    private World world;
    public Body b2body;
    private SpielScreen spielScreen;
    private CircleShape shape;

    private int speed;

    private int frameplayer, row;
    private int framecountplayer;
    private boolean flipped;
    boolean standOnGround;

    private Texture textur;
    private Sprite spritep;
    private TextureRegion regionsp[][];

    public Spieler(SpielScreen spielScreen){

        textur = spielScreen.getAssetManager().get("Sprites/bird_animation_player.png", Texture.class);

        frameplayer = 0;
        row = 1;
        framecountplayer = 1;
        flipped = true;
        standOnGround = false;

        this.spielScreen = spielScreen;
        world = spielScreen.getWelt();
        shape = new CircleShape();

        regionsp = TextureRegion.split(textur, 16,16);
        spritep = new Sprite(regionsp[1][1]);
        setRegion(spritep);

        setBounds(0, 0, 16 / Statics.PPM, 16 / Statics.PPM);
        definierePlayer();

        speed = 4;
    }

    private void definierePlayer() {
        //Hitbox erstellen
        BodyDef bdef = new BodyDef();
        bdef.position.set(32f / Statics.PPM, 164 / Statics.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        shape.setRadius(8 / Statics.PPM);

        fdef.shape = shape;
        //Dieses Objekt kann durch Bit_Filter.PPLAYER_BIT identifiziert werden
        fdef.filter.categoryBits = Bit_Filter.SPIELER_BIT;

        //Dieses Objekt kann mit Bit_Filter.COLLIDE_OBJEKT_BIT | Bit_Filter.LIGHT_BIT | Bit_Filter.DEATH_BIT interagieren
        fdef.filter.maskBits = Bit_Filter.NORMALER_OBJEKT_BIT | Bit_Filter.LICHT_BIT | Bit_Filter.STACHEL_BIT | Bit_Filter.BEWEGENDER_BIT;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void update(float dt){
        //Animation des Spielers durchlaufen
        if (spielScreen.istImSpiel()) {
            if (b2body.getLinearVelocity().x > 0) {

                row = 1;
                framecountplayer++;

                if (!standOnGround) {
                    if ((framecountplayer % speed) == 0) {
                        frameplayer++;
                        if (frameplayer > 7) {
                            frameplayer = 1;
                        }
                    }
                }
            } else {
                row = 0;
                frameplayer = 0;
            }
        }

        //Textur des Spielers updaten
        spritep.setRegion(regionsp[row][frameplayer]);

        if (flipped && !spritep.isFlipX()){
            spritep.flip(true, false);
        }

        setRegion(spritep);

        //Die Position der Textur der Position der Hitbox (body) anpassen
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //Wenn am ende der Karte und nicht Tod:
        if (b2body.getPosition().x > 38.3f && spielScreen.istImSpiel()){
            //Spiel beenden
            spielScreen.setStatus(false, SpielScreen.Todesgrund.LEVEL_ERFOLGREICH);
        } else if (Statics.level == 12 && spielScreen.istImSpiel() && b2body.getPosition().x > 14.4f){
            spielScreen.setStatus(false, SpielScreen.Todesgrund.SPIEL_BEENDET);
        }
        if (!spielScreen.istImSpiel() && !(spielScreen.todesgrund == SpielScreen.Todesgrund.KEINE_ENERGIE)){
            setRegion(spritep);
        }
    }

    @Override
    public void dispose() {
        //Grafikspeicher frei geben
        world.dispose();
        shape.dispose();
    }

    //für später
    public void setSpeed(int speed) {
        if (speed > 5){
            speed = 5;
        }
        if (speed < 3){
            speed = 3;
        }
        this.speed = speed;
    }
}

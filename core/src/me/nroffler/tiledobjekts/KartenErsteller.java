package me.nroffler.tiledobjekts;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import me.nroffler.DynamischeObjekte.DynmischeDef;
import me.nroffler.DynamischeObjekte.Kaefig;
import me.nroffler.Screens.SpielScreen;
import me.nroffler.main.Bit_Filter;
import me.nroffler.main.Statics;

public class KartenErsteller implements Disposable {

    private SpielScreen spielScreen;

    private World welt;
    private Map karte;

    private Array<BewegendesTiledObjekt> bewegendeBloecke;

    public KartenErsteller(SpielScreen spielScreen){
        this.spielScreen = spielScreen;
        karte = spielScreen.getKarte();
        welt = spielScreen.getWelt();

        bewegendeBloecke = new Array<>();
    }

    public void erstelleKaefige(int layer){
        for (MapObject object : spielScreen.getKarte().getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            DynmischeDef dDef = new DynmischeDef(new Vector2(rect.getX()/ Statics.PPM, rect.getY()/Statics.PPM), Kaefig.class);
            spielScreen.spawnDynmischesObjekt(dDef);
        }
    }

    public void erstelleBewegendeBloecke(boolean vel, int layer){
        for (MapObject object : karte.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bewegendeBloecke.add(new BewegenderBlock(spielScreen, rect.getX() / Statics.PPM + rect.getWidth() / 2 / Statics.PPM, rect.getY() / Statics.PPM));
        }
    }

    public void erstelleVoegel(boolean acl, int layer){
        for (MapObject object : karte.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if (rect.getWidth() == 16f) {
                spielScreen.getVogelVerwalter().rSpawn(acl, false, rect.getX() / Statics.PPM, rect.getY() / Statics.PPM);
            } else if (rect.getWidth() > 16f){
                spielScreen.getVogelVerwalter().rSpawn(acl, true, rect.getX() / Statics.PPM, rect.getY() / Statics.PPM);
            }
        }
    }

    //Hier werdem sämtliche Hitboxen aus der XML datei ausgelesen und in das Spiel eingefügt
    public void erstelleHitboxen(boolean isStatic, int layer){
        for (MapObject object : karte.getLayers().get(layer).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            Body pbody;
            BodyDef def = new BodyDef();

            if (isStatic) {
                //StaticBodys können nicht bewegt werden
                def.type = BodyDef.BodyType.StaticBody;
            } else {
                //DynamicBodys übernehmen bei Berührung die Bewegung des Objektes * einen Faktor
                def.type = BodyDef.BodyType.DynamicBody;
            }

            def.position.set((rect.getX() + rect.getWidth() /2 )/ Statics.PPM, (rect.getY() + rect.getHeight() / 2 )/ Statics.PPM);
            def.fixedRotation = true;

            pbody = welt.createBody(def);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rect.getWidth() / 2 / Statics.PPM, rect.getHeight() / 2 / Statics.PPM);

            FixtureDef fdef = new FixtureDef();
            fdef.shape = shape;

            if (layer == 5){
                //In der 5. Layer befinden sich Objekte, die den Spieler töten sollen, weswegen hier ein anderer Bit_Filter gesetzt werden muss, um diese Hitboxen im WorldContanctlistener
                //von anderen zu unterscheiden
                fdef.filter.categoryBits = Bit_Filter.STACHEL_BIT;
                pbody.createFixture(fdef).setUserData("death");
            } else {
                fdef.filter.categoryBits = Bit_Filter.NORMALER_OBJEKT_BIT;
                pbody.createFixture(fdef).setUserData("brk");
            }

            //Grafikspeicher freigeben
            shape.dispose();
        }

    }

    public Array<BewegendesTiledObjekt> getBewegendeBloecke() {
        return bewegendeBloecke;
    }

    @Override
    public void dispose() {
        //Grafikspeicher freigeben
        welt.dispose();
        karte.dispose();
    }
}

package me.nroffler.Screens;

//LibGDX
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

//Box2DLights
import box2dLight.PointLight;
import box2dLight.RayHandler;

//Meine Klassen
import me.nroffler.DynamischeObjekte.DynamischesObjekt;
import me.nroffler.DynamischeObjekte.DynmischeDef;
import me.nroffler.DynamischeObjekte.Kaefig;
import me.nroffler.Scenes.Hud;
import me.nroffler.Scenes.LevelInfoScreen;
import me.nroffler.main.DarkWorld;
import me.nroffler.main.Statics;
import me.nroffler.sprites.Spieler;
import me.nroffler.sprites.VogelVerwalter;
import me.nroffler.tiledobjekts.BewegendesTiledObjekt;
import me.nroffler.tiledobjekts.KartenErsteller;
import me.nroffler.tiledobjekts.WorldContanctlistener;
import me.nroffler.tiledobjekts.ZielPartikel;

//da ich diese Variable sehr oft verwende habe ich mich dazu entschieden, sie zu importieren
import static me.nroffler.main.Statics.PPM;

public class SpielScreen implements Screen {

    private boolean imSpiel;
    private boolean pause;
    private boolean debug;
    private float energie;

    private int[] hEbenen;
    private int[] vEbenen;

    //Objkete, deren Klassen von libGDX stammmen
    private TiledMap karte;
    private OrthogonalTiledMapRenderer renderer;

    private OrthographicCamera spielKamera;
    private Viewport spielPort;

    private World welt;
    private Box2DDebugRenderer b2dr;

    private RayHandler handler;
    private PointLight licht;

    private AssetManager assetManager;
    private DarkWorld darkWorld;

    //Objekte nach eigenen Klassen
    private Spieler spieler;
    private Hud hud;
    private ZielPartikel zielPartikel;
    private KartenErsteller kartenErsteller;
    private LevelInfoScreen levelInfoScreen;
    private VogelVerwalter vogelVerwalter;

    private Array<DynamischesObjekt> dynamischeObjekte;
    private Array<DynmischeDef> dynamischeWarteschlange;

    public enum Todesgrund {
        LEBT,
        AUS_DEM_BILD,
        STACHEL_BERUERT,
        LEVEL_ERFOLGREICH,
        KEINE_ENERGIE,
        SPIEL_BEENDET
    }

    public Todesgrund todesgrund;

    public SpielScreen(DarkWorld darkWorld){
        this.darkWorld = darkWorld;

        //Ebenen der XML Datei h = Hintergrund, ...
        hEbenen = new int[1];

        vEbenen = new int[2];
        vEbenen[0] = 2;
        vEbenen[1] = 4;

        //AssetManager wird für alle Texturen benötigt
        assetManager = darkWorld.assetManager;

        //Kamera und Vieport werden benötigt, um das Spiel der Bildschrimgröße anzupassen, vor allem für Android wichtig
        spielKamera = new OrthographicCamera();
        spielPort = new FitViewport(Statics.V_BREITE / PPM, Statics.V_HOEHE / PPM, spielKamera);

        //Arraylisten um Dynamische Objekte zu erschaffen
        dynamischeObjekte = new Array<>();
        dynamischeWarteschlange = new Array<>();

        //Karte aus XML Datei laden
        TmxMapLoader maplader = new TmxMapLoader();
        String mappickstring = ("level/map1-"+Integer.valueOf(Statics.level).toString()+".tmx");
        System.out.println(mappickstring);
        karte = maplader.load(mappickstring);
        renderer = new OrthogonalTiledMapRenderer(karte, 1 / PPM);

        //Kamera setzen
        spielKamera.position.set(spielPort.getWorldWidth() / 2, spielPort.getWorldHeight() /2, 0);

        //Welt wird mit einer Schwerkraft von -4.5f initialisiert
        welt = new World(new Vector2(0,-4.5f), true);
        //Kollisionsabfragen werden von der Klasse WorldContanctlistener bearbeitet
        welt.setContactListener(new WorldContanctlistener(this));

        //DebugRenderer um Hitboxen zu sehen
        b2dr = new Box2DDebugRenderer();

        //eigene Objekte initialisieren
        spieler = new Spieler(this);
        hud = new Hud(this);
        zielPartikel = new ZielPartikel(38.3f,1.6f, this);
        vogelVerwalter = new VogelVerwalter(this);

        //Map(Hitboxen) aus XML Datei bauen
        kartenErsteller = new KartenErsteller(this);
        kartenErsteller.erstelleHitboxen(true, 1);
        kartenErsteller.erstelleKaefige(3);
        kartenErsteller.erstelleHitboxen(true, 5);
        kartenErsteller.erstelleBewegendeBloecke(true,6);
        kartenErsteller.erstelleVoegel(true, 7);

        //Handler, der Lichtstrahlen berechnen kann
        handler = new RayHandler(welt);
        handler.setAmbientLight(1f);

        //Neue Lichtquelle erstellen (sichtbar am Ende des Levels)
        licht = new PointLight(handler, 100, Color.WHITE, 1.3f / PPM, 0,0);

        //Licht soll nicht an Objekten aufgehalten werden um Leistung zu sparen
        //zusätzlich, da die Computer unserer Schule nicht genug (Garfik-)Leistung haben, werden Schatten deaktiviert
        licht.setSoftnessLength(1f);
        licht.setActive(false);

        //Energie für das level initialiseren
        energie = 9f;

        //Debug
        debug = Statics.debug;

        //Splashscreen
        levelInfoScreen = new LevelInfoScreen(this);

        //Pausemenu
        pause = false;

        //Spieler lebt
        todesgrund = Todesgrund.LEBT;
    }

    //public, damit man auch Objekte aus anderen Klassen heraus spawnen kann, wie z.B MapCreator
    public void spawnDynmischesObjekt(DynmischeDef ddef){
        dynamischeWarteschlange.add(ddef);
    }

    private void erschaffeDynamischeObjekte(){
        //Objekte aus der Warteschlange in die Arraylist zum updaten schieben
        if (!dynamischeWarteschlange.isEmpty()){
            DynmischeDef ddef = dynamischeWarteschlange.get(dynamischeWarteschlange.size-1);
            //verschiedene Objekte brauchen verschiedene aktionen, deswegen wird hier getrennt, aber im Moment gibt es nur eins
            if (ddef.art == Kaefig.class){
                dynamischeObjekte.add(new Kaefig(SpielScreen.this, ddef.position.x, ddef.position.y));
                dynamischeWarteschlange.pop();
            }
        }
    }

    @Override
    public void show() {
        System.out.println("navigate: Play");

        //Spiel starten
        imSpiel = true;
    }

    public void update(float dt){
        //Untermethoden die zur Strukturierung dienen und Updatemethoden der einzelnen Objekte aufrufen
        bearbeiteEingaben(dt);

        erschaffeDynamischeObjekte();

        if (!pause) {
            spieler.update(dt);
        }

        zielPartikel.update(dt);

        updateKamera();

        //für n Blöcke
        for (BewegendesTiledObjekt bewegendesTiledObjekt : kartenErsteller.getBewegendeBloecke()) {
            bewegendesTiledObjekt.update(dt);
        }

        //für n Objekte
        for (DynamischesObjekt dynamischesObjekt: dynamischeObjekte){
            dynamischesObjekt.update(dt);
        }


        //Welt mit 1 / 60f also 60 Bildern die Sekunde berechnen, jedes Bild wird mit 6 VelocityIterations Schritten und 2 positionIterations Schritten berechnet
        welt.step(1 / 60f,6,2);

        vogelVerwalter.update(dt);

        updateLicht();

        levelInfoScreen.update(dt);
    }

    private void updateKamera(){
        //Kamera der Position des Spielers anpassen
        if (imSpiel) {
            //Sobald spieler am Ende der Karte ist, Kamerabewegung anhalten
            if (spielKamera.position.x < 36.2f && spielKamera.position.x > 0.96f && spieler.b2body.getPosition().x >= 0.96f) {
                spielKamera.position.x = spieler.b2body.getPosition().x + 128 / PPM;
                /*if (player.b2body.getPosition().y < 1.6f)
                gamecam.position.y = gamecam.viewportHeight /2 + (player.b2body.getPosition().y / 10);*/
            }
        }

        //Verschiebung der Kamera berechnen
        float alteBreite = spielKamera.viewportWidth * spielKamera.zoom;
        float alteHoehe = spielKamera.viewportHeight *spielKamera.zoom;

        float neueBreite = alteBreite * Math.abs(spielKamera.up.y) + alteHoehe * Math.abs(spielKamera.up.x);
        float neueHoehe = alteHoehe * Math.abs(spielKamera.up.y) + alteBreite * Math.abs(spielKamera.up.x);
        float xNeu = spielKamera.position.x - neueBreite / 2;
        float yNeu = spielKamera.position.y - neueHoehe / 2;

        xNeu -= 64f / PPM;
        neueBreite += 64f / PPM;
        //------------------------------

        spielKamera.update();
        renderer.setView(spielKamera.combined,xNeu,yNeu,neueBreite,neueHoehe);
    }

    private void updateLicht() {
        //Licht der Position des Spielers anpassen (sieht man nur am Ende)
        licht.setPosition(spieler.b2body.getPosition().x/PPM,spieler.b2body.getPosition().y/PPM);
        handler.update();
    }

    private void bearbeiteEingaben(float dt) {
        //Spiel stoppen
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            initStop();
        }

        //Hud (weißer Balken und Pause-Knopf) updaten
        hud.update(dt);

        //Wenn das Spiel pausiert ist, werden folgende updates unnötig
        if (pause){
            return;
        }

        //Desktop: Gdx.input.isKeyPressed(Input.Keys.SPACE) wenn Leertaste gedrückt wird
        //         Gdx.input.isTouched() oder wenn mit der maus gedrückt gehalten wird
        //Android oder IOS: Gdx.input.isTouched() bei Bildschirm berührung
        //--> Energy abziehen
        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (energie > 0 && levelInfoScreen.istAbgelaufen()) {
                if (!Statics.debug) {
                    energie -= 0.04f;
                }
            }
            if (energie <= 0){
                //abstürtzen wenn Energie leer ist
                return;
            }
            //Man kann sich erst bewegen, sobald der Splashscreen mit "Level n" abgelaufen ist
            if (levelInfoScreen.istAbgelaufen() || Statics.debug) {
                bewegen(dt);
            }

        } else if (energie < 10f) {
            energie += 0.0015f;
        }

        //Energy ans Hud übergeben, um weißen Energybalken zu updaten
        hud.setBildBreite(energie);

        //Sterben wenn Licht/Energy verbraucht
        if (energie < 0.1 && istImSpiel()){
            setStatus(false, Todesgrund.KEINE_ENERGIE);
            licht.setDistance(0);
        }

        //Spieler töten wenn er aus dem Bild (vieport) rausfliegt
        if ((spieler.b2body.getPosition().y < 0 || spieler.b2body.getPosition().y > spielKamera.viewportWidth-spielKamera.viewportWidth/2+spieler.getWidth()*2) && istImSpiel()){
            setStatus(false, Todesgrund.AUS_DEM_BILD);
        }

        //Licht an Energy anpassen
        if (imSpiel || !(todesgrund == Todesgrund.LEVEL_ERFOLGREICH) && !(todesgrund == Todesgrund.SPIEL_BEENDET)) {
            licht.setDistance(energie / 1000000);
        }

    }

    public void pausieren(){
        //Pause boolean switchen
        pause = !pause;

        if (pause){
            //Bewegung stoppen
            spieler.b2body.setLinearVelocity(new Vector2(0,0));
            //Spieler wird nicht mehr von der Schwerkraft beeinflusst
            spieler.b2body.setGravityScale(0f);
            //Spiel abdunkeln
            handler.setAmbientLight(0.05f);
        } else {
            //Schwerkraft wieder aktivieren
            spieler.b2body.setGravityScale(1f);
            //Abdunkeln aufheben
            handler.setAmbientLight(1f);
        }
    }

    private void bewegen(float dt) {
        //Solange der Spieler noch lebt / das Spiel noch läuft:
        if (imSpiel) {
            //in x-Richtung bis zu einen bestimmten wert beschleunigen
            if (spieler.b2body.getLinearVelocity().x < 1.7f) {
                spieler.b2body.applyLinearImpulse(new Vector2(100 * dt / PPM, 0), spieler.b2body.getWorldCenter(), true);
            }
            //in y-Richtung berschleunigen
            spieler.b2body.applyLinearImpulse(new Vector2(0, 1200 * dt / PPM), spieler.b2body.getWorldCenter(), true);
        }
    }


    @Override
    public void render(float delta) {
        // Zum optimieren
        if (Gdx.graphics.getFramesPerSecond() < 60 && spieler.b2body.getPosition().x > 1f){
            System.out.println("performance warning"+Gdx.graphics.getFramesPerSecond());
        }

        //alles schwarz übermalen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Updates
        update(delta);

        //Welt rendern
        renderer.render(hEbenen);
        renderer.render(vEbenen);

        //-----------------------------------------------------------------------------
        //Kamera über ProjectionMatrix an Container übergeben und Grafikenrenderer starten (batch.begin)
        darkWorld.batch.setProjectionMatrix(spielKamera.combined);
        darkWorld.batch.begin();

        //Spielertextur zeichnen
        spieler.draw(darkWorld.batch);

        //alle Objekte zeichnen
        for (DynamischesObjekt dynamischesObjekt : dynamischeObjekte){
            dynamischesObjekt.draw(darkWorld.batch);
        }

        //wenn nicht pausiert, Blöcke bewegen
        if (!pause) {
            for (BewegendesTiledObjekt movingTiledObjekt : kartenErsteller.getBewegendeBloecke()) {
                movingTiledObjekt.draw(darkWorld.batch);
            }
        }

        //wenn nicht Tod, schwarze Partikel zeichnen
        if((licht.getDistance() < 0.1f)) {
            zielPartikel.draw(darkWorld.batch);
        }

        //Effekte zeichnen
        for (DynamischesObjekt dynamischesObjekt : dynamischeObjekte){
            dynamischesObjekt.drawEffects(darkWorld.batch);
        }

        //Splashscreen
        if (!levelInfoScreen.istAbgelaufen()){
            levelInfoScreen.draw(darkWorld.batch);
        }

        //Vögel zeichnen
        vogelVerwalter.draw(darkWorld.batch);

        //Grafik zeichnen beenden
        darkWorld.batch.end();
        //-----------------------------------------------------------------------------

        //Tod
        if (!imSpiel){
            //Wenn das Level beendet wurde
            if (todesgrund == Todesgrund.LEVEL_ERFOLGREICH || todesgrund == Todesgrund.SPIEL_BEENDET) {
                licht.setActive(true);
                if (licht.getDistance() < 0.1f) {
                    //um Fehler zu vermeiden, Schwerkraft und Bewegung stoppen
                    welt.setGravity(new Vector2(0, 0));
                    spieler.b2body.setLinearVelocity(0, 0);
                    //Lichtheller machen
                    licht.setDistance(licht.getDistance() + 0.001f);
                    //schwarze Partikel kleiner werden lassen
                    zielPartikel.sclne();
                    if (energie > 0) {
                        //Energybalken leeren
                        energie -= 0.1f;
                        hud.setBildBreite(energie);
                    } else if (energie < 0) {
                        energie = 0;
                    }
                } else {
                    beendeLevel();
                }
                //wenn man vorher gestorben ist
            } else if (todesgrund == Todesgrund.AUS_DEM_BILD || todesgrund == Todesgrund.KEINE_ENERGIE || todesgrund == Todesgrund.STACHEL_BERUERT) {
                if (energie > 0.1f){
                    energie -= 0.1f;
                    hud.setBildBreite(energie);
                    if (energie <= 0.1f){
                        beendeLevel();
                    }
                }
            }
        }

        //Hitboxen rendern
        if (debug) {
            b2dr.render(welt, spielKamera.combined);
        }

        //Lichtberechnung nur innerhalb eines bestimmten bereiches, um Leistung zu sparen
        handler.setCombinedMatrix(spielKamera.combined.scl(PPM, PPM,0), spielKamera.position.x / PPM, spielKamera.position.y / PPM, spielKamera.viewportWidth, spielKamera.viewportHeight);
        handler.render();

        //Neue Camera an Vieport binden, um ein nicht bewegeneds Hud zu zeichnen
        if (energie >= 0.1f) {
            darkWorld.batch.setProjectionMatrix(hud.stage.getCamera().combined);
            hud.stage.act();
            hud.stage.draw();
        }
    }

    private void beendeLevel(){
        switch (todesgrund){
            case LEBT:
                break;
            case STACHEL_BERUERT:
                darkWorld.setScreen(new LeitScreen(darkWorld, Todesgrund.STACHEL_BERUERT.ordinal()));
                break;
            case LEVEL_ERFOLGREICH:
                Statics.level++;
                speichern();
                darkWorld.setScreen(new LeitScreen(darkWorld, Todesgrund.LEVEL_ERFOLGREICH.ordinal()));
                break;
            case KEINE_ENERGIE:
                darkWorld.setScreen(new LeitScreen(darkWorld, Todesgrund.KEINE_ENERGIE.ordinal()));
                break;
            case AUS_DEM_BILD:
                darkWorld.setScreen(new LeitScreen(darkWorld, Todesgrund.AUS_DEM_BILD.ordinal()));
                break;
            case SPIEL_BEENDET:
                Statics.dasSpielIstWirklichVorbei = true;
                darkWorld.setScreen(new LadeScreen(darkWorld, Todesgrund.SPIEL_BEENDET.ordinal()));
        }
    }

    private void speichern(){
        Preferences mypref = Gdx.app.getPreferences("DarkWorld");
        mypref.putString("DarkWorld", "localdata");
        mypref.putInteger("level", Statics.level);
        mypref.flush();
    }

    private void initStop(){
        assetManager.clear();
        Gdx.app.exit();
    }

    @Override
    public void resize(int width, int height) {
        //Wenn das Fenster vergrößert wird skaliert das Spiel mit
        spielPort.update(width, height);
    }

    @Override
    public void pause() {
        pause = true;
    }

    @Override
    public void resume() {
        pause = false;
    }

    @Override
    public void hide() {
        pause = true;
    }

    @Override
    public void dispose() {
        //Grafikspeicher freigeben
        handler.dispose();
        welt.dispose();
        karte.dispose();
        renderer.dispose();
        spieler.dispose();
        zielPartikel.dispose();
        licht.dispose();
        kartenErsteller.dispose();
        hud.dispose();
    }

    //Getter und Setter
    public AssetManager getAssetManager(){return assetManager;}

    public World getWelt() {
        return welt;
    }

    public void setStatus(boolean running, Todesgrund todesgrund){
        this.imSpiel = running;
        this.todesgrund = todesgrund;
    }

    public boolean istImSpiel(){
        return imSpiel;
    }

    public Map getKarte(){
        return karte;
    }

    public void addEnergie(float energie){
        this.energie += energie;
    }

    public Spieler getSpieler(){
        return spieler;
    }

    public SpriteBatch getBatch(){
        return darkWorld.batch;
    }

    public boolean istPausiert(){
        return pause;
    }

    public VogelVerwalter getVogelVerwalter() {
        return vogelVerwalter;
    }
}

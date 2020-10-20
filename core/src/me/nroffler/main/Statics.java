package me.nroffler.main;

public class Statics {

    //statics, die sein müssen, da es keine andere Möglichkeit gibt
    public static int level = 0;
    public static final int BILDER_PRO_SEKUNDE = 60;
    public static boolean ersterStart = true;
    public static boolean ueberschreiben = false;
    public static boolean debug = false;
    public static boolean dasSpielIstWirklichVorbei = false;

    //Größe des Desktopfensters
    public static final int BREITE = 1200;
    public static final int HOEHE = 624;

    //virtuelle Größe des Androidfensters
    //für Android wird das Bild hochskaliert zur Bildschirmgröße des Gerätes, berechnet wird es aber in 400*208 Pixeln
    public static final int V_BREITE = 440;
    public static final int V_HOEHE = 240;

    //zum Welt skalieren
    public static final float PPM = 100;

}

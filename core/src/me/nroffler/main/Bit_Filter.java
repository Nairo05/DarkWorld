package me.nroffler.main;

public class Bit_Filter {

    //dies ist eine Möglichkeit, sehr effiziente Kollisionsabfragen zu verwalten
    //jedes Objekt/jede Objektgruppe bekommt eine Zahl zugewiesen, durch die man sie indentifizieren kann
    //Diese sind static, da sie in vielen Klassen verwendet werden

    public static final short NON_COLLIDE_OBJEKT_BIT = 0;
    public static final short SPIELER_BIT = 2;
    public static final short LICHT_BIT = 4;
    public static final short NORMALER_OBJEKT_BIT = 8;
    public static final short KAEFIG_BIT = 26;
    public static final short STACHEL_BIT = 32;
    public static final short BEWEGENDER_BIT = 64;

}

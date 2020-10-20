package me.nroffler.tiledobjekts;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import me.nroffler.DynamischeObjekte.DynamischesObjekt;
import me.nroffler.Screens.SpielScreen;
import me.nroffler.main.Bit_Filter;

public class WorldContanctlistener implements ContactListener {

    private SpielScreen spielScreen;

    public WorldContanctlistener(SpielScreen spielScreen){
        this.spielScreen = spielScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        //In meinem Fall habe ich nur eine Art der Kollision, 2 Objekte
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //Wenn der Spieler mit einem Item/Dynamischen Objekt interagiert zum ersten mal
        if (fixA.getFilterData().categoryBits == Bit_Filter.KAEFIG_BIT && fixB.getFilterData().categoryBits == Bit_Filter.SPIELER_BIT && !(((DynamischesObjekt) fixA.getUserData()).istbenutzt())){
            benutzeItem(fixA);
            return;
        } else if (fixB.getFilterData().categoryBits == Bit_Filter.KAEFIG_BIT && fixA.getFilterData().categoryBits == Bit_Filter.SPIELER_BIT && !(((DynamischesObjekt) fixB.getUserData()).istbenutzt())){
            benutzeItem(fixB);
            return;
        }

        //Wenn ein Item mit der Welt interagiert
        if (fixA.getFilterData().categoryBits == Bit_Filter.KAEFIG_BIT && fixB.getFilterData().categoryBits == Bit_Filter.NORMALER_OBJEKT_BIT){
            ItemUsePhase2(fixA);
            return;
        } else if (fixB.getFilterData().categoryBits == Bit_Filter.KAEFIG_BIT && fixA.getFilterData().categoryBits == Bit_Filter.NORMALER_OBJEKT_BIT){
            ItemUsePhase2(fixB);
            return;
        }

        //Wenn der Spieler zum 2. Mal mit einem Item interagiert
        if (fixA.getFilterData().categoryBits == Bit_Filter.KAEFIG_BIT && fixB.getFilterData().categoryBits == Bit_Filter.SPIELER_BIT && (((DynamischesObjekt) fixA.getUserData()).istbenutzt())){
            finish(fixA);
            return;
        } else if (fixB.getFilterData().categoryBits == Bit_Filter.KAEFIG_BIT && fixA.getFilterData().categoryBits == Bit_Filter.SPIELER_BIT && (((DynamischesObjekt) fixB.getUserData()).istbenutzt())){
            finish(fixB);
            return;
        }

        //Wenn der Spieler mit einem Objekt interagiert, was ihn töten soll
        if (fixA.getFilterData().categoryBits == Bit_Filter.STACHEL_BIT && fixB.getFilterData().categoryBits == Bit_Filter.SPIELER_BIT){
            toteSpieler();
            return;
        } else if (fixB.getFilterData().categoryBits == Bit_Filter.SPIELER_BIT && fixA.getFilterData().categoryBits == Bit_Filter.STACHEL_BIT){
            toteSpieler();
            return;
        }

        //Wenn zwei Items miteinander interagieren
        if (fixA.getFilterData().categoryBits == Bit_Filter.KAEFIG_BIT && fixB.getFilterData().categoryBits == Bit_Filter.KAEFIG_BIT){
            finish(fixA);
            finish(fixB);
            return;
        }

        if (fixA.getFilterData().categoryBits == Bit_Filter.BEWEGENDER_BIT && fixB.getFilterData().categoryBits == Bit_Filter.NORMALER_OBJEKT_BIT){
            reverseVel(fixA);
            return;
        } else if (fixB.getFilterData().categoryBits == Bit_Filter.BEWEGENDER_BIT && fixA.getFilterData().categoryBits == Bit_Filter.NORMALER_OBJEKT_BIT){
            reverseVel(fixB);
            return;
        }

        //Wenn der Spieler ein tiled berührt, das sich bewegt
        if (fixA.getFilterData().categoryBits == Bit_Filter.SPIELER_BIT && fixB.getFilterData().categoryBits == Bit_Filter.BEWEGENDER_BIT){
            reverseVel(fixB);
            return;
        } else if (fixB.getFilterData().categoryBits == Bit_Filter.SPIELER_BIT && fixA.getFilterData().categoryBits == Bit_Filter.BEWEGENDER_BIT){
            reverseVel(fixA);
            return;
        }

        //Wenn ein Item/Dynamisches Objekt mit einem Objekt Interagiert, was es zerstören soll
        if (fixA.getFilterData().categoryBits == Bit_Filter.STACHEL_BIT && fixB.getFilterData().categoryBits == Bit_Filter.KAEFIG_BIT){
            finish(fixB);
        } else if (fixB.getFilterData().categoryBits == Bit_Filter.KAEFIG_BIT && fixA.getFilterData().categoryBits == Bit_Filter.STACHEL_BIT){
            finish(fixA);
        }

        //Wenn eine if Abfrage true ergibt, steht fest um welche Kollision es sich hier handelt, dann müssen die anderen Abfragen nicht mehr durchlaufen, deswegen return; nach jeder Abfrage

        //Alle Anderen Kollisionen werden automatisch abgebrochen / zurück gesetzt

    }

    private void reverseVel(Fixture fixture){
        ((BewegendesTiledObjekt) fixture.getUserData()).richtungAendern(false, true);
        ((BewegendesTiledObjekt) fixture.getUserData()).reduceVelocity();
    }

    private void finish(Fixture fixture){
        //Jedes Item hat eine Abstakte Oberklasse, als das hier die Fixture gecastet werden kann, um die jeweiligen Methoden aufzurufen
        ((DynamischesObjekt) fixture.getUserData()).zerstoeren();
        ((DynamischesObjekt) fixture.getUserData()).beenden();
    }

    private void benutzeItem(Fixture fixture) {
        //Jedes Item hat eine Abstakte Oberklasse, als das hier die Fixture gecastet werden kann, um die jeweiligen Methoden aufzurufen
        ((DynamischesObjekt) fixture.getUserData()).benutzen();
    }

    private void ItemUsePhase2(Fixture fixture){
        //Jedes Item hat eine Abstakte Oberklasse, als das hier die Fixture gecastet werden kann, um die jeweiligen Methoden aufzurufen
        ((DynamischesObjekt) fixture.getUserData()).zerstoeren();
    }

    private void toteSpieler(){
        //Spieler töten
        spielScreen.setStatus(false, SpielScreen.Todesgrund.STACHEL_BERUERT);
    }

    //Wird für mein Spiel nicht benötigt, muss aber durch implements ContactListener hingeschrieben werden
    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}

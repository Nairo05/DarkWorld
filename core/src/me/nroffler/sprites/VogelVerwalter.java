package me.nroffler.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import me.nroffler.Screens.SpielScreen;

public class VogelVerwalter {

    private SpielScreen spielScreen;
    private Random zufall;
    //Da nicht eine feste Anzahl von Vögeln immer auf dem Bildschirm ist, benutze ich hier eine Arrayliste um n-viele in der Welt haben zu können
    private Array<Vogel> birds;

    public VogelVerwalter(SpielScreen spielScreen){
        this.spielScreen = spielScreen;

        birds = new Array<>();
        zufall = new Random();
    }

    public void update(float dt){
        //Alle Vögel werden geupdatet
        for (int i = 0; i < birds.size; i++){
            //Die Vögel werden nur geupdatet wenn sie innerhalb der Sichtweite sind
            if (((spielScreen.getSpieler().getX()) + 6f) > birds.get(i).getX()) {
                birds.get(i).update(dt);
            }

            //Sobald der Vogel außerhalb der Sichtfeldes der Kamera ist, wird er entfernt und aus dem Grafikspeicher geworfen
            if (birds.get(i).getX() < spielScreen.getSpieler().getX()-5f){
                System.out.println("bird removed");
                birds.get(i).dispose();
                birds.removeIndex(i);
            }
        }

    }

    public void draw(SpriteBatch batch){
        //Alle Vögel zeichnen
        for (int i = 0; i < birds.size; i++){
            birds.get(i).draw(batch);
        }
    }

    public void spawnBird(float x, float y){
        birds.add(new FliegenderVogel(x, y, spielScreen));
    }

    public void spawnDumpBird(float x, float y){
        birds.add(new SitzenderVogel(x, y, spielScreen));
    }

    public void spawnGroupBird(float x, float y){
        birds.add(new FliegenderVogel(x + 1f, y-0.1f, spielScreen));
        birds.add(new FliegenderVogel(x + 0.92f, y, spielScreen));
        birds.add(new FliegenderVogel(x + 1.02f, y+0.1f, spielScreen));
    }

    //Diese Methode wird vom MapCreator.java aufgerufen
    public void rSpawn(boolean groupspawn, boolean dumpbird, float x, float y){
        if (dumpbird){
            spawnDumpBird(x, y);
        } else {
            if (zufall.nextInt(3) == 1) {
                if (groupspawn) {
                    spawnGroupBird(x, y);
                } else {
                    spawnBird(x + 0.5f, y);
                }
            } else {
                spawnBird(x + 0.5f, y);
            }
        }
    }

}

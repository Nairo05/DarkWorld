package me.nroffler.Scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import me.nroffler.Screens.SpielScreen;
import me.nroffler.main.Statics;

public class LevelInfoScreen implements Disposable {

    private int zaehler = 160;
    private boolean abgelaufen = false;
    private int level;
    private String levelstr;
    private Texture textur;
    private Sprite sprite;

    public LevelInfoScreen(SpielScreen spielScreen){
        this.level = Statics.level;
        levelstr = "splashs/"+level+".png";
        textur = spielScreen.getAssetManager().get("splashs/"+level+".png", Texture.class);
        sprite = new Sprite(textur);
    }

    public void update(float dt){
        if (!abgelaufen){
            zaehler--;
            if (zaehler <= 0){
                abgelaufen = true;
            }
        }
    }

    public void draw(SpriteBatch batch){
        if (!abgelaufen){
            batch.draw(sprite, 0,0, 4.45f, 2.4f);
        }
    }

    public boolean istAbgelaufen(){
        return abgelaufen;
    }

    @Override
    public void dispose() {
        textur.dispose();
    }
}

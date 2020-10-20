package me.nroffler.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import me.nroffler.Screens.SpielScreen;
import me.nroffler.main.Statics;

public abstract class Vogel extends Sprite implements Disposable {

    //Sowohl die schwazen Vögel, die sich nicht bewegen, als auch die schwarzen fliegenden
    // befinden sich auf dem selben Sheet, zudem haben sie eine Menge Attribute gemeinsam, weswegen ich diese Klase angelegt habe
    protected float x, y;
    protected SpielScreen spielScreen;
    protected Texture texture;
    protected Body b2body;
    protected Sprite sprite;
    //Teile einer Textur
    protected TextureRegion regions[][];

    public Vogel(float x, float y, SpielScreen spielScreen){
        this.x = x;
        this.y = y;
        this.spielScreen = spielScreen;

        defineBird(spielScreen);

        texture = spielScreen.getAssetManager().get("Sprites/bird_animation.png", Texture.class);

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setBounds(0,0,16 / Statics.PPM,16 / Statics.PPM);

        //Das große Sheet, wird in die einzelnen Texturen gesplitet
        regions = TextureRegion.split(texture, 16,16);
        sprite = new Sprite(regions[0][7]);
        setRegion(sprite);
    }

    public abstract void update(float dt);
    public abstract void defineBird(SpielScreen spielScreen);
}

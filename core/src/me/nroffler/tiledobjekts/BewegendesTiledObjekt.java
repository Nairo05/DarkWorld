package me.nroffler.tiledobjekts;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import me.nroffler.Screens.SpielScreen;

public abstract class BewegendesTiledObjekt extends Sprite {

    protected World world;
    protected SpielScreen spielScreen;
    public Body b2body;
    public Vector2 velocity;

    private boolean destroyed;

    public BewegendesTiledObjekt(SpielScreen spielScreen, float x, float y){

        this.world = spielScreen.getWelt();
        this.spielScreen = spielScreen;

        destroyed = false;

        setPosition(x, y);

        defineTile();

        velocity = new Vector2(0, 1);
    }

    public abstract void update(float dt);

    public void reduceVelocity(){
        if (velocity.y == 1f){
            velocity.y = 0.7f;
        }
    }

    protected abstract void defineTile();
    protected void destroy(){
        destroyed = true;
    }
    protected boolean getDestroyed(){
        return destroyed;
    }
    public void richtungAendern(boolean x, boolean y){
        if (x){
            velocity.x = - velocity.x;
        }
        if (y){
            velocity.y = - velocity.y;
        }
    }

}

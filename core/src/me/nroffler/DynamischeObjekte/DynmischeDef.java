package me.nroffler.DynamischeObjekte;

import com.badlogic.gdx.math.Vector2;

public class DynmischeDef {

    //diese Klasse wird benötigt, um verschiedene dynamische Objekte in der selben Arrayliste (--> PlayScreen) verwenden zu können

    public Vector2 position;
    public Class<?> art;

    public DynmischeDef(Vector2 position, Class<?> art){
        this.position = position;
        this.art = art;
    }
}

package me.notnroffler.backup.tst.Joints;

public class JointManager {

    //----------------------------------------------------------------------------------------------

    //kostet zu viel Leistung, wurde rausgenommen

    //----------------------------------------------------------------------------------------------

    /*private PlayScreen screen;
    private Texture texture;
    private TextureRegion region;
    private Array<Body> bodies;
    private Sprite sprite;

    public JointManager(PlayScreen screen){

        this.screen = screen;
        bodies = new Array<Body>();

        texture = screen.getAssetManager().get("level/tileset_15.png", Texture.class);
        region = new TextureRegion(texture, 54, 20, 4,4);
        sprite = new Sprite(region);

        if (screen.getlevel() == 1){
            initJoint(3f, 3f);
        }
    }

    public void draw(SpriteBatch batch){
        if (screen.getlevel() == 1) {
            for (int i = 1; i < 5; i++) {
                batch.draw(sprite, bodies.get(i).getPosition().x - 2 / Statics.PPM, bodies.get(i).getPosition().y - 16 / 2 / Statics.PPM, 4 / Statics.PPM, 16 / Statics.PPM);
            }
        }
    }

    private void initJoint(float x, float y){
        bodies.add(createBox(800, 170,16,16, true, true));
        for (int i = 1; i < 5; i++){
            bodies.add(createBox(800, 50+(i * 12),2,16, false, false));

            RopeJointDef rdef = new RopeJointDef();
            rdef.bodyA = bodies.get(i-1);
            rdef.bodyB = bodies.get(i);
            rdef.collideConnected = true;
            rdef.maxLength = 28f / Statics.PPM;

            rdef.localAnchorA.set(0, -1f / Statics.PPM);
            rdef.localAnchorB.set(0, 1f / Statics.PPM);

            screen.getWorld().createJoint(rdef);
        }
    }

    public Body createBox(int x, int y, int width, int height, boolean isStatic, boolean fixedrotation){
        Body pBody;
        BodyDef def = new BodyDef();

        if (isStatic){
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }
        def.position.set(x / Statics.PPM,y / Statics.PPM);
        if (fixedrotation) {
            def.fixedRotation = true;
        } else {
            //def.fixedRotation = false;
        }
        pBody = screen.getWorld().createBody(def);

        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = Bit_Filter.COLLIDE_OBJEKT_BIT;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2 / Statics.PPM, height/2 / Statics.PPM);
        fdef.shape = shape;

        pBody.createFixture(fdef);
        shape.dispose();

        pBody.setGravityScale(0.2f);
        return pBody;
    }*/

}

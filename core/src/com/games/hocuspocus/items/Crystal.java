package com.games.hocuspocus.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.games.hocuspocus.HocusPocus;

public class Crystal extends Sprite{
	
	private Texture texture;
	private World world;
	private Body body;
	private BodyDef bdef;
	private FixtureDef fdef;
	private TextureRegion region;
	private boolean setToDestroy;
	private boolean destroyed;

	
	public Crystal(World world, float x, float y){
		
		
		this.world = world;
		bdef = new BodyDef();
		fdef = new FixtureDef();
		texture = new Texture("images/crystal.png");
		region = new TextureRegion(texture,0,0,18,16);
		bdef.position.set(x/HocusPocus.PPM,y/HocusPocus.PPM);
		
		defineBody();
		
		setRegion(region);
		setBounds(0, 0, 18/HocusPocus.PPM, 16/HocusPocus.PPM);
		
		setPosition(body.getPosition().x - getWidth()/2 - 2/HocusPocus.PPM, 
				body.getPosition().y - getHeight()/2- 1/HocusPocus.PPM);
		
		setToDestroy = false;
		
	}


	private void defineBody() {
		
		bdef.type = BodyType.StaticBody;
		body = world.createBody(bdef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(7/HocusPocus.PPM, 7/HocusPocus.PPM);
		fdef.isSensor = true;
		fdef.filter.categoryBits = HocusPocus.CRYSTAL_BIT;
		fdef.filter.maskBits = HocusPocus.HOCUS_BIT;
		fdef.shape = shape;
		body.createFixture(fdef).setUserData(this);
	}
	
	
	public void update(float dt) {
		
		if(setToDestroy){
			world.destroyBody(body);
			setToDestroy = false;
			destroyed = true;
		}
	}
	
	
	public void render(SpriteBatch sb) {
		
		sb.begin();
		this.draw(sb);
		sb.end();
	}
	
	public void destroy(){
		
		setToDestroy = true;
	}
	
	
	public boolean isDestroyed(){
		
		return destroyed;
	}
	
	
	
	
	
	
	
	
}

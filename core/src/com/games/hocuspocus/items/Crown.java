package com.games.hocuspocus.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.games.hocuspocus.HocusPocus;

public class Crown extends Treasure{

	private static final int SCORE = 1000;
	
	
	public Crown(World world, float x, float y){
		this.world = world;
		bdef.position.set(x/HocusPocus.PPM,y/HocusPocus.PPM);
		defineBody();
		
		region = new TextureRegion(texture,16,32,16,10);
		setRegion(region);
		setBounds(0, 0, 16/HocusPocus.PPM, 10/HocusPocus.PPM);
		
		setPosition(body.getPosition().x - getWidth()/2, 
				body.getPosition().y - getHeight()/2);
	}

	
	@Override
	public void defineBody() {
		
		bdef.type = BodyType.StaticBody;
		body = world.createBody(bdef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(8/HocusPocus.PPM, 4/HocusPocus.PPM);
		fdef.isSensor = true;
		fdef.shape = shape;
		body.createFixture(fdef).setUserData(this);
		
	}
	
	
	public int getScore(){
		return SCORE;
	}
	

	@Override
	public void update(float dt) {
		
		if(setToDestroy){
			world.destroyBody(body);
			setToDestroy = false;
			destroyed = true;
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		
		sb.begin();
		this.draw(sb);
		sb.end();
	}



}

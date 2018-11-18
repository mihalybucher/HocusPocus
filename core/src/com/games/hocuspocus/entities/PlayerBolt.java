package com.games.hocuspocus.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.games.hocuspocus.HocusPocus;

public class PlayerBolt extends Bolt{
	
	
	public PlayerBolt(World world, float x, float y){
		
		velocity = new Vector2(1.5f,0);
		texture = new Texture("images/hocus.png");
		region = new TextureRegion(texture,261,0,18,12);
		setBounds(0, 0, 18/HocusPocus.PPM, 12/HocusPocus.PPM);
		setRegion(region);
		
		
		
		
		
		this.world = world;
		bdef.position.set(x,y);
		defineBody();
		
		
	}

	@Override
	public void defineBody() {
		
		bdef.type = BodyDef.BodyType.KinematicBody;
		body = world.createBody(bdef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(8/HocusPocus.PPM, 2/HocusPocus.PPM);
		fdef.filter.categoryBits = HocusPocus.PLAYER_BOLT_BIT;
		fdef.filter.maskBits = HocusPocus.WALL_BIT | HocusPocus.ENEMY_BIT;
		fdef.shape = shape;
		
		body.createFixture(fdef).setUserData(this);
		
	}

	@Override
	public void update(float dt) {
		
		body.setLinearVelocity(velocity);
		
		setPosition(body.getPosition().x - getWidth()/2, body.getPosition().y - 1/HocusPocus.PPM);
		
		
	}

	@Override
	public void render(SpriteBatch sb) {
		
		sb.begin();
		this.draw(sb);
		sb.end();
		
	}

}

package com.games.hocuspocus.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Bolt extends Sprite{
	
	//texture
	protected Texture texture;
	protected TextureRegion region;
	
	//velocity
	protected Vector2 velocity;
	
	//box2d
	protected World world;
	protected Body body;
	protected BodyDef bdef = new BodyDef();
	protected FixtureDef fdef = new FixtureDef();
	protected Shape shape;
	
	public abstract void defineBody();
	public abstract void update(float dt);
	public abstract void render(SpriteBatch sb);
	
	public void reverseVelocity(boolean x, boolean y){
		
		if(x){velocity.x = - velocity.x;}
		if(y){velocity.y = - velocity.y;}
	}
	
	
	

}

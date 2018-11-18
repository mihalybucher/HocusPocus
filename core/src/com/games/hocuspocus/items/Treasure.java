package com.games.hocuspocus.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.games.hocuspocus.HocusPocus;

public abstract class Treasure extends Sprite{

	protected Texture texture;
	
	protected World world;
	protected Body body;
	protected BodyDef bdef;
	protected FixtureDef fdef;
	protected TextureRegion region;
	protected boolean setToDestroy;
	protected boolean destroyed;
	
	
	public Treasure(){
		
		bdef = new BodyDef();
		fdef = new FixtureDef();
		texture = new Texture("images/treasures.png");
		fdef.filter.categoryBits = HocusPocus.TREASURE_BIT;
		fdef.filter.maskBits = HocusPocus.HOCUS_BIT;
		setToDestroy = false;
		
	}
	
	
	public abstract void defineBody();
	public abstract void update(float dt);
	public abstract void render(SpriteBatch sb);
	public abstract int getScore();
	
	
	public void destroy(){
		
		setToDestroy = true;
	}
	
	
	
	public boolean isDestroyed(){
		return destroyed;
	}
}

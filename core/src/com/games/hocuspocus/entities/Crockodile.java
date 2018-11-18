package com.games.hocuspocus.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.games.hocuspocus.HocusPocus;



public class Crockodile extends Enemy{
	
	
	
	//enum
	public enum State {STANDING, RUNNING, SHOOTING}
	public State currentState;
	public State previousState;
	
	//textures and animations
	private TextureRegion crockStanding;
	private Animation<TextureRegion> crockRunning;
	private Animation<TextureRegion> crockShooting;
	private float stateTimer;
	
	
	public Crockodile(World world, float x, float y){
		
		velocity = new Vector2(0.5f,0);
		
		texture = new Texture("images/crock.png");
		crockStanding = new TextureRegion(texture,0,0,28,24);
		setBounds(0, 0, 28/HocusPocus.PPM, 24/HocusPocus.PPM);
		setRegion(crockStanding);
		
		
		
		this.world = world;
		bdef.position.set(x/HocusPocus.PPM,y/HocusPocus.PPM);
		defineBody();
		
		
		//states
		currentState = State.STANDING;
		previousState = State.STANDING;
		stateTimer = 0;
		runningRight = true;
		
		
		
		//frames
		Array<TextureRegion> frames = new Array<TextureRegion>();
		
			frames.add(new TextureRegion(texture,28,0,29,24));
			frames.add(new TextureRegion(texture,57,0,26,24));
			frames.add(new TextureRegion(texture,83,0,28,24));
		
		
		crockRunning = new Animation<TextureRegion>(0.1f,frames);
		frames.clear();
		
		for(int i=4;i<6;i++){
			frames.add(new TextureRegion(texture,i*28,0,28,24));
		}
		
		crockShooting = new Animation<TextureRegion>(0.1f,frames);
		frames.clear();
		
	}
	
	

	@Override
	public void defineBody() {
		
		bdef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bdef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(7/HocusPocus.PPM, 10/HocusPocus.PPM);
		fdef.filter.categoryBits = HocusPocus.ENEMY_BIT;
		fdef.filter.maskBits = HocusPocus.WALL_BIT | HocusPocus.HOCUS_BIT | HocusPocus.SENSOR_BIT;
		fdef.shape = shape;
		
		body.createFixture(fdef).setUserData(this);
	}

	@Override
	public void update(float dt) {
		
		body.setLinearVelocity(velocity);
		
		setPosition(body.getPosition().x - getWidth()/2,
				body.getPosition().y - getHeight()/2 + 0.5f/HocusPocus.PPM);
		setRegion(getFrame(dt));
	}
	
	
public TextureRegion getFrame(float dt) {
		
		currentState = getState();
		TextureRegion region;
		
		switch(currentState){
		
		
		case RUNNING: region = crockRunning.getKeyFrame(stateTimer, true);
			break;
		case SHOOTING: region = crockShooting.getKeyFrame(stateTimer);
			break;
		case STANDING:
			default:
				region = crockStanding;
				break;
		}
		
		if((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
			region.flip(true, false);
			runningRight = false;
		}
		else if((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
			region.flip(true, false);
			runningRight = true;
		}
		
		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		
		return region;
	}
	
	
	public State getState() {
	
		if(body.getLinearVelocity().x != 0){
		return State.RUNNING;
		}
		return State.STANDING;
	}
	

	@Override
	public void render(SpriteBatch sb) {
		
		sb.begin();
		this.draw(sb);
		sb.end();
	}
	
	

}

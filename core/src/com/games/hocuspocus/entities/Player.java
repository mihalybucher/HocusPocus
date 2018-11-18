package com.games.hocuspocus.entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.games.hocuspocus.HocusPocus;
import com.games.hocuspocus.items.Treasure;
import com.games.hocuspocus.states.PlayState;


public class Player extends Sprite{
	
	
	//box2d variables
	public World world;
	public Body body;
	private BodyDef bdef = new BodyDef();
	private FixtureDef fdef = new FixtureDef();
	
	
	
	
	//textures
	private Texture texture;
	private TextureRegion hocus;
	
	//enum
	public enum State {FALLING, STANDING, RUNNING, JUMPING}
	public State currentState;
	public State previousState;
	
	//animation
	private Animation<TextureRegion> hocusRun;
	private TextureRegion hocusJump;
	private TextureRegion hocusFall;
	private TextureRegion hocusShoot;
	private float stateTimer;
	private float shootTimer;
	
	
	//booleans
	private boolean  runningRight;
	private boolean shooting;
	
	
	//statistics
	private int health;
	private int collectedCrystals;
	private int score;
	
	private PlayState playState;
	
	//constructor
	public Player(PlayState playState, World world, int x, int y){

		
		this.playState = playState;
		
		texture = new Texture("images/hocus.png");
		hocus = new TextureRegion(texture,0,0,24,36);
		setBounds(0, 0, 24/HocusPocus.PPM, 36/HocusPocus.PPM);
		setRegion(hocus);
		
		this.world = world;
		bdef.position.set(x/HocusPocus.PPM,y/HocusPocus.PPM);
		defineBody();
		
		//states
		currentState = State.STANDING;
		previousState = State.STANDING;
		stateTimer = 0;
		shootTimer = 0;
		runningRight = true;
		shooting = false;
		
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int i=1;i<7;i++){
			frames.add(new TextureRegion(texture,i*24,0,24,36));
		}
		
		hocusRun = new Animation<TextureRegion>(0.1f,frames);
		frames.clear();
		
		hocusJump = new TextureRegion(texture,7*24,0,24,36);
		hocusFall = new TextureRegion(texture,8*24,0,24,36);
		hocusShoot = new TextureRegion(texture,9*24,0,23,36);
		
		
		health = 100;
		collectedCrystals = 0;
		score = 0;
	}
	
	public void defineBody() {
		
		bdef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bdef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(6/HocusPocus.PPM, 14/HocusPocus.PPM);
		fdef.filter.categoryBits = HocusPocus.HOCUS_BIT;
		fdef.filter.maskBits = HocusPocus.WALL_BIT | HocusPocus.TREASURE_BIT | HocusPocus.CRYSTAL_BIT;
		fdef.shape = shape;
		
		body.createFixture(fdef).setUserData(this);
		
	}

	public void update(float dt){
		
		setPosition(body.getPosition().x - getWidth()/2 + 1/HocusPocus.PPM,
				body.getPosition().y - getHeight()/2 + 2.5f/HocusPocus.PPM);
		
		setRegion(getFrame(dt));
		
		
		
	}
	

	

	public TextureRegion getFrame(float dt) {
		
		currentState = getState();
		TextureRegion region;
		
		switch(currentState){
		
		case JUMPING: 
			
			
			
				region = hocusJump;
			
			break;
		case RUNNING: region = hocusRun.getKeyFrame(stateTimer, true);
			break;
		case FALLING: 
			
				region = hocusFall;
			
			break;
		case STANDING:
			default:
				if(!shooting)
					region = hocus;
				else 
					region = hocusShoot;
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
		
		
		
		
		if(shooting){
			shootTimer += dt;
			
		}
		
		if(shootTimer > 0.1){
			shooting = false;
			shootTimer = 0;
		}
		
		return region;
	}

	public State getState() {
		
		if(body.getLinearVelocity().y > 0){
			return State.JUMPING;
		}
		
		else if(body.getLinearVelocity().y < 0){
			return State.FALLING;
		}
		else if(body.getLinearVelocity().x != 0){
			return State.RUNNING;
		}
		
	
		return State.STANDING;
	}

	public void render(SpriteBatch sb){
		
		sb.begin();
		this.draw(sb);
		sb.end();
	}
	
	
	//player methods

	public void jump(){
		
		if(currentState != State.FALLING && previousState != State.JUMPING){
		body.applyLinearImpulse(new Vector2(0,4f), body.getWorldCenter(), true);
		}
		shooting = false;
	}

	public void moveLeft(){

		
		body.applyLinearImpulse(new Vector2(-0.1f,0), body.getWorldCenter(), true);
		
		
	}
	
	
	
	public void moveRight(){
		
		
		body.applyLinearImpulse(new Vector2(0.1f,0), body.getWorldCenter(), true);
		
	}
	
	
	public void collectTreasure(Treasure treasure){
		
		score += treasure.getScore();
	}
	
	public void collectCrystal(){
		
		collectedCrystals++;
	}
	
	
	public void shoot(){
		
		shooting = true;
		
		if(!runningRight){
			PlayerBolt bolt = new PlayerBolt(world, body.getPosition().x - getWidth(),body.getPosition().y);
			bolt.reverseVelocity(true, false);
			playState.bolts.add(bolt);
		}
		else {
			PlayerBolt bolt = new PlayerBolt(world, body.getPosition().x + getWidth(),body.getPosition().y);
			playState.bolts.add(bolt);
		}
		
		
		
		
		
		
	}
	
	
	//getters

	public int getHealth() {
		return health;
	}

	

	public int getCollectedCrystals() {
		return collectedCrystals;
	}

	

	public int getScore() {
		return score;
	}

	
	
	


	

	

	
	
	

	
	
	
}
	




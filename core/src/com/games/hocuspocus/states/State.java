package com.games.hocuspocus.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State {

	protected OrthographicCamera cam;
	protected GameStateManager gsm;
	
	
	public State(GameStateManager gsm){
		this.gsm = gsm;
		cam = new OrthographicCamera();
		
			
			
		
		
	}
	
	public abstract void handleInput();
	public abstract void update(float dt);
	public abstract void render(SpriteBatch sb);
	public abstract void dispode();
	
}

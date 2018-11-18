package com.games.hocuspocus;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.games.hocuspocus.states.GameStateManager;
import com.games.hocuspocus.states.PlayState;

public class HocusPocus extends ApplicationAdapter {
	
	//canstant
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final float PPM = 100;
	
	public static final String TITLE = "Hocus Pocus";
	
	//bits
		public static final short WALL_BIT = 1;
		public static final short HOCUS_BIT = 2;
		public static final short TREASURE_BIT = 4;
		public static final short CRYSTAL_BIT = 8;
		public static final short ENEMY_BIT = 16;
		public static final short SENSOR_BIT = 32;
		public static final short PLAYER_BOLT_BIT = 64;
		
	
	
	private GameStateManager gsm;
	
	SpriteBatch sb;
	
	
	
	
	@Override
	public void create () {
		sb = new SpriteBatch();
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		gsm.push(new PlayState(gsm));
	}

	@Override
	public void render () {
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(sb);
		
	}
	
	@Override
	public void dispose () {
		sb.dispose();
		
	}
}

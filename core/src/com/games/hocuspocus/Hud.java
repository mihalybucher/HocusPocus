package com.games.hocuspocus;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.games.hocuspocus.entities.Player;
import com.games.hocuspocus.states.PlayState;



public class Hud {

	private Texture texture;
	private TextureRegion hud;
	private TextureRegion[] numbers;
	private OrthographicCamera hudCam;
	
	private Player player;
	private PlayState playState;
	
	private int playerHealth;
	private int totalCrystal;
	private int levelNum;
	private int collectedCrystalNum;
	private int playerScore;
	
	public Hud(Player player, PlayState playState){
		
		this.player = player;
		this.playState = playState;
		
		texture = new Texture("images/hud.png");
		hud = new TextureRegion(texture,0,0,320,40);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false,HocusPocus.WIDTH/2,HocusPocus.HEIGHT/2);
		
		numbers = new TextureRegion[10];
		
		for(int i=0;i<numbers.length;i++){
			
			numbers[i] = new TextureRegion(texture,i*8,40,8,8);
			
		totalCrystal = playState.getCrystalNum();
		}
	}
	
	
	public void update(float dt){
		
		playerScore = player.getScore();
		playerHealth = player.getHealth();
		collectedCrystalNum = player.getCollectedCrystals();
		
		
	}
	
	public void render(SpriteBatch sb){
		sb.setProjectionMatrix(hudCam.combined);
		sb.begin();
		sb.draw(hud,0,0,hud.getRegionWidth()+
				(HocusPocus.WIDTH/2-hud.getRegionWidth()),hud.getRegionHeight());
		
		drawString(sb, String.valueOf(playerScore), 37, 11);
		drawString(sb,String.valueOf(playerHealth), 96, 11);
		drawString(sb, String.valueOf(collectedCrystalNum), 156, 11);
		drawString(sb, String.valueOf(totalCrystal), 172, 11);
		
		sb.end();
	}


	private void drawString(SpriteBatch sb, String s, float x, float y) {
		
		
		
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			 if(c >= '0' && c <= '9') c -= '0';
			else continue;
			sb.draw(numbers[c], x - (s.length()-1) * 4 + i * 8, y);
		}
		
	}


	public TextureRegion getHud() {
		return hud;
	}


	
	
}

package com.games.hocuspocus;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.games.hocuspocus.entities.Enemy;
import com.games.hocuspocus.entities.Player;
import com.games.hocuspocus.items.Crystal;
import com.games.hocuspocus.items.Treasure;

public class WorldContactListener implements ContactListener{

	
	
	
	@Override
	public void beginContact(Contact contact) {

		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		
		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		
		switch(cDef){
		
		case HocusPocus.TREASURE_BIT | HocusPocus.HOCUS_BIT:
			if(fixA.getFilterData().categoryBits == HocusPocus.TREASURE_BIT){
				((Player)fixB.getUserData()).collectTreasure((Treasure) fixA.getUserData());
				((Treasure) fixA.getUserData()).destroy();
			}
			
			else{
				((Player)fixA.getUserData()).collectTreasure((Treasure) fixB.getUserData());
				((Treasure) fixB.getUserData()).destroy();
			}
			
			break;
		
		case HocusPocus.CRYSTAL_BIT | HocusPocus.HOCUS_BIT:
			if(fixA.getFilterData().categoryBits == HocusPocus.CRYSTAL_BIT){
				((Player)fixB.getUserData()).collectCrystal();
				((Crystal) fixA.getUserData()).destroy();
			}
			
			else {
				((Player)fixA.getUserData()).collectCrystal();
				((Crystal) fixB.getUserData()).destroy();
			}
			
			break;
			
		case HocusPocus.ENEMY_BIT | HocusPocus.SENSOR_BIT:
			if(fixA.getFilterData().categoryBits == HocusPocus.SENSOR_BIT){
				((Enemy)fixB.getUserData()).reverseVelocity(true, false);
			}
			
			else {
				
				((Enemy)fixA.getUserData()).reverseVelocity(true, false);
			}
		
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}

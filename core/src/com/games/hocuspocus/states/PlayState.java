package com.games.hocuspocus.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.games.hocuspocus.HocusPocus;
import com.games.hocuspocus.Hud;
import com.games.hocuspocus.WorldContactListener;
import com.games.hocuspocus.entities.Bolt;
import com.games.hocuspocus.entities.Crockodile;
import com.games.hocuspocus.entities.Enemy;
import com.games.hocuspocus.entities.Player;
import com.games.hocuspocus.items.Crown;
import com.games.hocuspocus.items.Crystal;
import com.games.hocuspocus.items.Diamond;
import com.games.hocuspocus.items.Goblet;
import com.games.hocuspocus.items.Ruby;
import com.games.hocuspocus.items.Treasure;

public class PlayState extends State{
	
	
	//box2d
	private World world;
	private Box2DDebugRenderer b2dr;
	
	//arrays
	
	public Array<Treasure> treasures = new Array<Treasure>();
	public Array<Crystal> crystals = new Array<Crystal>();
	public Array<Enemy> enemies = new Array<Enemy>();
	public Array<Bolt> bolts = new Array<Bolt>();
	
	private int crystalNum;
	
	//hud
	private Hud hud;
	
	//player
	public Player player;
	
	
	
	private Viewport viewport;
	
	//map
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private float mapWidth;
	private float mapHeight;
	private OrthogonalTiledMapRenderer renderer;
	
	
	

	public PlayState(GameStateManager gsm) {
		super(gsm);

		
		//loading the map
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("levels/lvl1.tmx");
		mapWidth = (Integer) map.getProperties().get("width");
		mapHeight = (Integer) map.getProperties().get("height");
		
		
		
		renderer = new OrthogonalTiledMapRenderer(map, 1/HocusPocus.PPM);
		
		//creating box2d world and items
		world = new World(new Vector2(0,-10), true);
		b2dr = new Box2DDebugRenderer();
		world.setContactListener(new WorldContactListener());
	
		createlevel();
		createTreasures();
		createCrystals();
		createEnemies();
		createSensors();
	
		
		player = new Player(this,world,50,50);
		
		
		hud = new Hud(player, this);
		
		
		
		viewport = new FitViewport(HocusPocus.WIDTH/2/HocusPocus.PPM, HocusPocus.HEIGHT/2/HocusPocus.PPM, cam);
		viewport.apply(true);
		cam.position.set(HocusPocus.WIDTH/4/HocusPocus.PPM,HocusPocus.HEIGHT/4/HocusPocus.PPM
				-hud.getHud().getRegionHeight()/HocusPocus.PPM,0);
		
	}
	
	private void createSensors(){
		
		BodyDef bdef = new BodyDef();
		Shape shape;
		FixtureDef fdef = new FixtureDef();
		Body body;


		for(MapObject object : map.getLayers().get("sensors").getObjects()){
	
	
	Rectangle rect = ((RectangleMapObject)object).getRectangle();
	bdef.type = BodyDef.BodyType.StaticBody;
	bdef.position.set((rect.getX()+rect.getWidth()/2)/HocusPocus.PPM
			,(rect.getY()+rect.getHeight()/2)/HocusPocus.PPM);
	body = world.createBody(bdef);
	shape = new PolygonShape();
	((PolygonShape)shape).setAsBox(rect.getWidth()/2/HocusPocus.PPM, rect.getHeight()/2/HocusPocus.PPM);
	fdef.isSensor = true;
	fdef.filter.categoryBits = HocusPocus.SENSOR_BIT;
	fdef.filter.maskBits = HocusPocus.ENEMY_BIT;
	fdef.shape = shape;
	body.createFixture(fdef).setUserData("sensor");
	}
		
	}
	
	
	private void createEnemies(){
		
		for(MapObject object : map.getLayers().get("enemies").getObjects()){
			Rectangle rect = ((RectangleMapObject)object).getRectangle();
			if(object.getProperties().containsKey("Crock")){
				enemies.add(new Crockodile(world, rect.getX(), rect.getY()));
			}
			
		}
	}
	
	

	private void createCrystals() {
		for(MapObject object : map.getLayers().get("crystals").getObjects()){
			Rectangle rect = ((RectangleMapObject)object).getRectangle();
			crystals.add(new Crystal(world,rect.getX(),rect.getY()));
			crystalNum++;
		}
	}

	private void createTreasures() {
		
		for(MapObject object : map.getLayers().get("treasures").getObjects()){
			Rectangle rect = ((RectangleMapObject)object).getRectangle();
			
			if(object.getProperties().containsKey("Ruby")){
				
				treasures.add(new Ruby(world,rect.getX(),rect.getY()));
				
			}
			
			else if(object.getProperties().containsKey("Diamond")){
				
				treasures.add(new Diamond(world, rect.getX(), rect.getY()));
			}
			
			else if(object.getProperties().containsKey("Goblet")){
				
				treasures.add(new Goblet(world,rect.getX(),rect.getY()));
			}
			
			else if (object.getProperties().containsKey("Crown")){
				treasures.add(new Crown(world, rect.getX(), rect.getY()));
			}
			
			
		}
		
	}

	private void createlevel() {

		
				BodyDef bdef = new BodyDef();
				Shape shape;
				FixtureDef fdef = new FixtureDef();
				Body body;
		
		
		for(MapObject object : map.getLayers().get("wall").getObjects()){
			
			if(object instanceof RectangleMapObject){
			Rectangle rect = ((RectangleMapObject)object).getRectangle();
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX()+rect.getWidth()/2)/HocusPocus.PPM
					,(rect.getY()+rect.getHeight()/2)/HocusPocus.PPM);
			body = world.createBody(bdef);
			shape = new PolygonShape();
			((PolygonShape)shape).setAsBox(rect.getWidth()/2/HocusPocus.PPM, rect.getHeight()/2/HocusPocus.PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
			}
			
			if(object instanceof PolylineMapObject){
				shape = createPolyline((PolylineMapObject)object);
				bdef.type = BodyDef.BodyType.StaticBody;
				bdef.position.set(((PolylineMapObject) object).getPolyline().getOriginX(),
						((PolylineMapObject) object).getPolyline().getOriginY());
				body = world.createBody(bdef);
				fdef.shape = shape;
				body.createFixture(fdef);
			}
		}
	
	}

	private ChainShape createPolyline(PolylineMapObject poly) {
		float[] vertices = poly.getPolyline().getTransformedVertices();
	Vector2[] worldVertices = new Vector2[vertices.length/2];
		for(int i=0;i<worldVertices.length;i++){
			worldVertices[i] = new Vector2(vertices[i*2]/HocusPocus.PPM,vertices[i*2+1]/HocusPocus.PPM);
		}
		ChainShape cs = new ChainShape();
		cs.createChain(worldVertices);
		return cs;
	}
	
	
	public int getCrystalNum(){
		
		return crystalNum;
	}
	
	

	@Override
	public void handleInput() {
		if(Gdx.input.isKeyPressed(Keys.LEFT) && player.body.getLinearVelocity().x >= -1.3f){
			player.moveLeft();
		}
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT) && player.body.getLinearVelocity().x <= 1.3f){
			player.moveRight();
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
			player.jump();
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.ALT_LEFT)){
			player.shoot();
		}
		
		
	}

	//update
	@Override
	public void update(float dt) {
		handleInput();
		
		world.step(1/60f, 6, 2);
		player.update(dt);
		
		for(Enemy enemy : enemies){
			enemy.update(dt);
		}
		
		for(Bolt bolt : bolts){
			bolt.update(dt);
		}
		
		updateCamera();
		renderer.setView(cam);
		
		
		for(Treasure treasure : treasures){
			treasure.update(dt);
			
			
		}
		
		for(Crystal crystal : crystals){
			
			crystal.update(dt);
		}
		
		hud.update(dt);
		
	}	
		
	

	private void updateCamera() {
		
		if(player.body.getPosition().x < HocusPocus.WIDTH/4/HocusPocus.PPM){
			cam.position.x = HocusPocus.WIDTH/4/HocusPocus.PPM; 
		}
		
	else if(player.body.getPosition().x > (mapWidth + (mapWidth-90))/10){
			cam.position.x = (mapWidth + (mapWidth-90))/10;
		}
		
		
		else {
		
		cam.position.x = player.body.getPosition().x;
		}
		
		if(player.body.getPosition().y < HocusPocus.HEIGHT/4/HocusPocus.PPM
				-hud.getHud().getRegionHeight()/HocusPocus.PPM){
			cam.position.y = HocusPocus.HEIGHT/4/HocusPocus.PPM
					-hud.getHud().getRegionHeight()/HocusPocus.PPM;
		}
		
		else if(player.body.getPosition().y > mapHeight/10 + mapHeight/HocusPocus.PPM){
			
			cam.position.y = mapHeight/10 + mapHeight/HocusPocus.PPM;
		}
		
		else {
			
			cam.position.y = player.body.getPosition().y;
		}
		
		
		
		cam.update();
	}
	

	//render
	@Override
	public void render(SpriteBatch sb) {
		
		renderer.render();
	
		//b2dr.render(world, cam.combined);
		
		sb.setProjectionMatrix(cam.combined);
		
		for(Treasure treasure : treasures){
			if(!treasure.isDestroyed())
			treasure.render(sb);
		}
		
		for(Crystal crystal : crystals){
			if(!crystal.isDestroyed())
			crystal.render(sb);
		}
		
		
		
		
		player.render(sb);
		
		for(Enemy enemy : enemies){
			
			enemy.render(sb);
		}
		
		for(Bolt bolt : bolts){
			
			bolt.render(sb);
		}
			
		
		
		hud.render(sb);
		
		
		
		
		
		
		
		
		
		
	}

	


	//dispose
	@Override
	public void dispode() {
	
		
	}

}

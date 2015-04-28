package ch.bbcag.entity.levels;

import java.awt.Graphics2D;

import ch.bbcag.epix.entity.HUD;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import ch.bbcag.entity.enemies.Magican;
import ch.bbcag.entity.enemies.Plant;
import ch.bbcag.entity.enemies.ShootingPlant;
import ch.bbcag.entity.powerups.DamageUp;
import ch.bbcag.entity.powerups.Heart;
import ch.bbcag.epix.entity.Coin;
import ch.bbcag.epix.entity.Player;
import ch.bbcag.epix.entity.Powerup;
import ch.bbcag.epix.entity.User;
import ch.bbcag.epix.gamestate.GameState;
import ch.bbcag.epix.gamestate.GameStateManager;
import ch.bbcag.epix.tilemap.Background;
import ch.bbcag.epix.tilemap.TileMap;
import ch.bbcag.epix.view.GameFrame;

public class Level1State extends GameState{

	private User user;
	
	private Player player;
	private Player player2;
	
	private TileMap tilemap;
	private Background bg;
	
	private HUD hud;
	
	private ArrayList<Plant> plant;
	private ArrayList<ShootingPlant> shootingPlant;
	private ArrayList<Magican> magicans;
	
	private ArrayList<Powerup> powerups;
	
	private ArrayList<Coin> coins;
	
	public Level1State(GameStateManager gsm, User user) {
		this.gsm = gsm;	
		setUser(user);
		
		init();
	}

	public void init() {
	
		
		tilemap = new TileMap(32);
		tilemap.loadTiles("/TileSets/GroundTileSet.png");
		tilemap.loadMap("/Maps/level1.map");
		tilemap.setPosition(30, 30);
		
	
		bg = new Background("/Backgrounds/Background.png", 1);
	
		
		player = new Player(tilemap);
		player.setUsername(user.getUsername());
		player.setCoin(user.getCoin());
		player.setMaxHealth(user.getMaxHealth());
		player.setHealth(user.getMaxHealth());
		
		spawnEnemies();
		spawnPowerups();
		spawnCoins();
		
		player.setPosition(50, 40);

	}
	
	private void spawnPowerups() {
		powerups = new ArrayList<Powerup>();

		/*
		 * damageup
		 */
		DamageUp damageUp;
		Point[] damagUpPoints = new Point[] {
			new Point(350, 140)
			
		};
		for(int i = 0; i < damagUpPoints.length; i++) {
			damageUp = new DamageUp(tilemap);
			damageUp.setPosition(damagUpPoints[i].x, damagUpPoints[i].y);
			powerups.add(damageUp);
		}
		
		/*
		 * heart
		 */
		Heart heart;
		Point[] hearthPoints = new Point[] {
			new Point(400, 240),

		};
		for(int i = 0; i < hearthPoints.length; i++) {
			heart = new Heart(tilemap);
			heart.setPosition(hearthPoints[i].x, hearthPoints[i].y);
			powerups.add(heart);
		}
	}
	
	private void spawnEnemies() {
		
		/*
		 * plant
		 */
		plant = new ArrayList<Plant>();
			
		Plant s;
		Point[] plantPoints = new Point[] {
				new Point(460, 70)
		};
		for(int i = 0; i < plantPoints.length; i++) {
			s = new Plant(tilemap);
			s.setPosition(plantPoints[i].x, plantPoints[i].y);
			plant.add(s);
		}
			
		/*
		 * Magicians
		 */
		
		magicans = new ArrayList<Magican>();
		
		Magican m;
		Point[] magicanPoints = new Point[] {
				new Point(210, 240)
		};
		for(int i = 0; i < magicanPoints.length; i++) {
			m = new Magican(tilemap , player);
			m.setPosition(magicanPoints[i].x, magicanPoints[i].y);
			magicans.add(m);
		}
		
		
		/*
		 * shootingPlant
		 */
		shootingPlant = new ArrayList<ShootingPlant>();
		
		ShootingPlant p;
		Point[] shootingPlantPoints = new Point[] {
				new Point(700, 70),
				new Point(1000, 70)
			
		};
		for(int i = 0; i < shootingPlantPoints.length; i++) {
			p = new ShootingPlant(tilemap, false);
			p.setPosition(shootingPlantPoints[i].x, shootingPlantPoints[i].y);
			shootingPlant.add(p);
		}
		
	}
	
	private void spawnCoins() {
		coins = new ArrayList<Coin>();

		/*
		 * coin
		 */
		Coin coin;
		Point[] coinPoints = new Point[] {
			new Point(337, 208),
			new Point(562, 144)
			
		};
		for(int i = 0; i < coinPoints.length; i++) {
			coin = new Coin(tilemap);
			coin.setPosition(coinPoints[i].x, coinPoints[i].y);
			coins.add(coin);
		}
	}

	public void update() {

		// update player
		player.update();
		tilemap.setPosition(GameFrame.WIDTH / 4 - player.getx(), GameFrame.HEIGHT / 4 - player.gety());
		
		//update hud
		
		hud = new HUD(player);

		// set background
		bg.setPosition(tilemap.getx(), tilemap.gety());
		
		player.checkAttackPlants(plant);
		player.checkAttackShootingPlants(shootingPlant);
		player.checkPowerup(powerups);
		player.checkCoin(coins);
		
		
		for(int i = 0; i < plant.size(); i++) {
			Plant e = plant.get(i);
			e.update(e, player);
			if(e.isDead()) {
				plant.remove(i);
				i--;
			}
		}
		
		for(int i = 0; i < magicans.size(); i++) {
			Magican e = magicans.get(i);
			e.update(e, player);
			if(e.isDead()) {
				magicans.remove(i);
				i--;
			}
		}
		
		for(int i = 0; i < plant.size(); i++) {
			Plant e = plant.get(i);
			e.update(e, player);
			if(e.isDead()) {
				plant.remove(i);
				i--;
			}
		}
		
		
		for(int i = 0; i < shootingPlant.size(); i++) {
			ShootingPlant e = shootingPlant.get(i);	
			e.update(e, player);
			if(e.isDead()) {
				shootingPlant.remove(i);
				i--;
			}else {
				e.checkAttackPlayer(player);
			}
					
		}
		
		for(int i = 0; i < powerups.size(); i++) {
			Powerup e = powerups.get(i);
			e.update();
			if(e.isTaken()) {
				powerups.remove(i);
				i--;
			}
		}
		
		for(int i = 0; i < coins.size(); i++) {
			Coin coin = coins.get(i);
			coin.update();
			if(coin.isTaken()) {
				coins.remove(i);
				i--;
			}
		}
	}

	public void draw(Graphics2D g) {

		// draw bg
		bg.draw(g);

		// draw tilemap
		tilemap.draw(g);

		for(int i = 0; i < plant.size(); i++) {
			plant.get(i).draw(g);
		}
		
		for(int i = 0; i < shootingPlant.size(); i++) {
			shootingPlant.get(i).draw(g);
		}
		
		for(int i = 0; i < magicans.size(); i++) {
			magicans.get(i).draw(g);
		}
		
		
		for(int i = 0; i < powerups.size(); i++) {
			powerups.get(i).draw(g);
		}
		
		for(int i = 0; i < coins.size(); i++) {
			coins.get(i).draw(g);
		}
		
		// draw player
		player.draw(g);	
		hud.draw(g);
		
	}
	

	public void keyPressed(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_UP) player.setJumping(true);
		if(k == KeyEvent.VK_R) player.setRainbowing();
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_UP) player.setJumping(false);
		
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
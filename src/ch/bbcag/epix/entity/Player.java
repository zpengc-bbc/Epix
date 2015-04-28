package ch.bbcag.epix.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import ch.bbcag.entity.enemies.Plant;
import ch.bbcag.entity.enemies.PlantShot;
import ch.bbcag.entity.enemies.ShootingPlant;
import ch.bbcag.epix.tilemap.TileMap;

public class Player extends MapObject {

	// player stuff
	private int health;
	public void setHealth(int health) {
		this.health = health;
	}
	
	private int coin;
	private String username;
	private int maxHealth;
		

	private int rainbow;
	private int maxRainbow;
	private boolean dead;
	private boolean flinching;
	
	public boolean isFlinching() {
		return flinching;
	}

	public void setFlinching(boolean flinching) {
		this.flinching = flinching;
	}

	private long flinchTimer;
	
	private ArrayList<Powerup> powerups = new ArrayList<Powerup>();

	// fireball
	private boolean rainbowing;
	private int rainbowcost;
	private int rainbowdamage;
	private ArrayList<Rainbow> rainbows ;

	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = { 1, 6, 1, 3 };
	private ArrayList<PlantShot> plantshots;

	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 2;

	private static final int RAINBOW = 3;

	public Player(TileMap tm) {

		super(tm);

		width = 32;
		height = 32;

		// hitbox
		cwidth = 20;
		cheight = 32;

		moveSpeed = 0.2;
		maxSpeed = 5.2;
		stopSpeed = 0.8;
		fallSpeed = 0.30;
		maxFallSpeed = 4.0;
		jumpStart = -11.0;
		stopJumpSpeed = 0.6;

		facingRight = true;

		health = maxHealth = 6900;
		rainbow = maxRainbow = 2500;
		
		rainbowdamage = 10;
		rainbows = new ArrayList<Rainbow>();

		// load sprites
		try {

			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Player/Player.png"));

			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < 4; i++) {

				BufferedImage[] bi = new BufferedImage[numFrames[i]];

				for (int j = 0; j < numFrames[i]; j++) {
						bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);

				}

				sprites.add(bi);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(100);

	}
	
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}


	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getRainbow() {
		return rainbow;
	}

	public int getMaxRainbow() {
		return maxRainbow;
	}

	public void setRainbowing() {
		rainbowing = true;
	}

	public int getRainbowcost() {
		return rainbowcost;
	}

	public void setRainbowcost(int rainbowcost) {
		this.rainbowcost = rainbowcost;
	}

	public int getRainbowdamage() {
		return rainbowdamage;
	}

	public void setRainbowdamage(int rainbowdamage) {
		this.rainbowdamage = rainbowdamage;
	}

	public ArrayList<Rainbow> getRainbows() {
		return rainbows;
	}
	public void checkAttackPlants(ArrayList<Plant> plants) {
		// loop through enemies 
		for (int i = 0; i < plants.size(); i++) {

			Plant e = plants.get(i);

			// fireballs
			for (int j = 0; j < rainbows.size(); j++) {
				if (rainbows.get(j).intersects(e)) {
					e.hit(rainbowdamage);
					rainbows.get(j).setHit();
					break;
				}
			}
			// check enemy collision
			if (intersects(e)) {
				hit(e.getDamage());
			}
		}
	}
	
	public void checkAttackShootingPlants(ArrayList<ShootingPlant> shootingplants) {
		// loop through enemies
		for (int i = 0; i < shootingplants.size(); i++) {
			ShootingPlant e = shootingplants.get(i);
			// fireballs
			for (int j = 0; j < rainbows.size(); j++) {
				if (rainbows.get(j).intersects(e)) {
					e.hit(rainbowdamage);
					rainbows.get(j).setHit();
					break;
				}
			}
			// check enemy collision
			if (intersects(e)) {
				e.update(e, this);
				hit(e.getDamage());
			}
		}
	}
	
	public void checkPowerup(ArrayList<Powerup> powerups) {

		// loop through enemies
		for (int i = 0; i < powerups.size(); i++) {

			Powerup powerup = powerups.get(i);

			// fireballs
			
			// check enemy collision
			if (intersects(powerup)) {
				powerup.update();
				addPowerupToPlayer(powerup);
				
				
				
				
			}

		}
	}
	
	public void addPowerupToPlayer(Powerup powerup){
		setHealth(getHealth() + powerup.plusHealth);
		setRainbowdamage(getRainbowdamage() + powerup.plusDamage);
		powerups.add(powerup);
		powerup.setTaken(true);
		powerup.setAvailable(true);
		powerup.setTakenTime(System.currentTimeMillis());
	}
	
	public void removePowerupFromPlayer(Powerup powerup){
		setHealth(getHealth() - powerup.plusHealth);
		setRainbowdamage(getRainbowdamage() - powerup.plusDamage);
		System.out.println(getHealth());
		System.out.println(getRainbowdamage());
		powerups.remove(powerup);
		System.out.println("Powerup removed");
		
	}
	
	
	
	public void hit(int damage) {
		if (flinching)
			return;
		health -= damage;
		if (health < 0)
			health = 0;
		if (health == 0)
			dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	private void getNextPosition() {

		// movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		} else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0) {
					dx = 0;
				}
			}
		}

		// cannot move while attacking, except in air
		if ((currentAction == RAINBOW) && !(jumping || falling)) {
			dx = 0;
		}

		// jumping
		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		// falling
		if (falling) {
			if (dy > 0)
				dy += fallSpeed * 1;
			else dy += fallSpeed;

			if (dy > 0)
				jumping = false;
			if (dy < 0 && !jumping)
				dy += stopJumpSpeed;

			if (dy > maxFallSpeed)
				dy = maxFallSpeed;

		}

	}

	public void update() {
		
		


		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);	

		if (powerups.size() != 0) {			
			for(int i = 0; i < powerups.size(); i++) {
				Powerup powerup = powerups.get(i);
				if(powerup.getExpireTime() > 0 && powerup.getTakenTime() + powerup.getExpireTime() <= System.currentTimeMillis()){
					removePowerupFromPlayer(powerup);
					
				}
			}
		}
		
		
		// check attack has stopped
		if (currentAction == RAINBOW) {
			if (animation.hasPlayedOnce())
				rainbowing = false;
		}
		// fireball attack
		if (rainbowing && currentAction != RAINBOW) {
			Rainbow fb = new Rainbow(tileMap, facingRight);
			fb.setPosition(x, y);
			rainbows.add(fb);
		}
		// update fireballs
		for (int i = 0; i < rainbows.size(); i++) {
			rainbows.get(i).update();
			if (rainbows.get(i).shouldRemove()) {
				rainbows.remove(i);
				i--;
			}
		}

		// check done flinching
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 1000) {
				flinching = false;
			}
		}

		// set animation
		else if (rainbowing) {
			if (currentAction != RAINBOW) {
				currentAction = RAINBOW;
				animation.setFrames(sprites.get(RAINBOW));
				animation.setDelay(100);
				width = 32;
			}
		} else if (dy > 0) {
			if (currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-4);
				width = 32;
			}
		}

		else if (dy < 0) {
			if (currentAction != FALLING) {
				currentAction = FALLING;
				
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 32;
			}
		}

		else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(60);
				width = 32;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 32;
			}
		}

		animation.update();

		// set direction
		if (currentAction != RAINBOW) {
			if (right)
				facingRight = true;
			if (left)
				facingRight = false;
		}

	}

	public void draw(Graphics2D g) {

		setMapPosition();

		// draw fireballs
		for (int i = 0; i < rainbows.size(); i++) {
			rainbows.get(i).draw(g);
		}
		// draw player
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0) {
				return;
			}
		}

		super.draw(g);

	}

	public ArrayList<Powerup> getPowerups() {
		return powerups;
	}

	public void setPowerups(ArrayList<Powerup> powerups) {
		this.powerups = powerups;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}
}

package ch.bbcag.epix.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import ch.bbcag.epix.entity.Animation;
import ch.bbcag.epix.entity.Enemy;
import ch.bbcag.epix.entity.Player;
import ch.bbcag.epix.tilemap.TileMap;

/**
 * Schiessende Pflanze
 * @author Miguel Jorge, Penglerd Chiramet Phong || ICT Berufsbildungs AG
 *          Copyright Berufsbildungscenter 2015
 */
public class ShootingPlant extends Enemy {

	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = { 4, 6 };

	private static final int IDLE = 0;
	private static final int SHOOT = 1;
	private boolean onscreen;
	private boolean shotright;
	private long timer;
	private long time = 1000; // animation delay * anzahl sprites
	private int plantshotsdamage = 10;

	private ArrayList<PlantShot> plantshots;
	private int range = 112;

	/**
	 * Konstruktor
	 * 
	 * @param tm {@link TileMap}
	 * @param b
	 */
	public ShootingPlant(TileMap tm, boolean b) {
		super(tm);

		fallSpeed = 1;
		maxFallSpeed = 1.1;

		width = 32;
		height = 32;
		cwidth = 32;
		cheight = 32;

		setRange(224);
		timer = System.currentTimeMillis();
		health = maxHealth = 20;
		damage = 10;
		// load sprites

		setPlantshots(new ArrayList<PlantShot>());

		/*
		 * Bilder laden
		 */
		try {
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Enemies/PlantShooting.png"));
			this.setSprites(new ArrayList<BufferedImage[]>());
			for (int i = 0; i < 2; i++) {

				BufferedImage[] bi = new BufferedImage[getNumFrames()[i]];

				for (int j = 0; j < getNumFrames()[i]; j++) {

					if (i != getShoot()) {
						bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
					} else {
						bi[j] = spritesheet.getSubimage(j * width * 2, i * height, width * 2, height);
					}

				}

				this.getSprites().add(bi);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		currentAction = getIdle();
		animation.setFrames(getSprites().get(currentAction));
		animation.setDelay(100);
		width = 32;

	}

	/**
	 * Bewegung
	 */
	private void getNextPosition() {
		// falling
		if (falling) {
			dy += fallSpeed;
		}

	}

	/**
	 * Ob die Pflanze den gegner attackiert
	 * 
	 * @param playerHit {@link Player}
	 * @param playerHealth {@link Player}
	 */	
	public void checkAttackPlayer(Player playerHit, Player playerHealth){
		for (int j = 0; j < getPlantshots().size(); j++) {
			if (getPlantshots().get(j).intersects(playerHit)) {
				playerHit.hit(damage, playerHealth);
				getPlantshots().get(j).setHit();
				break;
			}
		}
	}

	/**
	 * Update
	 * @param e {@link ShootingPlant}
	 * @param player {@link Player}
	 */
	public void update(ShootingPlant e, Player player) {

		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		for (int i = 0; i < getPlantshots().size(); i++) {
			getPlantshots().get(i).update(player, e);
			if (getPlantshots().get(i).isRemove()) {
				getPlantshots().remove(i);
				i--;
			}
		}
		// check flinching
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 400) {
				flinching = false;
			}
		} else if (OnScreen(e, getRange())) {
			if (currentAction != getShoot()) {
				currentAction = getShoot();
				animation.setFrames(getSprites().get(getShoot()));
				animation.setDelay(250);
				width = 64;
				cwidth = 40;
			}
		} else {
			if (currentAction != getIdle()) {
				currentAction = getIdle();
				animation.setFrames(getSprites().get(getIdle()));
				animation.setDelay(400);
				width = 32;
				cwidth = 32;

			}
		}

		if (currentAction == getShoot()) {
			if (facingRight) {
				setShotright(true);
			} else {
				setShotright(false);
			}
			if (animation.getFrame() == 3) {
				PlantShot ps = new PlantShot(tileMap, isShotright(), player);
				ps.setPosition(e.getx(), e.gety());
				if (getTimer() + getTime() <= System.currentTimeMillis()) {
					getPlantshots().add(ps);
					setTimer(System.currentTimeMillis());

				}
			}
		}

		animation.update();

		if (true) {
			if (e.getx() > player.getx()) {
				facingRight = true;
			} else {
				facingRight = false;
			}
		}
	}

	/**
	 * Schaut ob der Spieler die Pflanze auf dem Bildschirm sieht
	 * 
	 * @param e {@link ShootingPlant}
	 * @param range
	 * @return ob der Spieler die Plfanze auf dem Bildschirm sieht
	 */
	public boolean OnScreen(ShootingPlant e, int range) {
		double a = e.getXmap();
		double spielerkoordinaten = (a - a - a) + range;
		if (e.getx() + range > spielerkoordinaten && e.getx() - spielerkoordinaten < range) {
			return true;
		} else {
			return false;
		}
	}

	
	public void draw(Graphics2D g) {

		// if(notOnScreen()) return;

		setMapPosition();

		for (int i = 0; i < getPlantshots().size(); i++) {
			getPlantshots().get(i).draw(g);
		}

		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed / 100 % 2 == 0) {
				return;
			}
		}

		// draw Shot
		if (facingRight) {
			g.drawImage(animation.getImage(), (int) (x - (width - cwidth) + xmap - width / 2), (int) (y + ymap - height / 2), null);

		} else {
			g.drawImage(animation.getImage(), (int) (x + (width - cwidth) + xmap - width / 2 + width), (int) (y + ymap - height / 2), -width, height, null);

		}
	}
	

	/*
	 * Getter und Setter
	 */
	public boolean isOnscreen() {
		return onscreen;
	}

	public void setOnscreen(boolean onscreen) {
		this.onscreen = onscreen;
	}

	public static final int getIdle() {
		return IDLE;
	}

	public static final int getShoot() {
		return SHOOT;
	}

	public ArrayList<PlantShot> getPlantshots() {
		return plantshots;
	}

	public void setPlantshots(ArrayList<PlantShot> plantshots) {
		this.plantshots = plantshots;
	}

	public long getTimer() {
		return timer;
	}

	public void setTimer(long timer) {
		this.timer = timer;
	}

	public boolean isShotright() {
		return shotright;
	}

	public void setShotright(boolean shotright) {
		this.shotright = shotright;
	}

	public ArrayList<BufferedImage[]> getSprites() {
		return sprites;
	}

	public final int[] getNumFrames() {
		return numFrames;
	}

	public long getTime() {
		return time;
	}

	public int getPlantshotsdamage() {
		return plantshotsdamage;
	}

	public int getRange() {
		return range;
	}

	public void setSprites(ArrayList<BufferedImage[]> sprites) {
		this.sprites = sprites;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setPlantshotsdamage(int plantshotsdamage) {
		this.plantshotsdamage = plantshotsdamage;
	}

	public void setRange(int range) {
		this.range = range;
	}
}

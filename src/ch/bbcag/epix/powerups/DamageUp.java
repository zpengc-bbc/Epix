package ch.bbcag.epix.powerups;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import ch.bbcag.epix.entity.Animation;
import ch.bbcag.epix.entity.Powerup;
import ch.bbcag.epix.tilemap.TileMap;

/**
 * Powerup DamageUp, fuer eine gewisse Zeit hat der Spieler mehr schaden
 * @author  Miguel Jorge, Penglerd Chiramet Phong || ICT Berufsbildungs AG
 *			Copyright Berufsbildungscenter 2015
 */
public class DamageUp extends Powerup {

	private BufferedImage[] sprites;
	
	/*
	 * DamageUp wird herstellt
	 * - Verbesserungen werden festgelegt
	 * - Animation wird festgelegt
	 */
	
	
	/**
	 * Konstruktor
	 * @param tm {@link TileMap}
	 */
	public DamageUp(TileMap tm) {
		super(tm);
		
		plusDamage = 20;
		expireTime = 10000;
		
		width = 32;
		height = 32;
		cwidth = 32;
		cheight = 32;
		
		try {

			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Powerups/PowerUps_Weapon Up.png"));

			sprites = new BufferedImage[4];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(100);

		right = true;
		facingRight = true;
	}
	
	
	public void update() {

		checkTileMapCollision();

		// update animation
		animation.update();
	}
	
	
	public void draw(Graphics2D g) {

		setMapPosition();

		super.draw(g);
	}
}

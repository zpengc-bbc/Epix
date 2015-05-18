package ch.bbcag.epix.powerups;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import ch.bbcag.epix.entity.Animation;
import ch.bbcag.epix.entity.Powerup;
import ch.bbcag.epix.tilemap.TileMap;

/**
 * 
 * @author  Miguel Jorge, Penglerd Chiramet Phong || ICT Berufsbildungs AG
 *			Jetpack.java.java Copyright Berufsbildungscenter 2015
 */

public class Jetpack extends Powerup {

	private BufferedImage[] sprites;
	
	/*
	 * Jepack wird herstellt
	 * - Verbesserungen werden festgelegt
	 * - Animation wird geladen
	 */
	
	public Jetpack(TileMap tm) {
		super(tm);		
		
		jetpack = true;
		plusMoveSpeed = 0;
		plusMaxSpeed = 0;
		plusStopSpeed = 0;
		plusFallSpeed = 0;
		plusMaxFallSpeed = 0;
		plusJumpStart = 0;
		plusStopJumpSpeed = 0;
		
		expireTime = 10000;
		
		width = 32;
		height = 32;
		cwidth = 32;
		cheight = 32;
		
		try {

			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Powerups/PowerUps_Jet Pack.png"));

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
	
	/*
	 * Animation wird aktualisiert
	 */
	public void update() {

		checkTileMapCollision();
		animation.update();

	}
	
	public void draw(Graphics2D g) {

		setMapPosition();

		super.draw(g);

	}

}

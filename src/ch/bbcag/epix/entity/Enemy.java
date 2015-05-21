package ch.bbcag.epix.entity;

import ch.bbcag.epix.tilemap.TileMap;

/**
 * 
 * @author  Miguel Jorge, Penglerd Chiramet Phong || ICT Berufsbildungs AG
 *			Enemy.java.java Copyright Berufsbildungscenter 2015
 */

public class Enemy extends MapObject {

	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;

	protected boolean flinching;
	protected long flinchTimer;

	public Enemy(TileMap tm) {
		super(tm);
	}

	public boolean isDead() {
		return dead;
	}

	public int getDamage() {
		return damage;
	}
	public void hit(int damage) {
		if (dead || flinching)
			return;
		health -= damage;
		if (health < 0){
			health = 0;
		}
		
		if (health == 0) {
			dead = true;
		}
			
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	public void hitPlayer(int damage,Player player) {
		if (!player.isShield()) {
			if (flinching) {
				return;
			}
			player.setHealth(player.getHealth() - damage);
			if (player.getHealth() < 0) {
				player.setHealth(0);
			}
			if (player.getHealth() == 0) {
				player.setDead(true);
			}
			player.setFlinching(true);
			player.setFlinchTimer(System.nanoTime());
		}
	}
}

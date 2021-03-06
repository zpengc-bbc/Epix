package ch.bbcag.epix.gamestate;

import java.awt.Graphics2D;

import ch.bbcag.epix.controller.EpixController;
import ch.bbcag.epix.entity.User;
import ch.bbcag.epix.levels.BossState;
import ch.bbcag.epix.levels.Level1State;
import ch.bbcag.epix.levels.Level2State;

/**
 * GamestateManager
 * @author Miguel Jorge, Penglerd Chiramet Phong || ICT Berufsbildungs AG
 *         Copyright Berufsbildungscenter 2015
 */

public class GameStateManager {

	private GameState[] gameStates;
	private int currentLevel;

	private boolean finished;
	private boolean dead;

	private User user;

	public static final int NUMLEVELS = 4;

	public static final int LEVEL1 = 1;
	public static final int LEVEL2 = 2;
	public static final int BOSSLEVEL = 3;

	
	/**
	 * Konstruktor
	 * @param level 
	 * @param user {@link User}
	 */
	public GameStateManager(int level, User user) {

		this.user = user;	

		setGameStates(new GameState[NUMLEVELS]);

		this.setCurrentLevel(level);
		loadState(getCurrentLevel(), user);

	}

	
	/**
	 * Level laden
	 * @param level
	 * @param user {@link User}
	 */
	private void loadState(int level, User user) {
		if (level == LEVEL1) {
			getGameStates()[level] = new Level1State(this, user);
		} else if (level == LEVEL2) {
			getGameStates()[level] = new Level2State(this, user);
		} else if (level == BOSSLEVEL) {
			getGameStates()[level] = new BossState(this, user);
		}
		EpixController.getInstance().saveUpgrades(user);
	}

	
	/**
	 * Level entladen
	 * @param level
	 */
	private void unloadState(int level) {
		getGameStates()[level] = null;
	}
	
	
	/**
	 * Level neustarten
	 */
	public void restartState() {
		unloadState(currentLevel);
		loadState(currentLevel, user);
		setFinished(false);
		setDead(false);
	}

	
	/**
	 * Level setten
	 * @param level
	 * @param user {@link User}
	 */
	public void setState(int level, User user) {
		unloadState(currentLevel);
		currentLevel = level;
		loadState(currentLevel, user);
		// gameStates[currentState].init();
	}
	
	
	/**
	 * Musik stoppen
	 */
	public void stopMusic(){
		getGameStates()[currentLevel].backgroundMusic.stop();
		getGameStates()[currentLevel].backgroundMusic.close();
	}
	
	
	/**
	 * Speichern
	 */
	public void saveState() {
		EpixController.getInstance().coinsUpdate(getGameStates()[currentLevel].player.getUser(), getGameStates()[currentLevel].player.getCoin());
		EpixController.getInstance().collectedCoinsUpdate(getGameStates()[currentLevel].player.getUser(), getGameStates()[currentLevel].player.getCollectedCoin());	
		int level_ID = EpixController.getInstance().getID_Level(getGameStates()[currentLevel].levelName);
		EpixController.getInstance().save(user, level_ID);
		user.setCoin(getGameStates()[currentLevel].player.getCoin());
		System.out.println(getGameStates()[currentLevel].player.getCollectedCoin());
		user.setCollectedCoin(getGameStates()[currentLevel].player.getCollectedCoin());
	}

	
	/**
	 * Update
	 */
	public void update() {
		try {
			getGameStates()[currentLevel].update();

			// restart if player dead
			if (getGameStates()[currentLevel].player.isDead()) {
				setDead(true);
			}

			// if finished update coin in database
			if (getGameStates()[currentLevel].finished) {
				this.setFinished(true);

			}

		} catch (Exception e) {
			
		}
	}

	
	/**
	 * Draw
	 * @param g {@link Graphics2D}
	 */
	public void draw(Graphics2D g) {
		try {
			getGameStates()[currentLevel].draw(g);
		} catch (Exception e) {
		
		}
	}

	
	public void keyPressed(int k) {
		getGameStates()[currentLevel].keyPressed(k);
	}

	public void keyReleased(int k) {
		getGameStates()[currentLevel].keyReleased(k);
	}

	
	/*
	 * Getter und Setter
	 */
	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public GameState[] getGameStates() {
		return gameStates;
	}

	public void setGameStates(GameState[] gameStates) {
		this.gameStates = gameStates;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

}

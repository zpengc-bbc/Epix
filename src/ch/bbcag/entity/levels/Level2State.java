package ch.bbcag.entity.levels;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import ch.bbcag.epix.entity.Player;
import ch.bbcag.epix.gamestate.GameState;
import ch.bbcag.epix.gamestate.GameStateManager;
import ch.bbcag.epix.tilemap.Background;
import ch.bbcag.epix.tilemap.TileMap;
import ch.bbcag.epix.view.GameFrame;

/**
 * 
 * @author Chiramet Phong Penglerd, ICT Berufsbildungscenter AG, chirametphong.penglerd@bbcag.ch
 *
 */

public class Level2State extends GameState{

	private TileMap tilemap;
	private Player player;
	private Background bg;
	
	public Level2State(GameStateManager gsm) {
		this.gsm = gsm;	
		init();
	}

	public void init() {
	
		tilemap = new TileMap(16);
		tilemap.loadTiles("/TileSets/GroundTileSet.png");
		tilemap.loadMap("/Maps/level2.map");
		tilemap.setPosition(30, 30);
		
		
		bg = new Background("/Backgrounds/Background.png", 1);
		
		player = new Player(tilemap);
		
		player.setPosition(28, 28);

	}

	public void update() {

		// update player
		player.update();
		tilemap.setPosition(GameFrame.WIDTH / 2 - player.getx(), GameFrame.HEIGHT / 2 - player.gety());

		// set background
		bg.setPosition(tilemap.getx(), tilemap.gety());
		

	}

	public void draw(Graphics2D g) {

		// draw bg
		bg.draw(g);

		// draw tilemap
		tilemap.draw(g);

		// draw player
		player.draw(g);	
		
		
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
}
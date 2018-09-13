import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import java.util.ArrayList;
import java.util.Iterator;

public class World {
    // variables for tiles and player
	private Image grass;
	private Image water;
    private Player playerSprite;
    // arraylist to store each obstacle
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    // constant variables
	public static final int SPRITE_WIDTH = 48;
	// speed of the buses, units per ms
	public static final float OBSTACLE_SPEED = 0.15f;
	// number of obstacle rows
    public static final int NUM_ROWS = 5;
    // tile constants
    public static final int WATER_Y_START = 48, WATER_Y_END = 336, GRASS_ROW_1= 384, GRASS_ROW_2 = 672;
    // obstacle constants
    public static final int ROW_1_OFFSET = 48, ROW_2_OFFSET = 0, ROW_3_OFFSET = 64, ROW_4_OFFSET = 128, ROW_5_OFFSET = 250;
    public static final float ROW_1_SEPARATION = 6.5f, ROW_2_SEPARATION = 5, ROW_3_SEPARATION = 12, ROW_4_SEPARATION = 5, ROW_5_SEPARATION = 6.5f;
    public static final int ROW_1_Y_POSITION = 432, ROW_2_Y_POSITION = 480, ROW_3_Y_POSITION = 528, ROW_4_Y_POSITION = 576, ROW_5_Y_POSITION = 624;
    // array for the number of obstacles per row, increment 1 to account for index
    private int[] rowCount = new int[NUM_ROWS+1];

	public World() throws SlickException {
        // perform initialisation logic
	    // player position constants
	    final int PLAYER_X_POSITION = 512 - (SPRITE_WIDTH/2);
        final int PLAYER_Y_POSITION = 720 - (SPRITE_WIDTH/2);
        // set image sources
		grass = new Image("assets/grass.png");
		water = new Image("assets/water.png");
		// create new instance of player sprite
		playerSprite = new Player("assets/frog.png", PLAYER_X_POSITION, PLAYER_Y_POSITION);
        // create new instances of obstacles
        createObstacleRow(ROW_1_OFFSET, ROW_1_SEPARATION, ROW_1_Y_POSITION, 0, "right");
        createObstacleRow(ROW_2_OFFSET, ROW_2_SEPARATION, ROW_2_Y_POSITION, 1, "left");
        createObstacleRow(ROW_3_OFFSET, ROW_3_SEPARATION, ROW_3_Y_POSITION, 2, "right");
        createObstacleRow(ROW_4_OFFSET, ROW_4_SEPARATION, ROW_4_Y_POSITION, 3, "left");
        createObstacleRow(ROW_5_OFFSET, ROW_5_SEPARATION, ROW_5_Y_POSITION, 4, "right");

	}
	public void createObstacleRow(int offset, double separation, int y, int rowNumber, String direction) throws SlickException {
	    Obstacle bus;
	    // iterate until at the end of the screen
	    for (int x = offset; x < App.SCREEN_WIDTH; x+=separation*SPRITE_WIDTH) {
	        // New instance of bus sprite
            bus = new Obstacle("assets/bus.png", x, y - ((float)SPRITE_WIDTH/2), direction);
            // Add object/instance to array list
            obstacles.add(bus);
            // increment the number of objects created for this row
            rowCount[rowNumber] ++;
        }
        // set the next index
        rowCount[rowNumber+1] = rowCount[rowNumber];
    }
	
	public void update(Input input, int delta) {
	    // idea for the iterator/arraylist sourced from http://slick.ninjacave.com/forum/viewtopic.php?f=3&t=7064

		// update all of the sprites in the game
        // create an iterator object for the obstacles array list
        Iterator<Obstacle> itr = obstacles.iterator();
        // loop through the list until at the end
        while(itr.hasNext()) {
            // create local object
            Obstacle currentBus = itr.next();
            // collision detection, checks if the current bus is the array iterator is colliding with the player
            if (currentBus.getBB().intersects(playerSprite.getBB())) {
                // call the method in the sprite class
                currentBus.contactSprite(playerSprite);
            }
            // call the update method for each instance
            currentBus.update(input, delta);
        }
        // call the update method for the player instance
        playerSprite.update(input, delta);
	}
	
	public void render(Graphics g) {
	    // draw the tiles
	    drawTiles();
		// draw the player
        playerSprite.render();
		// draw each of the obstacles
		for (int x = 0; x < NUM_ROWS; x++) {
            drawObstacleRow(x);
        }
	}

	public void drawTiles() {
        // draw all of the sprites in the game
        // draw the grass tiles, iterate across the x-axis until at the end of the screen
        for (int x = 0 - (SPRITE_WIDTH/2); x < App.SCREEN_WIDTH; x+=SPRITE_WIDTH) {
            grass.draw(x, GRASS_ROW_1 - ((float)SPRITE_WIDTH/2));
            grass.draw(x, GRASS_ROW_2 - ((float)SPRITE_WIDTH/2));
        }
        // draw the water tiles, iterate across the x-axis for multiple y values until at the end of the screen
        for (int x = 0 - (SPRITE_WIDTH/2); x < App.SCREEN_WIDTH; x+=SPRITE_WIDTH) {
            for (int y = WATER_Y_START + (SPRITE_WIDTH/2); y <= WATER_Y_END - (SPRITE_WIDTH/2); y+=SPRITE_WIDTH) {
                water.draw(x, y);
            }
        }
    }

	public void drawObstacleRow(int rowNumber) {
	    // draws each of the obstacles supplied by the array list
	    if (rowNumber==0) {
	        // if this is the first row, start at index 0
            for (int x = 0; x < rowCount[rowNumber]; x++) {
                obstacles.get(x).render();
            }
        } else {
	        // else continue from the previous row count index
            for (int x = rowCount[rowNumber-1]; x < rowCount[rowNumber]; x++) {
                obstacles.get(x).render();
            }
        }
    }
}

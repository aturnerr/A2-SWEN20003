import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import utilities.BoundingBox;

import java.io.*;
import java.nio.Buffer;
import java.util.*;

/**
 * World class directs each of the game elements and their interactions
 *
 * @author Adam Turner 910935
 *
 */
public class World {
    // variables for tiles and player
	private Image life = new Image("assets/lives.png");;
    private Player playerSprite;
    private Sprite extraLife;
    private Random rand;
    // arrayList to store obstacles, tiles and holes at top of screen
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private ArrayList<Sprite> tiles = new ArrayList<>();
    private ArrayList<Hole> holes = new ArrayList<>();
    // constant variables
	public static final int SPRITE_WIDTH = 48;
    public static final int TO_MILLISECONDS = 1000;

    public static final int PLAYER_X_POSITION = 512;
    public static final int PLAYER_Y_POSITION = 720;

    public static final int EXTRALIFE_TIME_MAX = 35;
    public static final int EXTRALIFE_TIME_MIN = 25;
    public static final int EXTRALIFE_MOVE_OFFSET = 2;
    public static final int EXTRALIFE_MAX_LIFETIME = 14;

    public static final int NUM_TILE_ARGUMENTS = 3;
    public static final int NUM_OBSTACLE_ARGUMENTS = 4;

    public static final int HOLE_OFFSET = 120;
    public static final int HOLE_SEPARATION = 192;

    public static final int TURTLE_VISIBLE_TIME = 7;
    public static final int TURTLE_UNDERWATER_TIME = 2;

    public static final int LIVES_XPOS = 24;
    public static final int LIVES_YPOS = 744;
    public static final int LIVES_SEPARATION = 32;

    public static final float BUS_SPEED = 0.15f;
    public static final float RACECAR_SPEED = 0.5f;
    public static final float BIKE_SPEED = 0.2f;
    public static final float BULLDOZER_SPEED = 0.05f;
    public static final float LOG_SPEED = 0.1f;
    public static final float LONGLOG_SPEED = 0.07f;
    public static final float TURTLE_SPEED = 0.085f;
    // other variables
    private int lifeCount = 3;
    private int extraLifeTime;
    private long pastTime = 0;
    private int currentLevel = 0;

    /**
     * Perform initialisation logic
     * @throws SlickException ignore slick exception
     */
	public World() throws SlickException {
        // create new random object
        rand = new Random();
        // determine extra life time
        extraLifeTime = rand.nextInt(EXTRALIFE_TIME_MAX - EXTRALIFE_TIME_MIN + 1) + EXTRALIFE_TIME_MIN;
        // load the first level
        loadLevel(currentLevel);
	}

    /**
     * Loads level with the given index
     * @param level the level number to load
     */
	public void loadLevel(int level) {
	    // try to read the .lvl file
        try (BufferedReader br =
                     new BufferedReader(new FileReader("assets/levels/" + level + ".lvl"))) {
            String text;
            // clear any previous objects
            tiles.clear();
            obstacles.clear();
            holes.clear();
            // create player object
            playerSprite = new Player("frog", PLAYER_X_POSITION, PLAYER_Y_POSITION);
            // read the file line by line
            while ((text = br.readLine()) != null) {
                // split up each value
                String[] values = text.split(",");
                // assign to corresponding variable
                String type = values[0];
                int xpos = Integer.parseInt(values[1]);
                int ypos = Integer.parseInt(values[2]);
                // create object based on number of arguments in the line
                if (values.length == NUM_TILE_ARGUMENTS) {
                    Sprite tile = new Sprite(type, xpos, ypos);
                    tiles.add(tile);
                }
                if (values.length == NUM_OBSTACLE_ARGUMENTS) {
                    boolean travelsLeft = Boolean.parseBoolean(values[3]);
                    Obstacle obstacle = new Obstacle(type, xpos, ypos, travelsLeft);
                    obstacles.add(obstacle);
                }
            }
            // set hardcoded frog hole locations
            for(int i=HOLE_OFFSET; i < App.SCREEN_WIDTH; i += HOLE_SEPARATION) {
                Hole hole = new Hole("frog", i, SPRITE_WIDTH);
                holes.add(hole);
            }
        // catch any errors
        } catch (FileNotFoundException e) {
            // exit the game if there aren't any more levels to load
            System.out.println("No more levels! Exiting game...");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
        }
        catch (SlickException e) { }
    }

    // pick a random log from the obstacles array, return index
    private int getRandomLog() {
        rand = new Random();
        int logIndex = rand.nextInt(obstacles.size());
        while(!(obstacles.get(logIndex).getType().equals("log") | obstacles.get(logIndex).getType().equals("longLog"))) {
            logIndex = rand.nextInt(obstacles.size());
        }
        return logIndex;
    }

    // create new extralife object on a random log
    private void newExtraLife() throws SlickException {
        int logIndex = getRandomLog();
        extraLife = new ExtraLife("extralife", obstacles.get(logIndex).getLocation().getX(), obstacles.get(logIndex).getLocation().getY(), obstacles.get(logIndex));
    }

    /**
     * Update all of the sprites in the game
     * @param input controller
     * @param delta time
     * @throws SlickException
     */
	public void update(Input input, int delta) throws SlickException {
        // create an iterator object for the obstacles array list
        Iterator<Obstacle> itr = obstacles.iterator();
        // reset the player movement flags to default
        playerSprite.setMoveRight(true);
        playerSprite.setMoveLeft(true);
        playerSprite.setMoveUp(true);
        playerSprite.setMoveDown(true);
        // loop through the list until at the end
        while(itr.hasNext()) {
            // create local object
            Obstacle current = itr.next();
            // collision detection, checks if the any obstacles that can kill the player are colliding
            if (current.getBB().intersects(playerSprite.getBB())
                    & (current.getType().equals("bus")
                    | current.getType().equals("racecar")
                    | current.getType().equals("bike"))) {
                // reset position and lose life
                playerSprite.resetPosition();
                lifeCount --;
            }
            // check for any ridable objects
            if (current.getBB().intersects(playerSprite.getBB())
                    & (current.getType().equals("bulldozer")
                    | current.getType().equals("log")
                    | current.getType().equals("longLog")
                    | current.getType().equals("turtles"))) {
                // call the method in the sprite class
                playerSprite.contactSprite(current, delta);
            }
            // bulldozer collision, block player movement based on its relative position to any nearby bulldozers
            BoundingBox bb;
            if (current.getBB().intersects(bb = new BoundingBox(playerSprite.getLocation().getX()+SPRITE_WIDTH, playerSprite.getLocation().getY(), SPRITE_WIDTH, SPRITE_WIDTH))
                    & (current.getType().equals("bulldozer"))) {
                playerSprite.setMoveRight(false);
            }
            if (current.getBB().intersects(bb = new BoundingBox(playerSprite.getLocation().getX()-SPRITE_WIDTH, playerSprite.getLocation().getY(), SPRITE_WIDTH, SPRITE_WIDTH))
                    & (current.getType().equals("bulldozer"))) {
                playerSprite.setMoveLeft(false);
            }
            if (current.getBB().intersects(bb = new BoundingBox(playerSprite.getLocation().getX(), playerSprite.getLocation().getY()-SPRITE_WIDTH, SPRITE_WIDTH, SPRITE_WIDTH))
                    & (current.getType().equals("bulldozer"))) {
                playerSprite.setMoveUp(false);
            }
            if (current.getBB().intersects(bb = new BoundingBox(playerSprite.getLocation().getX(), playerSprite.getLocation().getY()+SPRITE_WIDTH, SPRITE_WIDTH, SPRITE_WIDTH))
                    & (current.getType().equals("bulldozer"))) {
                playerSprite.setMoveDown(false);
            }
            // call the update method for each instance
            current.update(input, delta);
        }

        // check if the player is touching a water tile, if they are, check if they are on a log
        for (int i=0; i<tiles.size(); i++ ) {
            if (tiles.get(i).getBB().intersects(playerSprite.getBB()) & tiles.get(i).getType().equals("water")) {
                boolean onLog = false;
                // trip the boolean if player is on a log
                for (int j=0; j<obstacles.size(); j++)  {
                    if (playerSprite.getBB().intersects(obstacles.get(j).getBB()) & obstacles.get(j).isVisible()) {
                        onLog = true;
                    }
                }
                // if not, lose a life and reset
                if (!onLog) {
                    lifeCount--;
                    playerSprite.resetPosition();
                    break;
                }
            }
            // check for tree collision, block movement
            BoundingBox bb;
            if (tiles.get(i).getBB().intersects(bb = new BoundingBox(playerSprite.getLocation().getX(), playerSprite.getLocation().getY()-48, SPRITE_WIDTH, SPRITE_WIDTH))
                    & (tiles.get(i).getType().equals("tree"))) {
                playerSprite.setMoveUp(false);
            }

        }
        // set initial boolean flag
        boolean levelComplete = true;
        // check each of the holes, trip boolean if all are complete
        for (int i=0; i < holes.size(); i++) {
            if (holes.get(i).getBB().intersects(playerSprite.getBB())) {
                holes.get(i).setVisible(true);
                playerSprite.resetPosition();
            }
            if (!holes.get(i).isVisible()) {
                levelComplete = false;
            }
        }

        // if extralife exists, check if its touching the player and add a life
        if (extraLife != null) {
            if (playerSprite.getBB().intersects(extraLife.getBB()) & extraLife.isVisible()) {
                extraLife.setVisible(false);
                lifeCount++;
            }
        }

        // spawn the extralife object on the interval determined at start of game
        if (pastTime < extraLifeTime * TO_MILLISECONDS) {
            pastTime += delta;
        } else {
            pastTime = 0;
            newExtraLife();
        }

        // load the next level if level is complete
        if (levelComplete) {
            currentLevel ++;
            loadLevel(currentLevel);
        }

        // end game if player lives run out
        if (lifeCount == -1) System.exit(0);

        // call the update method for the player instance
        playerSprite.update(input, delta);

        // update extralife object if it exists
        if (extraLife != null) extraLife.update(input, delta);
	}

    /**
     * Draw each of the sprites in the game
     * @param g
     */
	public void render(Graphics g) {
	    drawTiles();
	    drawObstacles();
	    drawHoles();
	    drawLives();
		// draw the player
        playerSprite.render();
        // draw extralife if it exists
        if (extraLife != null) extraLife.render();
	}

	// draw respective elements
    private void drawTiles() {
        for (int i=0; i < tiles.size(); i++) {
            tiles.get(i).render();
        }
    }
    private void drawObstacles() {
        for (int i=0; i < obstacles.size(); i++) {
            obstacles.get(i).render();
        }
    }
    private void drawHoles() {
        for (int i=0; i < holes.size(); i++) {
            holes.get(i).render();
        }
    }
    private void drawLives() {
	    for (int i=0; i < lifeCount; i++) {
	        life.drawCentered((i+LIVES_XPOS) + (i*LIVES_SEPARATION), LIVES_YPOS);
        }
    }
}

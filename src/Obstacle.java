import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Obstacle class controls behaviour of all moving obstacles within the game
 *
 * @author Adam Turner 910935
 *
 */
public class Obstacle extends Sprite {

    private Boolean travelsRight;
    private float speed;
    private String type;

    /**
     * Default constructor, with the addition of a direction string for the obstacles
     * @param type
     * @param x
     * @param y
     * @param travelsRight
     * @throws SlickException
     */
    public Obstacle(String type, float x, float y, Boolean travelsRight) throws SlickException {
        super(type, x, y);
        this.type = type;
        this.travelsRight = travelsRight;
        if (type.equals("bus")) speed = World.BUS_SPEED;
        if (type.equals("racecar")) speed = World.RACECAR_SPEED;
        if (type.equals("bike")) speed = World.BIKE_SPEED;
        if (type.equals("bulldozer")) speed = World.BULLDOZER_SPEED;
        if (type.equals("log")) speed = World.LOG_SPEED;
        if (type.equals("longLog")) speed = World.LONGLOG_SPEED;
        if (type.equals("turtle")) speed = World.TURTLE_SPEED;
    }

    /**
     * Update the position, location and visibility of specific types of sprites
     * @param input
     * @param delta
     */
    public void update(Input input, int delta) {
        if (travelsRight) {
            // if the sprite goes off screen
            if (getLocation().getX() > App.SCREEN_WIDTH + this.getWidth() / 2) {
                getLocation().setX(-this.getWidth()/2);
            }
            getLocation().setX(getLocation().getX() + (speed*delta));
        } else {
            // repeat for the opposite direction
            if (getLocation().getX() < -this.getWidth()/2) {
                // set the location to the opposite side
                getLocation().setX(App.SCREEN_WIDTH + this.getWidth() / 2);
            }
            getLocation().setX(getLocation().getX() - (speed*delta));
        }
        // specific behaviour for bike
        if (type.equals("bike")) {
            if (getLocation().getX() < World.SPRITE_WIDTH/2 | getLocation().getX() > World.TO_MILLISECONDS) {
                travelsRight = !travelsRight;
            }
        }
        // specific behaviour for turtle
        if (this.type.equals("turtle")) {
            turtleTimer(delta);
        }
        // update the bounding box location
        setBB();
    }

    /**
     * Get the speed of the obstacle
     * @return
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Get the current motion direction of the obstacle
     * @return
     */
    public int getDirection() {
        if (travelsRight) {
            return 1;
        } else {
            return -1;
        }
    }
}
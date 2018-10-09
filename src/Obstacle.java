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
        if (type.equals("bus")) speed = 0.15f;
        if (type.equals("racecar")) speed = 0.5f;
        if (type.equals("bike")) speed = 0.2f;
        if (type.equals("bulldozer")) speed = 0.05f;
        if (type.equals("log")) speed = 0.1f;
        if (type.equals("longLog")) speed = 0.07f;
        if (type.equals("turtle")) speed = 0.085f;
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
            if (getLocation().getX() < 24 | getLocation().getX() > 1000) {
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
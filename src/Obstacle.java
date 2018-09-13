import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Obstacle extends Sprite {

    private Boolean travelsRight;
    private float speed;
    private String type;

    public Obstacle(String type, float x, float y, Boolean travelsRight) throws SlickException {
        // default constructor, with the addition of a direction string for the obstacles
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
    public void update(Input input, int delta) {
        if (travelsRight) {
            // if the sprite goes off screen
            if (getLocation().getX() > App.SCREEN_WIDTH + this.getWidth()/2) {
                getLocation().setX(-World.SPRITE_WIDTH - this.getWidth() / 2);
            }
            getLocation().setX(getLocation().getX() + (speed*delta));
        } else {
            // repeat for the opposite direction
            if (getLocation().getX() < -World.SPRITE_WIDTH - this.getWidth()/2) {
                // set the location to the opposite side
                getLocation().setX(App.SCREEN_WIDTH + this.getWidth() / 2);
            }
            getLocation().setX(getLocation().getX() - (speed*delta));
        }
        if (type.equals("bike")) {
            if (getLocation().getX() < 24 | getLocation().getX() > 1000) {
                travelsRight = !travelsRight;
            }
        }
        turtleTimer(delta);
        // update the bounding box location
        setBB();
    }



    public float getSpeed() {
        return speed;
    }
    public int getDirection() {
        if (travelsRight) {
            return 1;
        } else {
            return -1;
        }
    }
}
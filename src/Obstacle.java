import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Obstacle extends Sprite {

    private Boolean travelsLeft = false;

    public Obstacle(String imageSrc, float x, float y, String direction) throws SlickException {
        // default constructor, with the addition of a direction string for the obstacles
        super(imageSrc, x, y);

        if (direction.equals("left")) {
            // set the direction of the object
            travelsLeft = true;
        }
    }
    public void update(Input input, int delta) {
        if (travelsLeft) {
            // if the sprite goes off screen
            if (getLocation().getX() < -World.SPRITE_WIDTH) {
                // set the location to the opposite side
                getLocation().setX(App.SCREEN_WIDTH);
            }
            getLocation().setX(getLocation().getX() - (World.OBSTACLE_SPEED*delta));
        } else {
            // repeat for the opposite direction
            if (getLocation().getX() > App.SCREEN_WIDTH) {
                getLocation().setX(-World.SPRITE_WIDTH);
            }
            getLocation().setX(getLocation().getX() + (World.OBSTACLE_SPEED*delta));
        }
        // update the bounding box location
        setBB();
    }
}
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import utilities.BoundingBox;

/**
 * ExtraLife class controls behaviour of the extralife object within the game
 *
 * @author Adam Turner 910935
 *
 */
public class ExtraLife extends Sprite {

    private long timeSinceLastMove = 0;
    private long timeSinceSpawned = 0;
    private Obstacle log;
    private boolean moveRight = true;

    /**
     * Default constructor, set the log which the object is attached to
     * @param type type of sprite
     * @param x position
     * @param y position
     * @param log assigned log
     * @throws SlickException ignore slick exception
     */
    public ExtraLife(String type, float x, float y, Obstacle log) throws SlickException {
        super(type, x, y);
        this.log = log;
    }

    /**
     * Update the visibility and movement of the object
     * @param input controller
     * @param delta time
     */
    public void update(Input input, int delta) {
        // check if the life object is off the screen
        if (log.getDirection() == 1) {
            if (getLocation().getX() > App.SCREEN_WIDTH + log.getWidth() / 2) {
                getLocation().setX(-log.getWidth()/2);
            }
            getLocation().setX(getLocation().getX() + (log.getSpeed()*delta));
        } else {
            if (getLocation().getX() < -log.getWidth()/2) {
                // set the location to the opposite side
                getLocation().setX(App.SCREEN_WIDTH + log.getWidth() / 2);
            }
            getLocation().setX(getLocation().getX() - (log.getSpeed()*delta));
        }
        setBB();

        // move the life left and right along log
        if (isVisible()) {
            if (timeSinceLastMove < World.EXTRALIFE_MOVE_OFFSET * World.TO_MILLISECONDS) {
                timeSinceLastMove += delta;
            } else {
                timeSinceLastMove = 0;
                if (moveRight & canMoveRight()) {
                    getLocation().setX(getLocation().getX() + World.SPRITE_WIDTH);
                } else {
                    moveRight = false;
                }
                if (!moveRight & canMoveLeft()) {
                    getLocation().setX(getLocation().getX() - World.SPRITE_WIDTH);
                } else {
                    moveRight = true;
                }
            }
            // disappear after specified amount of time
            if (timeSinceSpawned < (World.EXTRALIFE_MAX_LIFETIME) * World.TO_MILLISECONDS) {
                timeSinceSpawned += delta;
            } else {
                setVisible(false);
            }
        }


    }

    /**
     * Check if the life object can move right
     * @return if it can move right
     */
    public boolean canMoveRight() {
        BoundingBox bb;
        if (log.getBB().intersects(bb = new BoundingBox(getLocation().getX()+World.SPRITE_WIDTH, getLocation().getY(), World.SPRITE_WIDTH, World.SPRITE_WIDTH))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if the life object can move left
     * @return if it can move left
     */
    public boolean canMoveLeft() {
        BoundingBox bb;
        if (log.getBB().intersects(bb = new BoundingBox(getLocation().getX()-World.SPRITE_WIDTH, getLocation().getY(), World.SPRITE_WIDTH, World.SPRITE_WIDTH))) {
            return true;
        } else {
            return false;
        }
    }
}

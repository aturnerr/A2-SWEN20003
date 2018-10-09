import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import utilities.BoundingBox;

/**
 * Player class controls behaviour of the player object within the game
 *
 * @author Adam Turner 910935
 *
 */
public class Player extends Sprite {
    private boolean canMoveLeft = true;
    private boolean canMoveRight = true;
    private boolean canMoveUp = true;
    private boolean canMoveDown = true;

    /**
     * Default constructor
     * @param type
     * @param x
     * @param y
     * @throws SlickException
     */
    public Player(String type, float x, float y) throws SlickException {
        super(type, x, y);
    }

    /**
     * Accept keyboard input values and update location of Player
     * @param input
     * @param delta
     */
    public void update(Input input, int delta) {
        if (input.isKeyPressed(Input.KEY_LEFT) & getLocation().getX() >= World.SPRITE_WIDTH & canMoveLeft) {
            // only move one unit left if the player isn't next to the left side of screen
            getLocation().setX(getLocation().getX() - World.SPRITE_WIDTH);
        }
        if (input.isKeyPressed(Input.KEY_RIGHT) & getLocation().getX() <= App.SCREEN_WIDTH - World.SPRITE_WIDTH*2 & canMoveRight) {
            // only move one unit right if the player is at a position less than the screen width
            getLocation().setX(getLocation().getX() + World.SPRITE_WIDTH);
        }
        if (input.isKeyPressed(Input.KEY_UP) & getLocation().getY() >= World.SPRITE_WIDTH & canMoveUp) {
            // only move one unit up if the player isn't less at the top side of the screen.
            getLocation().setY(getLocation().getY() - World.SPRITE_WIDTH);
        }
        if (input.isKeyPressed(Input.KEY_DOWN) & getLocation().getY() <= App.SCREEN_HEIGHT - World.SPRITE_WIDTH*2 & canMoveDown) {
            // only move one unit up if the player is at a position less than the screen height.
            getLocation().setY(getLocation().getY() + World.SPRITE_WIDTH);
        }
        // update the bounding box location
        setBB();
    }

    /**
     * If player comes in contact with obstacle, move along with it
     * @param other
     * @param delta
     */
    public void contactSprite(Obstacle other, int delta) {
        getLocation().setX(getLocation().getX() + (other.getSpeed() * other.getDirection() * delta));
    }

    /**
     * Set movement for each direction
     * @param move
     */
    public void setMoveLeft(Boolean move) {
        canMoveLeft = move;
    }
    public void setMoveRight(Boolean move) {
        canMoveRight = move;
    }
    public void setMoveUp(Boolean move) {
        canMoveUp = move;
    }
    public void setMoveDown(Boolean move) {
        canMoveDown = move;
    }

    /**
     * Reset the position of the Player
     */
    public void resetPosition() {
        getLocation().setX(World.PLAYER_X_POSITION);
        getLocation().setY(World.PLAYER_Y_POSITION);
    }
}


import org.newdawn.slick.SlickException;

/**
 * Hole class controls behaviour of the hole objects in the game
 *
 * @author Adam Turner 910935
 *
 */
public class Hole extends Sprite {
    /**
     * Default constructor, set initial visibility
     * @param type type of sprite
     * @param x position
     * @param y position
     * @throws SlickException ignore slick exception
     */
    public Hole(String type, float x, float y) throws SlickException {
        super(type, x, y);
        setVisible(false);
    }
}

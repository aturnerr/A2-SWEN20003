import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import utilities.BoundingBox;

/**
 * Sprite class controls behaviour of all sprite types within the game
 *
 * @author Adam Turner 910935
 *
 */
public class Sprite {
    // initialise variables
    private Point location;
    private Image image;
    private BoundingBox boundingBox;
    private String type;
    private boolean visible = true;
    private long pastTime = 0;

    /**
     * Set the location point, image and bounding box for each sprite
     * @param type
     * @param x
     * @param y
     * @throws SlickException
     */
	public Sprite(String type, float x, float y) throws SlickException {
        if (type.equals("turtle")) {
            type = "turtles";
        }
        this.type = type;
        location = new Point(x, y);
        image = new Image("assets/"+type+".png");
        boundingBox = new BoundingBox(image, location.getX(), location.getY());
	}

    /**
     * Update the location for the sprite
     * @param input
     * @param delta
     */
	public void update(Input input, int delta) {
        location.setX(location.getX());
        location.setY(location.getY());

	}

    /**
     * Update the bounding box location
     */
    public void setBB() {
        boundingBox.setX(location.getX());
        boundingBox.setY(location.getY());
    }

    /**
     * Return the current location of the sprite
     * @return location
     */
    public Point getLocation() {
	    return location;
    }

    /**
     * Return the current location of the bounding box
     * @return boundingBox
     */
    public BoundingBox getBB() {
	    return boundingBox;
    }

    /**
     * Determine timing for turtles
     * @param delta time
     */
    public void turtleTimer(int delta) {
            if (pastTime < World.TURTLE_VISIBLE_TIME * 1000) {
                pastTime += delta;
                visible = true;
            } else if (pastTime > (World.TURTLE_VISIBLE_TIME + World.TURTLE_UNDERWATER_TIME) * 1000)  {
                pastTime = 0;
                visible = true;
            }
            else {
                pastTime += delta;
                visible = false;
            }
    }

    /**
     * Check if the sprite is currently visible
     * @return visible
     */
    public boolean isVisible() {
	    return visible;
    }

    /**
     * Set the visibility of the sprite
     * @param isVisible
     */
    public void setVisible(Boolean isVisible) {
	    visible = isVisible;
    }

    /**
     * Get the type of the sprite
     * @return type
     */
    public String getType() {
	    return type;
    }

    /**
     * Get the width of the sprite image
     * @return image width
     */
    public float getWidth() {
	    return image.getWidth();
    }

    /**
     * Draw the sprite
     */
	public void render() {

        if (visible) {
            image.drawCentered(location.getX(), location.getY());
        }
	}
}

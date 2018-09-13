import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import utilities.BoundingBox;

public class Sprite {
    // initialise variables
    private Point location;
    private Image image;
    private BoundingBox boundingBox;

	public Sprite(String imageSrc, float x, float y) throws SlickException {
		// location point, image and bounding box for each sprite
        location = new Point(x, y);
        image = new Image(imageSrc);
        boundingBox = new BoundingBox(image, location.getX(), location.getY());
	}
	
	public void update(Input input, int delta) {
		// update the location for the sprite
        location.setX(location.getX());
        location.setY(location.getY());
	}

    public void setBB() {
	    // update the bounding box location
	    boundingBox.setX(location.getX());
        boundingBox.setY(location.getY());
    }

    public Point getLocation() {
	    // return the current location of the sprite
	    return location;
    }

    public BoundingBox getBB() {
	    // return the current location of the bounding box
	    return boundingBox;
    }

	public void render() {
	    // draw the sprite
	    image.draw(location.getX(), location.getY());
	}
	
	public void contactSprite(Sprite other) {
		// exit the game
        System.exit(0);
	}
}

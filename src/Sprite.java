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
    private String type;
    private boolean visible = true;
    private long pastTime = 0;

	public Sprite(String type, float x, float y) throws SlickException {
		// location point, image and bounding box for each sprite
        if (type.equals("turtle")) {
            type = "turtles";
        }
        this.type = type;
        location = new Point(x, y);
        image = new Image("assets/"+type+".png");
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

    public void turtleTimer(int delta) {
        if (this.type.equals("turtles")) {
            if (pastTime < 7 * 1000) {
                pastTime += delta;
                visible = true;
            } else if (pastTime > 9 * 1000)  {
                pastTime = 0;
                visible = true;
            }
            else {
                pastTime += delta;
                visible = false;
            }
        }
    }

    public void lifeTimer(int delta, int time) {
        if (this.type.equals("turtles")) {
            if (pastTime < time * 1000) {
                pastTime += delta;
                visible = false;
            }
            else {
                pastTime = 0;
                visible = true;
            }
        }
    }

    public boolean isVisible() {
	    return visible;
    }

    public void setVisible(Boolean isVisible) {
	    visible = isVisible;
    }

    public String getType() {
	    return type;
    }

    public float getWidth() {
	    return image.getWidth();
    }

	public void render() {
	    // draw the sprite
        if (visible) {
            image.drawCentered(location.getX(), location.getY());
        }

	}
	
	public void contactSprite(Sprite other) {
		// exit the game

	}
}

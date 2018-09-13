import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

public class Tile {
    private Point location;
    private Image image;

    public Tile(String type, float x, float y) throws SlickException {
        location = new Point(x, y);
        image = new Image("assets/"+type+".png");
    }

    public void render() {
        // draw the sprite
        image.drawCentered(location.getX(), location.getY());
    }
}

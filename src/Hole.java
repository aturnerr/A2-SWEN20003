import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import utilities.BoundingBox;

public class Hole extends Sprite {

    //private boolean visible = false;

    public Hole(String type, float x, float y) throws SlickException {
        super(type, x, y);
        setVisible(false);
    }
}

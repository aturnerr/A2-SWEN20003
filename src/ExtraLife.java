import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import utilities.BoundingBox;

public class ExtraLife extends Sprite {

    private long pastTime = 1;
    private long timeSinceLastMove = 0;
    private long timeSinceSpawned = 0;
    private Obstacle log;
    private boolean moveRight = true;

    public ExtraLife(String type, float x, float y, Obstacle log) throws SlickException {
        super(type, x, y);
        this.log = log;
    }

    public void update(Input input, int delta) {
        //lifeTimer(delta, timeToAppear);

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

        if (isVisible()) {
            if (timeSinceLastMove < 2 * 1000) {
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

            if (timeSinceSpawned < (14) * 1000) {
                timeSinceSpawned += delta;
            } else {
                setVisible(false);
            }
        }


    }

    public void setLog(Obstacle log) {
        this.log = log;
    }

    public boolean canMoveRight() {
        BoundingBox bb;
        if (log.getBB().intersects(bb = new BoundingBox(getLocation().getX()+48, getLocation().getY(), World.SPRITE_WIDTH, World.SPRITE_WIDTH))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean canMoveLeft() {
        BoundingBox bb;
        if (log.getBB().intersects(bb = new BoundingBox(getLocation().getX()-48, getLocation().getY(), World.SPRITE_WIDTH, World.SPRITE_WIDTH))) {
            return true;
        } else {
            return false;
        }
    }

    public void lifeTimer(int delta, int time) {
        if (pastTime < time * 1000) {
            pastTime += delta;
            this.setVisible(false);
        }
        else {
            //pastTime = 0;
            this.setVisible(true);
        }
    }
}

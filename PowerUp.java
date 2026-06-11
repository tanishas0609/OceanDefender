public class PowerUp extends Sprite {

    private final int DRIFT_SPEED = 1;
    private boolean collected;

    public PowerUp(int x, int y) {
        super(x, y);
        collected = false;
        initPowerUp();
    }

    private void initPowerUp() {
        loadImage("https://codehs.com/uploads/d7ecdd3a582c1e6da3e0df265c5fa9fa");
        getImageDimensions();
    }

    public void move() {
        x += DRIFT_SPEED;
        if (x > 450) {
            visible = false;
        }
    }

    public void collect() {
        collected = true;
        visible   = false;
    }

    public boolean isCollected() {
        return collected;
    }
}

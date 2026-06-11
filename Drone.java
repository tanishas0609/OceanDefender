public class Drone extends Sprite {

    private final int RESET_X = 420;
    private int speed;

    public Drone(int x, int y, int speed) {
        super(x, y);
        this.speed = speed;
        initDrone();
    }

    private void initDrone() {
        loadImage("https://codehs.com/uploads/f4015327bdaa9ea74c00d5e91f916a3e");
        getImageDimensions();
    }

    public void move() {
        x -= speed;
        if (x < 0) {
            x = RESET_X;
        }
    }

    public void increaseSpeed(int amount) {
        speed += amount;
    }
}

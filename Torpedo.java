public class Torpedo extends Sprite {

    private final int BOARD_WIDTH   = 390;
    private final int TORPEDO_SPEED = 4;

    public Torpedo(int x, int y) {
        super(x, y);
        initTorpedo();
    }

    private void initTorpedo() {
        loadImage("https://codehs.com/uploads/6a4344bb68ee27e6d313bfff9c4b0305");
        getImageDimensions();
    }

    public void move() {
        x += TORPEDO_SPEED;
        if (x > BOARD_WIDTH) {
            visible = false;
        }
    }
}

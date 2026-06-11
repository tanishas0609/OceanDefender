import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Sub extends Sprite {

    private int dx;
    private int dy;
    private List<Torpedo> torpedoes;
    private boolean shieldActive;

    public Sub(int x, int y) {
        super(x, y);
        initSub();
    }

    private void initSub() {
        torpedoes    = new ArrayList<>();
        shieldActive = false;
        loadImage("https://codehs.com/uploads/31eaf281a54bca39e9cffbd9bcb2f457");
        getImageDimensions();
    }

    public void move() {
        x += dx;
        y += dy;
        if (x < 1) { x = 1; }
        if (y < 1) { y = 1; }
    }

    public List<Torpedo> getTorpedoes() {
        return torpedoes;
    }

    public void fire() {
        torpedoes.add(new Torpedo(x + width, y + height / 2));
    }

    public void activateShield() {
        shieldActive = true;
    }

    public boolean hasShield() {
        return shieldActive;
    }

    public void breakShield() {
        shieldActive = false;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE)  { fire();  }
        if (key == KeyEvent.VK_LEFT)   { dx = -2; }
        if (key == KeyEvent.VK_RIGHT)  { dx =  2; }
        if (key == KeyEvent.VK_UP)     { dy = -2; }
        if (key == KeyEvent.VK_DOWN)   { dy =  2; }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT)   { dx = 0; }
        if (key == KeyEvent.VK_RIGHT)  { dx = 0; }
        if (key == KeyEvent.VK_UP)     { dy = 0; }
        if (key == KeyEvent.VK_DOWN)   { dy = 0; }
    }
}

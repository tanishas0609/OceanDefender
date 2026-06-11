import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.EventQueue;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH  = 500;
    private final int B_HEIGHT = 350;
    private final int DELAY    = 15;
    private final int ISUB_X   = 40;
    private final int ISUB_Y   = 175;

    private Timer  timer;
    private Sub    sub;
    private List<Drone>   drones;
    private List<PowerUp> powerUps;

    private boolean inGame;
    private int     score;
    private int     lives;
    private int     wave;
    private int     powerUpTimer;

    private final int[][] pos = {
        {520, 30},  {600, 70},  {480, 110},
        {550, 150}, {510, 200}, {570, 240},
        {490, 280}, {620, 50},  {540, 130},
        {580, 180}, {500, 90},  {630, 220},
        {460, 260}, {590, 30},  {470, 160},
        {610, 100}, {530, 310}, {490, 50},
        {560, 330}, {620, 140}
    };

    public Board() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocus();
        setBackground(new Color(0, 30, 80));
        inGame = true;

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        sub          = new Sub(ISUB_X, ISUB_Y);
        score        = 0;
        lives        = 3;
        wave         = 1;
        powerUpTimer = 0;
        powerUps     = new ArrayList<>();

        initDrones();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void initDrones() {
        drones = new ArrayList<>();
        for (int[] p : pos) {
            drones.add(new Drone(p[0], p[1], wave));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            drawObjects(g);
        } else {
            drawGameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics g) {
        if (sub.isVisible()) {
            g.drawImage(sub.getImage(), sub.getX(), sub.getY(), this);
            if (sub.hasShield()) {
                g.setColor(new Color(0, 255, 255, 120));
                g.fillOval(sub.getX() - 5, sub.getY() - 5,
                           sub.getImage().getWidth(null) + 10,
                           sub.getImage().getHeight(null) + 10);
            }
        }

        List<Torpedo> ts = sub.getTorpedoes();
        for (Torpedo t : ts) {
            if (t.isVisible()) {
                g.drawImage(t.getImage(), t.getX(), t.getY(), this);
            }
        }

        for (Drone d : drones) {
            if (d.isVisible()) {
                g.drawImage(d.getImage(), d.getX(), d.getY(), this);
            }
        }

        for (PowerUp p : powerUps) {
            if (p.isVisible()) {
                g.drawImage(p.getImage(), p.getX(), p.getY(), this);
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.drawString("Score: " + score, 5, 15);
        g.drawString("Lives: " + lives, 5, 30);
        g.drawString("Wave:  " + wave,  5, 45);

        if (sub.hasShield()) {
            g.setColor(new Color(0, 255, 255));
            g.drawString("[ SHIELD ACTIVE ]", 5, 62);
        }
    }

    private void drawGameOver(Graphics g) {
        Font big   = new Font("Arial", Font.BOLD, 20);
        Font small = new Font("Arial", Font.PLAIN, 14);
        FontMetrics fmBig   = getFontMetrics(big);
        FontMetrics fmSmall = getFontMetrics(small);

        String msgOver  = "GAME OVER";
        String msgScore = "Final Score: " + score;
        String msgWave  = "You reached Wave " + wave;

        g.setColor(Color.WHITE);
        g.setFont(big);
        g.drawString(msgOver,
                (B_WIDTH - fmBig.stringWidth(msgOver))   / 2,
                B_HEIGHT / 2 - 20);

        g.setFont(small);
        g.drawString(msgScore,
                (B_WIDTH - fmSmall.stringWidth(msgScore)) / 2,
                B_HEIGHT / 2 + 10);
        g.drawString(msgWave,
                (B_WIDTH - fmSmall.stringWidth(msgWave))  / 2,
                B_HEIGHT / 2 + 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!inGame) {
            timer.stop();
            return;
        }
        updateSub();
        updateTorpedoes();
        updateDrones();
        updatePowerUps();
        checkCollisions();
        repaint();
    }

    private void updateSub() {
        if (sub.isVisible()) {
            sub.move();
        }
    }

    private void updateTorpedoes() {
        List<Torpedo> ts = sub.getTorpedoes();
        for (int i = 0; i < ts.size(); i++) {
            Torpedo t = ts.get(i);
            if (t.isVisible()) {
                t.move();
            } else {
                ts.remove(i);
                i--;
            }
        }
    }

    private void updateDrones() {
        if (drones.isEmpty()) {
            wave++;
            initDrones();
            return;
        }
        for (int i = 0; i < drones.size(); i++) {
            Drone d = drones.get(i);
            if (d.isVisible()) {
                d.move();
            } else {
                drones.remove(i);
                i--;
            }
        }
    }

    private void updatePowerUps() {
        powerUpTimer++;
        if (powerUpTimer >= 333) {
            powerUpTimer = 0;
            int spawnY = 20 + (int)(Math.random() * (B_HEIGHT - 60));
            powerUps.add(new PowerUp(0, spawnY));
        }
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
            if (p.isVisible()) {
                p.move();
            } else {
                powerUps.remove(i);
                i--;
            }
        }
    }

    public void checkCollisions() {
        if (!inGame) return;

        Rectangle subBounds = sub.getBounds();

        for (Drone d : drones) {
            if (d.isVisible() && subBounds.intersects(d.getBounds())) {
                if (sub.hasShield()) {
                    d.setVisible(false);
                    sub.breakShield();
                } else {
                    d.setVisible(false);
                    lives--;
                    if (lives <= 0) {
                        sub.setVisible(false);
                        inGame = false;
                        return;
                    }
                }
            }
        }

        List<Torpedo> ts = sub.getTorpedoes();
        for (Torpedo t : ts) {
            for (Drone d : drones) {
                if (t.isVisible() && d.isVisible() && t.getBounds().intersects(d.getBounds())) {
                    t.setVisible(false);
                    d.setVisible(false);
                    score += 10;
                }
            }
        }

        for (PowerUp p : powerUps) {
            if (p.isVisible() && !p.isCollected() && subBounds.intersects(p.getBounds())) {
                p.collect();
                sub.activateShield();
            }
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            sub.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            sub.keyPressed(e);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Ocean Defender");
            Board board = new Board();
            frame.add(board);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            board.requestFocusInWindow();
        });
    }
}

package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SurvivalMode extends JFrame {

    private static final long serialVersionUID = 1L;

    public SurvivalMode() {
        setTitle("Survival Mode");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 360, 640);
        setLocationRelativeTo(null);
        setResizable(false);
        SurvivalBoard board = new SurvivalBoard(this);
        add(board);
    }
}

class SurvivalBoard extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private Timer timer;
    private Image bg, playerImg, asteroidImg, tieImg, pBulletImg, eBulletImg, expImg;
    private int pX = 150, pY = 500, pSpeed = 6;
    private boolean left = false, right = false;
    private long startTime;
    private int survivalTime = 0;
    private int lives = 3;
    private int invulnTimer = 0;
    private boolean isGameOver = false;
    private ArrayList<GameObject> asteroids = new ArrayList<>();
    private ArrayList<GameObject> enemies = new ArrayList<>();
    private ArrayList<GameObject> pBullets = new ArrayList<>();
    private ArrayList<GameObject> eBullets = new ArrayList<>();
    private ArrayList<GameObject> explosions = new ArrayList<>();
    private Random rand = new Random();
    private int speedMultiplier = 1;
    private JFrame parentFrame;
    private JButton btnGotIt;

    public SurvivalBoard(JFrame parent) {
        this.parentFrame = parent;
        setLayout(null);
        setFocusable(true);
        requestFocusInWindow();
        
        btnGotIt = new JButton("Main Menu");
        btnGotIt.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnGotIt.setBounds(110, 300, 130, 50);
        btnGotIt.setVisible(false);
        btnGotIt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainMenu menu = new MainMenu();
                menu.setVisible(true);
                parentFrame.dispose();
            }
        });
        add(btnGotIt);
        
        bg = loadImage("/Background3.png");
        playerImg = loadImage("/XWing.png");
        asteroidImg = loadImage("/Asteroid.png");
        tieImg = loadImage("/TIE.png");
        pBulletImg = loadImage("/PlayerBullet.png");
        eBulletImg = loadImage("/EnemyBullet.png");
        expImg = loadImage("/Explosion.png");
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_A) left = true;
                if (key == KeyEvent.VK_D) right = true;
                if (key == KeyEvent.VK_SPACE && !isGameOver) shoot();
            }
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_A) left = false;
                if (key == KeyEvent.VK_D) right = false;
            }
        });
        
        startTime = System.currentTimeMillis();
        timer = new Timer(16, this);
        timer.start();
    }

    private Image loadImage(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL).getImage();
        } else {
            System.err.println("CRITICAL ERROR: Could not find image file: " + path);
            return new ImageIcon().getImage();
        }
    }

    private void shoot() {
        pBullets.add(new GameObject(pX + 15, pY, 10, 20));
    }

    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            survivalTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
            speedMultiplier = 1 + (survivalTime / 10);

            if (left && pX > 0) pX -= pSpeed;
            if (right && pX < 340 - 40) pX += pSpeed;

            if (rand.nextInt(100) < 2 + speedMultiplier) {
                asteroids.add(new GameObject(rand.nextInt(300), -50, 40, 40));
            }
            if (rand.nextInt(150) < 1 + speedMultiplier) {
                enemies.add(new GameObject(rand.nextInt(300), -50, 40, 40));
            }

            for (GameObject a : asteroids) a.y += 3 + speedMultiplier;
            
            for (GameObject en : enemies) {
                en.y += 2 + speedMultiplier;
                if (rand.nextInt(100) < 2) {
                    eBullets.add(new GameObject(en.x + 15, en.y + 40, 10, 20));
                }
            }
            
            for (GameObject pb : pBullets) pb.y -= 7;
            for (GameObject eb : eBullets) eb.y += 5 + speedMultiplier;

            if (invulnTimer > 0) invulnTimer--;

            checkCollisions();
        }
        repaint();
    }

    private void checkCollisions() {
        Rectangle pRect = new Rectangle(pX, pY, 40, 40);
        
        Iterator<GameObject> pbi = pBullets.iterator();
        while (pbi.hasNext()) {
            GameObject pb = pbi.next();
            Rectangle pbRect = new Rectangle(pb.x, pb.y, pb.w, pb.h);
            boolean hit = false;
            
            Iterator<GameObject> ai = asteroids.iterator();
            while (ai.hasNext()) {
                GameObject a = ai.next();
                if (pbRect.intersects(new Rectangle(a.x, a.y, a.w, a.h))) {
                    explosions.add(new GameObject(a.x, a.y, 40, 40, 15));
                    ai.remove();
                    hit = true;
                    break;
                }
            }
            
            if (!hit) {
                Iterator<GameObject> eni = enemies.iterator();
                while (eni.hasNext()) {
                    GameObject en = eni.next();
                    if (pbRect.intersects(new Rectangle(en.x, en.y, en.w, en.h))) {
                        explosions.add(new GameObject(en.x, en.y, 40, 40, 15));
                        eni.remove();
                        hit = true;
                        break;
                    }
                }
            }
            if (hit) pbi.remove();
        }

        Iterator<GameObject> exi = explosions.iterator();
        while (exi.hasNext()) {
            GameObject ex = exi.next();
            ex.timer--;
            if (ex.timer <= 0) exi.remove();
        }

        for (GameObject a : asteroids) {
            if (pRect.intersects(new Rectangle(a.x, a.y, a.w, a.h))) hitPlayer();
        }
        for (GameObject en : enemies) {
            if (pRect.intersects(new Rectangle(en.x, en.y, en.w, en.h))) hitPlayer();
        }
        for (GameObject eb : eBullets) {
            if (pRect.intersects(new Rectangle(eb.x, eb.y, eb.w, eb.h))) hitPlayer();
        }
    }

    private void hitPlayer() {
        if (invulnTimer <= 0) {
            lives--;
            invulnTimer = 60;
            if (lives <= 0) {
                isGameOver = true;
                timer.stop();
                btnGotIt.setVisible(true);
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        
        if (!isGameOver) {
            if (invulnTimer == 0 || invulnTimer % 10 < 5) {
                g.drawImage(playerImg, pX, pY, 40, 40, this);
            }
        }
        
        for (GameObject a : asteroids) g.drawImage(asteroidImg, a.x, a.y, a.w, a.h, this);
        for (GameObject en : enemies) g.drawImage(tieImg, en.x, en.y, en.w, en.h, this);
        for (GameObject pb : pBullets) g.drawImage(pBulletImg, pb.x, pb.y, pb.w, pb.h, this);
        for (GameObject eb : eBullets) g.drawImage(eBulletImg, eb.x, eb.y, eb.w, eb.h, this);
        for (GameObject ex : explosions) g.drawImage(expImg, ex.x, ex.y, ex.w, ex.h, this);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Time: " + survivalTime + "s", 10, 30);
        
        g.setColor(Color.RED);
        g.drawString("Lives: " + lives, 260, 30);
        
        if (isGameOver) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, 360, 640);
            
            g.setColor(Color.RED);
            g.setFont(new Font("Times New Roman", Font.BOLD, 45));
            g.drawString("GAME OVER!", 25, 200);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Times New Roman", Font.BOLD, 25));
            g.drawString("Survived for: " + survivalTime + "s", 70, 250);
        }
    }
}

class GameObject {
    int x, y, w, h, timer;
    public GameObject(int x, int y, int w, int h) {
        this.x = x; this.y = y; this.w = w; this.h = h;
    }
    public GameObject(int x, int y, int w, int h, int timer) {
        this.x = x; this.y = y; this.w = w; this.h = h; this.timer = timer;
    }
}
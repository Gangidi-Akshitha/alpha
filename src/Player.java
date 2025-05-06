import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private double x, y, speed = 4, width = 40, height = 50;
    private boolean up, down, left, right;

    private final List<Bullet> bullets = new ArrayList<>();
    private long lastShotTime = 0;
    private final long fireDelay = 70_000_000;
    
    private boolean isHit = false;
    private long hitEffectStartTime = 0;
    private long hitEffectDuration = 200_000_000;

    private long lastDamageTime = 0;
    private long damageCooldown = 1_000_000_000; 

    private int health = 100;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (up) y -= speed;
        if (down) y += speed;
        if (left) x -= speed;
        if (right) x += speed;

        x = Math.max(0, Math.min(x, 800 - width));
        y = Math.max(0, Math.min(y, 600 - height));

        bullets.forEach(Bullet::update);
        bullets.removeIf(Bullet::isOffScreen);
    }

    public void render(Graphics g) {
        long now = System.nanoTime();
        if (isHit && now - hitEffectStartTime < hitEffectDuration) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);
            isHit = false;
        }
        g.fillRect((int)x, (int)y, (int)width, (int)height);
   
        g.setColor(Color.GRAY); 
        g.fillRect((int)x + 10, (int)y + 10, 20, 30);
        g.setColor(Color.LIGHT_GRAY); 
        g.fillOval((int)x + 13, (int)y, 14, 14);
        g.setColor(Color.DARK_GRAY);
        g.fillRect((int)x, (int)y + 12, 10, 10); 
        g.fillRect((int)x + 30, (int)y + 12, 10, 10);
        g.setColor(Color.RED); 
        g.fillRect((int)x + 18, (int)y - 10, 4, 10);
        g.setColor(Color.BLUE); 
        g.fillOval((int)x + 15, (int)y + 3, 3, 3); 
        g.fillOval((int)x + 22, (int)y + 3, 3, 3);
        
        bullets.forEach(b -> b.render(g));
    }

    public void shoot() {
        long now = System.nanoTime();
        if (now - lastShotTime > fireDelay) {
            bullets.add(new Bullet(x + width / 2 - 4, y));
            lastShotTime = now;
        }
    }

    public void takeDamage(int damage) {
        long now = System.nanoTime();
        if (now - lastDamageTime >= damageCooldown) {
            health -= damage;
            if (health < 0) health = 0;
            lastDamageTime = now;

            isHit = true;
            hitEffectStartTime = now;
        }
    }
    
    public void reset(double x, double y) {
        this.x = x;
        this.y = y;
        this.health = 100;
        this.bullets.clear();
    }

    public boolean isAlive() { 
    	return health > 0; 
    	}
    public int getHealth() {
    	return health;
    	}
    public Rectangle getBounds() { 
    	return new Rectangle((int)x, (int)y, (int)width, (int)height);
    	}

    public List<Bullet> getBullets() {
    	return bullets; 
    	}
    public void setUp(boolean b) { 
    	up = b; 
    	} 
    public void setDown(boolean b) { 
    	down = b;
    	}
    public void setLeft(boolean b) { 
    	left = b; 
    	} 
    public void setRight(boolean b) { 
    	right = b;
    	}
    public double getX() {
    	return x;
    	} 
    public double getY() { 
    	return y; 
    	}
}
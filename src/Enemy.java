import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Enemy {
    private double x, y;
    private double size = 30;
    private double speed;

    public Enemy(double x, double y, double speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void update(double playerX, double playerY) {
        double dx = playerX - x;
        double dy = playerY - y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist > 0) {
            x += (dx / dist) * speed;
            y += (dy / dist) * speed;
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int)x, (int)y, (int)size, (int)size);
    }

    public double getX() { 
    	return x; 
    	}
    public double getY() { 
    	return y;
    	}
    public double getSize() { 
    	return size;
    	}
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)size, (int)size);
    }
}
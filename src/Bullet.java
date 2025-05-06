import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet {
    private double x, y;
    private double speed = 10;
    private double size = 10;

    public Bullet(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y -= speed;
    }

    public void render(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval((int)x, (int)y, (int)size, (int)size);
    }

    public boolean isOffScreen() {
        return y < 0;
    }

    public double getX() { 
    	return x;
    	}
    public double getY() {
    	return y;
    	}
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)size, (int)size);
    }
}
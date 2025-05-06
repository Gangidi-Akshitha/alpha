
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

public class GamePane extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Player player;
    private List<Enemy> enemies;
    private int score = 0;

    public GamePane() {
        setLayout(null);
        setBackground(Color.BLACK);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void update() {
        repaint();
    }

    private void renderHealthBar(Graphics g, int health) {
        int fullBars = health / 20;
        for (int i = 0; i < 5; i++) {
            g.setColor(i < fullBars ? Color.GREEN : Color.DARK_GRAY);
            g.fillRect(20 + i * 25, 20, 20, 10);
            g.setColor(Color.BLACK);
            g.drawRect(20 + i * 25, 20, 20, 10);
        }
    }

    private void renderScore(Graphics g, int score) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 25, 62);
    }

    private void renderGameOver(Graphics g, int score) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, 800, 600);
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("GAME OVER", 250, 300);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Final Score: " + score, 300, 350);
    }

    public void render(Player player, List<Enemy> enemies, int score) {
        this.player = player;
        this.enemies = enemies;
        this.score = score;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (player != null) {
            // Render enemies
            if (enemies != null) {
                for (Enemy enemy : enemies) {
                    enemy.render(g);
                }
            }
            
            // Render player
            player.render(g);
            
            // Render UI
            renderHealthBar(g, player.getHealth());
            renderScore(g, score);
            
            if (!player.isAlive()) {
                renderGameOver(g, score);
            }
        }
    }
}
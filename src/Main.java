
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Top-Down Shooter Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            
            GameManager gameManager = new GameManager();
            frame.add(gameManager.getGamePane());
            
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
            
            gameManager.startGameLoop();
        });
    }
}
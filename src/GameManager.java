import javax.swing.JButton;
import javax.swing.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class GameManager {
    private final GamePane gamePane;
    private final Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private final Random random = new Random();

    private long lastEnemySpawn = 0;
    private final long enemySpawnDelay = 1_200_000_000;
    private Timer gameLoop;
    private int score = 0;
    private JButton restartButton;
    
    private int lastSpeedIncreaseScore = 0;
    private double enemySpeed = 2.0;

    public GameManager() {
        gamePane = new GamePane();
        player = new Player(400, 300);
        gamePane.setPlayer(player);

        restartButton = new JButton("Restart");
        restartButton.setBounds(360, 380, 80, 30);
        restartButton.setBackground(new java.awt.Color(40, 167, 69));
        restartButton.setForeground(java.awt.Color.WHITE);
        restartButton.setVisible(false);
        restartButton.addActionListener(e -> restartGame());
        gamePane.add(restartButton);
        
        setupKeyListener();
    }

    private void setupKeyListener() {
        gamePane.setFocusable(true);
        gamePane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        player.setUp(true);
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        player.setDown(true);
                        break;
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        player.setLeft(true);
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        player.setRight(true);
                        break;
                    case KeyEvent.VK_SPACE:
                        player.shoot();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        player.setUp(false);
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        player.setDown(false);
                        break;
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        player.setLeft(false);
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        player.setRight(false);
                        break;
                }
            }
        });
    }

    private void restartGame() {
        enemies.clear();
        player.reset(400, 300);
        score = 0;
        restartButton.setVisible(false);
        startGameLoop();
        gamePane.requestFocus();
    }

    public GamePane getGamePane() {
        return gamePane;
    }

    private void spawnEnemy() {
        double x, y;
        int edge = random.nextInt(3);

        switch (edge) {
            case 0 -> { x = random.nextInt(800 - 30); y = 0; }
            case 1 -> { x = 0; y = random.nextInt(600 - 90); }
            case 2 -> { x = 800 - 30; y = random.nextInt(600 - 90); }
            default -> { x = 0; y = 0; }
        }

        enemies.add(new Enemy(x, y, enemySpeed));
    }

    private boolean isColliding(Player player, Enemy enemy) {
        return player.getBounds().intersects(enemy.getBounds());
    }

    public void startGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        
        gameLoop = new Timer(16, e -> {
            long now = System.nanoTime();
            if (now - lastEnemySpawn > enemySpawnDelay) {
                spawnEnemy();
                lastEnemySpawn = now;
            }
            
            if (score - lastSpeedIncreaseScore >= 200) {
                enemySpeed += 1.0;
                lastSpeedIncreaseScore = score;
                System.out.println("Enemy speed increased to: " + enemySpeed);
            }

            player.update();
            enemies.forEach(enemy -> enemy.update(player.getX(), player.getY()));

            handleCollisions();
            if (!player.isAlive()) {
                gameLoop.stop();
                restartButton.setVisible(true);
                System.out.println("Game Over!");
            }

            gamePane.update();
            gamePane.render(player, enemies, score);
        });
        
        gameLoop.start();
        gamePane.requestFocus();
    }

    private void handleCollisions() {
        List<Bullet> bullets = player.getBullets();
        List<Enemy> enemiesToRemove = new ArrayList<>();
        List<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            for (Enemy enemy : enemies) {
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    bulletsToRemove.add(bullet);
                    enemiesToRemove.add(enemy);
                    score += 10;
                    break;
                }
            }
        }

        enemies.removeAll(enemiesToRemove);
        bullets.removeAll(bulletsToRemove);

        for (Enemy enemy : enemies) {
            if (isColliding(player, enemy)) {
                player.takeDamage(4);
            }
        }
    }
}
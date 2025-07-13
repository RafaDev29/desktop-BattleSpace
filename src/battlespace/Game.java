// Game.java
package battlespace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game {
    private final int WIDTH = 30;
    private final int HEIGHT = 20;
    private char[][] board;

    private Player player;
    private List<Enemy> enemies;
    private List<Bullet> bullets;

    private boolean running = true;
    private int score = 0;
    private int lives = 3;

    private InputHandler inputHandler;

    // Contadores para movimientos lentos
    private int enemyMoveCounter = 0;
    private int enemyMoveInterval = 90; // cada 90 frames (3 segundos a 30fps)

    private int bulletMoveCounter = 0;
    private int bulletMoveInterval = 15; // cada 15 frames (0.5 segundos)

    private int bulletFireCooldown = 0;
    private final int BULLET_FIRE_DELAY = 10; // 10 frames de delay entre disparos

    public Game() {
        board = new char[HEIGHT][WIDTH];
        player = new Player(WIDTH / 2, HEIGHT - 2);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();

        spawnEnemies();

        inputHandler = new InputHandler();
        inputHandler.setGame(this);
        new Thread(inputHandler).start();
    }

    public void start() {

        render();

        long lastTime = System.currentTimeMillis();
        final int TARGET_FPS = 30;
        final long FRAME_TIME = 1000 / TARGET_FPS;

        while (running) {
            long currentTime = System.currentTimeMillis();
            long deltaTime = currentTime - lastTime;

            if (deltaTime >= FRAME_TIME) {
                update();
                lastTime = currentTime;
            }


            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        Renderer.showGameOver(score);
        inputHandler.stop();
    }

    private void update() {
        boolean needsRender = false;


        if (bulletFireCooldown > 0) {
            bulletFireCooldown--;
        }


        bulletMoveCounter++;
        if (bulletMoveCounter >= bulletMoveInterval) {
            Iterator<Bullet> it = bullets.iterator();
            while (it.hasNext()) {
                Bullet b = it.next();
                b.moveUp();
                needsRender = true;
                if (b.getY() < 0) {
                    it.remove();
                } else {

                    Iterator<Enemy> ei = enemies.iterator();
                    while (ei.hasNext()) {
                        Enemy e = ei.next();
                        if (e.getX() == b.getX() && e.getY() == b.getY()) {
                            ei.remove();
                            it.remove();
                            score += 100;
                            needsRender = true;
                            break;
                        }
                    }
                }
            }
            bulletMoveCounter = 0;
        }


        for (Enemy e : enemies) {
            if (e.getY() >= player.getY()) {
                lives--;
                enemies.clear();
                bullets.clear();
                spawnEnemies();
                needsRender = true;
                // Aumentar dificultad un poco
                if (enemyMoveInterval > 60) {
                    enemyMoveInterval -= 5;
                }
                break;
            }
        }


        if (enemies.isEmpty()) {
            spawnEnemies();
            needsRender = true;

            if (enemyMoveInterval > 60) {
                enemyMoveInterval -= 3;
            }
            if (bulletMoveInterval > 8) {
                bulletMoveInterval -= 1;
            }
        }

        if (lives <= 0) running = false;


        enemyMoveCounter++;
        if (enemyMoveCounter >= enemyMoveInterval) {
            for (Enemy e : enemies) {
                e.moveDown();
            }
            enemyMoveCounter = 0;
            needsRender = true;
        }


        if (needsRender) {
            render();
        }
    }

    private void render() {
        clearBoard();


        board[player.getY()][player.getX()] = '^';


        for (Enemy e : enemies) {
            if (e.getY() >= 0 && e.getY() < HEIGHT)
                board[e.getY()][e.getX()] = '@';
        }

        // Dibujar balas
        for (Bullet b : bullets) {
            if (b.getY() >= 0 && b.getY() < HEIGHT)
                board[b.getY()][b.getX()] = '|';
        }

        // Usar el renderer optimizado
        Renderer.renderGame(board, score, lives, WIDTH);
    }

    private void clearBoard() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                board[y][x] = ' ';
            }
        }
    }

    private void spawnEnemies() {
        for (int row = 0; row < 3; row++) {
            for (int i = 4; i < WIDTH - 4; i += 4) {
                enemies.add(new Enemy(i, 2 + row));
            }
        }
    }

    public void movePlayerLeft() {
        player.move(-1, WIDTH);
        render(); // Solo renderizar cuando el jugador se mueva
    }

    public void movePlayerRight() {
        player.move(1, WIDTH);
        render(); // Solo renderizar cuando el jugador se mueva
    }

    public void fireBullet() {
        if (bulletFireCooldown <= 0) {
            bullets.add(new Bullet(player.getX(), player.getY() - 1));
            bulletFireCooldown = BULLET_FIRE_DELAY;
            render(); // Solo renderizar cuando se dispare
        }
    }

    public void quit() {
        running = false;
    }
}
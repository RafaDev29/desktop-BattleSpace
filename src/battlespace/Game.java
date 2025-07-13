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

    private int enemyMoveCounter = 0;
    private int enemyMoveInterval = 20; // cada 20 frames bajan 1 línea

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
        while (running) {
            update();
            render();
            sleep(30); // 30 ms por frame (~33 FPS)
        }

        System.out.println("\n💀 GAME OVER 💀 Final Score: " + score);
    }

    private void update() {
        // Mover balas
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.moveUp();
            if (b.getY() < 0) {
                it.remove();
            } else {
                // Verificar colisión con enemigos
                Iterator<Enemy> ei = enemies.iterator();
                while (ei.hasNext()) {
                    Enemy e = ei.next();
                    if (e.getX() == b.getX() && e.getY() == b.getY()) {
                        ei.remove();
                        it.remove();
                        score += 100;
                        break;
                    }
                }
            }
        }

        // Verificar colisión enemigos con jugador
        for (Enemy e : enemies) {
            if (e.getY() >= player.getY()) {
                lives--;
                enemies.clear(); // reiniciar enemigos
                bullets.clear();
                spawnEnemies();
                break;
            }
        }

        if (lives <= 0) running = false;

        // Movimiento automático de enemigos
        enemyMoveCounter++;
        if (enemyMoveCounter >= enemyMoveInterval) {
            for (Enemy e : enemies) {
                e.moveDown();
            }
            enemyMoveCounter = 0;
        }
    }

    private void render() {
        clearBoard();

        // Dibujar jugador
        board[player.getY()][player.getX()] = '^';

        // Dibujar enemigos
        for (Enemy e : enemies) {
            if (e.getY() >= 0 && e.getY() < HEIGHT)
                board[e.getY()][e.getX()] = '@';
        }

        // Dibujar balas
        for (Bullet b : bullets) {
            if (b.getY() >= 0 && b.getY() < HEIGHT)
                board[b.getY()][b.getX()] = '|';
        }

        // Dibujar panel
        Renderer.clearConsole();
        System.out.println("SCORE: " + score + "   LIVES: " + "♥".repeat(lives));
        System.out.println("-".repeat(WIDTH));
        for (char[] row : board) {
            System.out.println(new String(row));
        }
        System.out.println("-".repeat(WIDTH));
        System.out.println("Use A/D to move, SPACE to fire.");
    }

    private void clearBoard() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                board[y][x] = ' ';
            }
        }
    }

    private void spawnEnemies() {
        for (int i = 4; i < WIDTH - 4; i += 4) {
            enemies.add(new Enemy(i, 2));
        }
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    // Métodos para InputHandler
    public void movePlayerLeft() {
        player.move(-1, WIDTH);
    }

    public void movePlayerRight() {
        player.move(1, WIDTH);
    }

    public void fireBullet() {
        bullets.add(new Bullet(player.getX(), player.getY() - 1));
    }
}

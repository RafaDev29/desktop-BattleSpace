package battlespace;

public class Bullet {
    private int x;
    private int y;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveUp() {
        y--; // Se mueve una casilla por frame
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
package battlespace;

public class Player {
    private int x;
    private int y;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int dx, int maxWidth) {
        x += dx;
        if (x < 0) x = 0;
        if (x >= maxWidth) x = maxWidth - 1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

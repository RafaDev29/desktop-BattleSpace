package battlespace;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Battlespace...");
        System.out.println("Controls: A/D to move, SPACE to fire, Q to quit");
        System.out.println("Press ENTER to start!");

        try {
            System.in.read();
        } catch (Exception e) {
            // Ignore
        }

        Game game = new Game();
        game.start();
    }
}
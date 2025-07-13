// Renderer.java
package battlespace;

public class Renderer {
    private static boolean gameStarted = false;
    private static final String ANSI_SAVE_CURSOR = "\033[s";
    private static final String ANSI_RESTORE_CURSOR = "\033[u";
    private static final String ANSI_HIDE_CURSOR = "\033[?25l";
    private static final String ANSI_SHOW_CURSOR = "\033[?25h";

    public static void renderGame(char[][] board, int score, int lives, int width) {
        if (!gameStarted) {

            clearConsole();
            System.out.print(ANSI_HIDE_CURSOR);
            gameStarted = true;
        }


        StringBuilder screen = new StringBuilder();

        // Línea 1: Score y vidas
        screen.append("SCORE: ").append(score).append("   LIVES: ");
        for (int i = 0; i < lives; i++) {
            screen.append("♥");
        }

        for (int i = 0; i < 20; i++) {
            screen.append(" ");
        }
        screen.append("\n");


        screen.append("-".repeat(width)).append("\n");


        for (char[] row : board) {
            screen.append(new String(row)).append("\n");
        }


        screen.append("-".repeat(width)).append("\n");


        screen.append("Use A/D to move, SPACE to fire. Press Q to quit.\n");


        System.out.print("\033[H");
        System.out.print(screen.toString());
        System.out.flush();
    }

    public static void clearConsole() {
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }

    public static void showGameOver(int finalScore) {
        System.out.print(ANSI_SHOW_CURSOR);
        System.out.println("\n💀 GAME OVER 💀 Final Score: " + finalScore);
        System.out.println("Thanks for playing!");
    }
}
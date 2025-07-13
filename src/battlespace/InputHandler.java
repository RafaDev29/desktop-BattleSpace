package battlespace;

import java.io.IOException;

public class InputHandler implements Runnable {

    private Game game;

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int key = System.in.read();
                if (key == -1) break;

                char c = (char) key;

                switch (Character.toUpperCase(c)) {
                    case 'A':
                        game.movePlayerLeft();
                        break;
                    case 'D':
                        game.movePlayerRight();
                        break;
                    case ' ':
                        game.fireBullet();
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

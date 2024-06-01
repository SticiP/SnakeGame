package game.snakegame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Wall {
    private final int x;
    private final int y;

    public Wall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x * SnakeGame.CELL_SIZE, y * SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE);
    }
}

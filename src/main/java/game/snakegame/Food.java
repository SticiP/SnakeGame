package game.snakegame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

import java.util.List;
import java.util.Random;

public class Food {
    private int x;
    private int y;
    private final Random random = new Random();

    public Food(List<Cell> snakeBody, List<Wall> walls) {
        respawn(snakeBody, walls);
    }

    public void respawn(List<Cell> snakeBody, List<Wall> walls) {
        boolean isValidPosition;
        do {
            isValidPosition = true;
            x = random.nextInt(SnakeGame.LEVEL_WIDTH);
            y = random.nextInt(SnakeGame.LEVEL_HEIGHT);

            // Verificăm coliziunea cu șarpele
            for (Cell cell : snakeBody) {
                if (cell.getX() == x && cell.getY() == y) {
                    isValidPosition = false;
                    break;
                }
            }

            // Verificăm coliziunea cu pereții
            for (Wall wall : walls) {
                if (wall.getX() == x && wall.getY() == y) {
                    isValidPosition = false;
                    break;
                }
            }
        } while (!isValidPosition);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(GraphicsContext gc) {
        RadialGradient gradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.RED), new Stop(1, Color.DARKRED)
        );

        gc.setFill(gradient);
        gc.fillOval(x * SnakeGame.CELL_SIZE, y * SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE);
    }
}

package game.snakegame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import java.util.LinkedList;
import java.util.List;

public class Snake {
    private final List<Cell> body = new LinkedList<>();
    private Direction direction = Direction.RIGHT;

    private boolean growing = false;

    public Snake() {
        body.add(new Cell(10, 10)); // poziția inițială a șarpelui
    }

    public Snake(Cell initialPosition) {
        body.add(initialPosition); // inițializează șarpele cu o poziție specifică
    }

    public void setDirection(Direction direction) {
        if (this.direction.isOpposite(direction)) return; // previne șarpele să se întoarcă în direcția opusă
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public List<Cell> getBody() {
        return body;
    }

    public void update() {
        Cell head = body.getFirst();
        int newX = head.getX();
        int newY = head.getY();

        switch (direction) {
            case UP -> newY--;
            case DOWN -> newY++;
            case LEFT -> newX--;
            case RIGHT -> newX++;
        }

        // Teleportare la marginea scenei
        if (newX < 0) newX = SnakeGame.LEVEL_WIDTH - 1;
        if (newX >= SnakeGame.LEVEL_WIDTH) newX = 0;
        if (newY < 0) newY = SnakeGame.LEVEL_HEIGHT - 1;
        if (newY >= SnakeGame.LEVEL_HEIGHT) newY = 0;

        body.addFirst(new Cell(newX, newY));
        if (!growing) {
            body.removeLast();
        } else {
            growing = false;
        }
    }

    public void grow() {
        growing = true;
    }

    public boolean checkCollision(Food food) {
        Cell head = body.getFirst();
        return head.getX() == food.getX() && head.getY() == food.getY();
    }

    public boolean checkSelfCollision() {
        Cell head = body.getFirst();
        for (int i = 1; i < body.size(); i++) {
            if (head.getX() == body.get(i).getX() && head.getY() == body.get(i).getY()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkWallCollision(List<Wall> walls) {
        Cell head = body.getFirst();
        for (Wall wall : walls) {
            if (head.getX() == wall.getX() && head.getY() == wall.getY()) {
                return true;
            }
        }
        return false;
    }

    public void draw(GraphicsContext gc) {
        RadialGradient gradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.LIMEGREEN), new Stop(1, Color.DARKGREEN)
        );

        for (Cell cell : body) {
            gc.setFill(gradient);
            gc.fillRoundRect(cell.getX() * SnakeGame.CELL_SIZE, cell.getY() * SnakeGame.CELL_SIZE,
                    SnakeGame.CELL_SIZE, SnakeGame.CELL_SIZE, 10, 10);
        }
    }
}

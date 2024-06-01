package game.snakegame;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SnakeGame extends Application {
    public static final int CELL_SIZE = 20;
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 640;
    public static int LEVEL_WIDTH;
    public static int LEVEL_HEIGHT;

    private Snake snake;
    private Food food;
    private List<Wall> walls;
    private AnimationTimer timer;
    private GraphicsContext gc;
    private Scene gameScene;
    private Stage primaryStage;
    private Canvas canvas;
    private Label scoreLabel;
    private int score;
    private String currentLevel;

    private int gggg = 0;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        new MainMenu(primaryStage, this).show();
    }

    private void setupGameScene(int width, int height) {
//        int canvasWidth = width * CELL_SIZE;
//        int canvasHeight = height * CELL_SIZE;

        int canvasWidth = DEFAULT_WIDTH;
        int canvasHeight = DEFAULT_HEIGHT;

        canvas = new Canvas(canvasWidth, canvasHeight);
        gc = canvas.getGraphicsContext2D();

        StackPane canvasPane = new StackPane();
        canvasPane.getChildren().add(canvas);
        canvasPane.getStyleClass().add("canvas-pane");

        scoreLabel = new Label("Score: 0");
        scoreLabel.getStyleClass().add("score-label");

        VBox root = new VBox(20);
        root.getChildren().addAll(scoreLabel, canvasPane);
        root.getStyleClass().add("root");

        gameScene = new Scene(root, canvasWidth + 50, canvasHeight + 100); // Adăugăm spațiu pentru scor
        gameScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/game/snakegame/style.css")).toExternalForm());
        setupInputHandling();
    }

    private void setupInputHandling() {
        gameScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP) {
                if (snake.getDirection() != Direction.DOWN) {
                    snake.setDirection(Direction.UP);
                }
            } else if (e.getCode() == KeyCode.DOWN) {
                if (snake.getDirection() != Direction.UP) {
                    snake.setDirection(Direction.DOWN);
                }
            } else if (e.getCode() == KeyCode.LEFT) {
                if (snake.getDirection() != Direction.RIGHT) {
                    snake.setDirection(Direction.LEFT);
                }
            } else if (e.getCode() == KeyCode.RIGHT) {
                if (snake.getDirection() != Direction.LEFT) {
                    snake.setDirection(Direction.RIGHT);
                }
            }
        });
    }

    public void startGame() {
        new LevelSelectMenu(primaryStage, this).show();
    }

    public void startGameWithLevel(String levelFilename) {
        try {
            currentLevel = levelFilename;
            ObjectMapper objectMapper = new ObjectMapper();
            LevelData levelData = objectMapper.readValue(new File("src/main/resources/game/snakegame/levels/" + levelFilename), LevelData.class);

            setupGameScene(levelData.getWidth(), levelData.getHeight());
            LEVEL_WIDTH = levelData.getWidth();
            LEVEL_HEIGHT = levelData.getHeight();

            snake = new Snake();

            walls = new ArrayList<>();
            for (int y = 0; y < levelData.getHeight(); y++) {
                for (int x = 0; x < levelData.getWidth(); x++) {
                    String cell = levelData.getGrid().get(y).get(x);
                    if (cell.equals("WALL")) {
                        walls.add(new Wall(x, y));
                    } else if (cell.equals("SNAKE")) {
                        snake = new Snake(new Cell(x, y)); // inițializăm șarpele cu poziția inițială
                    }
                }
            }

            food = new Food(snake.getBody(), walls);

            score = 0;
            updateScoreLabel();

            timer = new AnimationTimer() {
                private long lastUpdate = 0;

                @Override
                public void handle(long now) {
                    if (now - lastUpdate >= 100_000_000) {
                        snake.update();

                        if(gggg < 5)
                        {
                            snake.grow();
                            gggg++;
                        }

                        if (food != null && snake.checkCollision(food)) {
                            snake.grow();
                            food.respawn(snake.getBody(), walls);
                            score++;
                            updateScoreLabel();
                        }
                        if (snake.checkSelfCollision() || (walls != null && snake.checkWallCollision(walls))) {
                            new GameOverMenu(primaryStage, SnakeGame.this).show();
                            stop();
                        }
                        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                        snake.draw(gc);
                        if (food != null) {
                            food.draw(gc);
                        }
                        if (walls != null) {
                            for (Wall wall : walls) {
                                wall.draw(gc);
                            }
                        }
                        lastUpdate = now;
                    }
                }
            };
            timer.start();

            primaryStage.setScene(gameScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restartGame() {
        gggg = 0;
        startGameWithLevel(currentLevel);
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

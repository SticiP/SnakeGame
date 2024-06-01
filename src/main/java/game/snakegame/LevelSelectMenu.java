package game.snakegame;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LevelSelectMenu {
    private final Stage primaryStage;
    private final SnakeGame snakeGame;
    private final List<String> levels;
    private int currentLevelIndex;
    private Canvas previewCanvas;
    private HBox levelIndicators;
    private Label levelNameLabel;

    public LevelSelectMenu(Stage primaryStage, SnakeGame snakeGame) {
        this.primaryStage = primaryStage;
        this.snakeGame = snakeGame;
        this.levels = loadLevels();
        this.currentLevelIndex = 0;
    }

    private List<String> loadLevels() {
        List<String> levels = new ArrayList<>();
        levels.add("level1.json");
        levels.add("level2.json");
        levels.add("level3.json");
        return levels;
    }

    public void show() {
        VBox menu = new VBox(20);
        menu.getStyleClass().add("level-select-menu");
        menu.setAlignment(javafx.geometry.Pos.CENTER);

        levelNameLabel = new Label("Level " + (currentLevelIndex + 1));
        updateLevelNameLabel();
        levelNameLabel.getStyleClass().add("level-name-label");

        Button prevButton = new Button("<");
        prevButton.getStyleClass().add("level-nav-button");
        prevButton.setOnAction(e -> {
            showPreviousLevel();
            updateLevelNameLabel(); // actualizăm eticheta după schimbarea nivelului
        });

        Button nextButton = new Button(">");
        nextButton.getStyleClass().add("level-nav-button");
        nextButton.setOnAction(e -> {
            showNextLevel();
            updateLevelNameLabel(); // actualizăm eticheta după schimbarea nivelului
        });

        HBox navButtons = new HBox(10, prevButton, nextButton);
        navButtons.setAlignment(javafx.geometry.Pos.CENTER);

        previewCanvas = new Canvas(375, 300);
        drawLevelPreview(currentLevelIndex);

        Button playButton = new Button("Play");
        playButton.getStyleClass().add("menu-button");
        playButton.setOnAction(e -> startSelectedLevel());

        levelIndicators = new HBox(10);
        levelIndicators.setAlignment(javafx.geometry.Pos.CENTER);
        updateLevelIndicators();

        menu.getChildren().addAll(levelNameLabel, navButtons, previewCanvas, playButton, levelIndicators);
        StackPane root = new StackPane();
        root.getChildren().add(menu);

        Scene levelSelectScene = new Scene(root, 800, 640);
        levelSelectScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/game/snakegame/style.css")).toExternalForm());

        primaryStage.setScene(levelSelectScene);
    }

    private void showPreviousLevel() {
        if (currentLevelIndex > 0) {
            currentLevelIndex--;
            drawLevelPreview(currentLevelIndex);
            updateLevelIndicators();
        }
    }

    private void showNextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
            drawLevelPreview(currentLevelIndex);
            updateLevelIndicators();
        }
    }

    private void drawLevelPreview(int levelIndex) {
        GraphicsContext gc = previewCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());

        // Fundalul mai deschis la culoare
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LevelData levelData = objectMapper.readValue(new File("src/main/resources/game/snakegame/levels/" + levels.get(levelIndex)), LevelData.class);

            int gridWidth = levelData.getWidth();
            int gridHeight = levelData.getHeight();
            double cellSize = Math.min(previewCanvas.getWidth() / gridWidth, previewCanvas.getHeight() / gridHeight);

            gc.setFill(Color.DARKGRAY);
            for (int y = 0; y < gridHeight; y++) {
                for (int x = 0; x < gridWidth; x++) {
                    String cell = levelData.getGrid().get(y).get(x);
                    if (cell.equals("WALL")) {
                        gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLevelIndicators() {
        levelIndicators.getChildren().clear();
        for (int i = 0; i < levels.size(); i++) {
            Circle indicator = new Circle(5, Color.GRAY);
            if (i == currentLevelIndex) {
                indicator.setFill(Color.WHITE);
            }
            levelIndicators.getChildren().add(indicator);
        }
    }

    private void updateLevelNameLabel() {
        levelNameLabel.setText("Level " + (currentLevelIndex + 1));
    }

    private void startSelectedLevel() {
        snakeGame.startGameWithLevel(levels.get(currentLevelIndex));
    }
}

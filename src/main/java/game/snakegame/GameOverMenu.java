package game.snakegame;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameOverMenu {
    private final Stage primaryStage;
    private final SnakeGame snakeGame;

    public GameOverMenu(Stage primaryStage, SnakeGame snakeGame) {
        this.primaryStage = primaryStage;
        this.snakeGame = snakeGame;
    }

    public void show() {
        VBox menu = new VBox(10);
        menu.getStyleClass().add("game-over-menu");

        Label gameOverLabel = new Label("Game Over!");
        gameOverLabel.getStyleClass().add("game-over-label");

        Button restartButton = new Button("Restart");
        restartButton.getStyleClass().add("menu-button");

        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("menu-button");

        restartButton.setOnAction(e -> snakeGame.restartGame());

        exitButton.setOnAction(e -> primaryStage.close());

        menu.getChildren().addAll(gameOverLabel, restartButton, exitButton);
        StackPane root = new StackPane();
        root.getChildren().add(menu);

        Scene gameOverScene = new Scene(root, SnakeGame.DEFAULT_WIDTH, SnakeGame.DEFAULT_HEIGHT);
        gameOverScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(gameOverScene);
    }
}

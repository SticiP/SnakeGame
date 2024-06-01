package game.snakegame;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class MainMenu {
    private final Stage primaryStage;
    private final SnakeGame snakeGame;

    public MainMenu(Stage primaryStage, SnakeGame snakeGame) {
        this.primaryStage = primaryStage;
        this.snakeGame = snakeGame;
    }

    public void show() {
        VBox menu = new VBox(20);
        menu.getStyleClass().add("main-menu");

        ImageView logo = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png"))));
        logo.setFitWidth(200);
        logo.setPreserveRatio(true);

        Button playButton = new Button("Play");
        playButton.getStyleClass().add("menu-button");
        playButton.setOnAction(e -> snakeGame.startGame());

        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("menu-button");
        exitButton.setOnAction(e -> primaryStage.close());

        menu.getChildren().addAll(logo, playButton, exitButton);
        StackPane root = new StackPane();
        root.getChildren().add(menu);

        Scene menuScene = new Scene(root, SnakeGame.DEFAULT_WIDTH, SnakeGame.DEFAULT_HEIGHT);
        menuScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(menuScene);
        primaryStage.setTitle("Snake Game");
        primaryStage.show();
    }
}

module game.snakegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens game.snakegame to javafx.fxml;
    exports game.snakegame;
}
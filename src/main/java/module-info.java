module com.game.university_platformer_game {
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.controls;
    requires java.sql;
    requires javafx.media;


    opens com.game.university_platformer_game to javafx.fxml;
    exports com.game.university_platformer_game;
}
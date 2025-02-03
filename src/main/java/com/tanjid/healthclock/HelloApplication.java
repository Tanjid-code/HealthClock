package com.tanjid.healthclock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        AnchorPane root = fxmlLoader.load();

        // Create Scene
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("HealthClock");

        // Set Application Icon
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")))); // Optional icon

        // Set the Scene and Show
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

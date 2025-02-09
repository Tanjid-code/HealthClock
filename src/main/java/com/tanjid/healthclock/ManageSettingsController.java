package com.tanjid.healthclock;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageSettingsController {

    @FXML
    private Button backButton;

    @FXML
    private void goBackToHome() {
        try {
            // Check if the backButton is properly initialized
            if (backButton != null) {
                Stage stage = (Stage) backButton.getScene().getWindow();

                // Load the Home page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
                Scene scene = new Scene(loader.load());

                // Set the scene and show
                stage.setScene(scene);
                stage.show();

                System.out.println("Navigated back to Home page.");
            } else {
                System.out.println("Error: backButton is null!");
            }
        } catch (IOException e) {
            System.out.println("Error loading home-view.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

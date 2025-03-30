package com.tanjid.healthclock;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class ViewMedicinesController {

    // Method for going back to the home page
    @FXML
    private void goBackToHome(ActionEvent event) {
        try {
            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the Home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Scene scene = new Scene(loader.load());

            // Set the new scene
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigated back to Home Page.");
        } catch (IOException e) {
            System.out.println("Error loading home-view.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Search medicines button handler
    @FXML
    private void searchMedicines(ActionEvent event) {
        System.out.println("Search Medicines button clicked!");
        // Add logic for searching medicines here
    }
}

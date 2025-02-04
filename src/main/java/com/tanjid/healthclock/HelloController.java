package com.tanjid.healthclock;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML
    private ImageView logoImage;

    @FXML
    private Text quoteText;  // Changed from quoteLabel to quoteText to match the FXML file.

    @FXML
    private Button btnAddPrescription, btnViewMedicines, btnManageSettings;

    @FXML
    public void initialize() {
        // Load the logo image
        Image image = new Image(getClass().getResourceAsStream("icon.png"));
        logoImage.setImage(image);
    }

    // Handle Add Prescription Button Click
    @FXML
    protected void onAddPrescription() {
        try {
            // Get the current stage
            Stage stage = (Stage) quoteText.getScene().getWindow();  // Changed quoteLabel to quoteText

            if (stage != null) {
                // Load the Add Prescription page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("add_prescription.fxml"));
                Scene scene = new Scene(loader.load());

                // Set the new scene
                stage.setScene(scene);
                stage.show();

                System.out.println("Navigated to Add Prescription Page.");
            } else {
                System.out.println("Stage is null. Unable to navigate.");
            }
        } catch (IOException e) {
            System.out.println("Error loading add-prescription.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Handle View Medicines & Alarm Button Click
    @FXML
    private void handleViewMedicinesClick() {
        System.out.println("View Medicines & Alarm button clicked!");
        // Logic to navigate to view medicines and alarm page
    }

    // Handle Manage Settings Button Click
    @FXML
    private void handleManageSettingsClick() {
        System.out.println("Manage Settings button clicked!");
        // Logic to navigate to settings page
    }
}

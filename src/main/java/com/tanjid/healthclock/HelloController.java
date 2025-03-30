package com.tanjid.healthclock;

import javafx.event.ActionEvent;
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
    private Text quoteText;  // Corrected to match the FXML file.

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
            Stage stage = (Stage) quoteText.getScene().getWindow();  // Use quoteText reference

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
            System.out.println("Error loading add_prescription.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Handle View Medicines & Alarm Button Click (Updated for New Page)
    @FXML
    protected void handleViewMedicinesClick() {
        try {
            // Get the current stage
            Stage stage = (Stage) btnViewMedicines.getScene().getWindow();

            if (stage != null) {
                // Load the View Medicines & Alarms page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("view-medicines.fxml"));
                Scene scene = new Scene(loader.load());

                // Set the new scene
                stage.setScene(scene);
                stage.show();

                System.out.println("Navigated to View Medicines & Alarms Page.");
            } else {
                System.out.println("Stage is null. Unable to navigate.");
            }
        } catch (IOException e) {
            System.out.println("Error loading view_medicines.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Handle Manage Settings Button Click
    @FXML
    public void onManageSettings(ActionEvent actionEvent) {
        try {
            // Get the current stage
            Stage stage = (Stage) quoteText.getScene().getWindow();  // Corrected to quoteText

            // Load the Manage Settings page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manage_settings.fxml"));
            Scene scene = new Scene(loader.load());

            // Set the new scene
            stage.setScene(scene);
            stage.show();

            System.out.println("Navigated to Manage Settings Page.");
        } catch (IOException e) {
            System.out.println("Error loading manage_settings.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

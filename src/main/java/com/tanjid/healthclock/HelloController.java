package com.tanjid.healthclock;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class HelloController {

    @FXML
    private ImageView logoImage;

    @FXML
    private Text quoteText;

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
    private void handleAddPrescriptionClick(ActionEvent event) {
        System.out.println("Add Prescription button clicked!");
        // Logic to navigate to add prescription page
    }

    // Handle View Medicines & Alarm Button Click
    @FXML
    private void handleViewMedicinesClick(ActionEvent event) {
        System.out.println("View Medicines & Alarm button clicked!");
        // Logic to navigate to view medicines and alarm page
    }

    // Handle Manage Settings Button Click
    @FXML
    private void handleManageSettingsClick(ActionEvent event) {
        System.out.println("Manage Settings button clicked!");
        // Logic to navigate to settings page
    }
}

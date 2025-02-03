package com.tanjid.healthclock;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.Random;

public class HomeController {
    @FXML
    private Label quoteLabel;

    private final String[] healthQuotes = {
            "Your Health is Your Greatest Wealth!",
            "Take care of your body. It’s the only place you have to live.",
            "A healthy outside starts from the inside.",
            "Good health is not something we can buy. However, it can be an investment.",
            "An apple a day keeps the doctor away."
    };

    @FXML
    public void initialize() {
        // Set a random health quote on startup
        Random random = new Random();
        quoteLabel.setText(healthQuotes[random.nextInt(healthQuotes.length)]);
    }

    @FXML
    protected void onAddPrescription() {
        System.out.println("Add Prescription Clicked!");
        // Load the add prescription page here
    }

    @FXML
    protected void onViewMedicines() {
        System.out.println("View Medicines & Alarms Clicked!");
        // Load the view medicines & alarms page here
    }

    @FXML
    protected void onManageSettings() {
        System.out.println("Manage Settings Clicked!");
        // Load the manage settings page here
    }
}

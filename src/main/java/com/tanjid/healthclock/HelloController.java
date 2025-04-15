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
import java.sql.*;
import java.time.LocalDate;

public class HelloController {

    @FXML
    private ImageView logoImage;

    @FXML
    private Text quoteText;

    @FXML
    private Button btnAddPrescription, btnViewMedicines, btnManageSettings;

    private static final String URL = "jdbc:mysql://localhost:3306/prescription_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @FXML
    public void initialize() {
        // Load logo image
        Image image = new Image(getClass().getResourceAsStream("icon.png"));
        logoImage.setImage(image);

        // Delete expired prescriptions
        deleteExpiredPrescriptions();
    }

    private void deleteExpiredPrescriptions() {
        String currentDate = LocalDate.now().toString();  // yyyy-MM-dd format

        String sql = "DELETE FROM prescriptions WHERE end_date < ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, currentDate);
            int deletedRows = stmt.executeUpdate();

            System.out.println("Expired prescriptions deleted: " + deletedRows);
        } catch (SQLException e) {
            System.out.println("Error deleting expired prescriptions: " + e.getMessage());
        }
    }

    @FXML
    protected void onAddPrescription() {
        try {
            Stage stage = (Stage) quoteText.getScene().getWindow();
            if (stage != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("add_prescription.fxml"));
                Scene scene = new Scene(loader.load());
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

    @FXML
    protected void handleViewMedicinesClick() {
        try {
            Stage stage = (Stage) btnViewMedicines.getScene().getWindow();
            if (stage != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("view-medicines.fxml"));
                Scene scene = new Scene(loader.load());
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

    @FXML
    public void onManageSettings(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) quoteText.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manage_settings.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
            System.out.println("Navigated to Manage Settings Page.");
        } catch (IOException e) {
            System.out.println("Error loading manage_settings.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

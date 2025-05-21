package com.tanjid.healthclock;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    private static final String DB_NAME = "prescription_db";
    private static final String URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @FXML
    public void initialize() {
        // Load logo
        Image image = new Image(getClass().getResourceAsStream("icon.png"));
        logoImage.setImage(image);

        // Ensure database and table exist
        checkAndCreateDatabase();
        checkAndCreateTable();

        // Delete old data if longest end_date < today
        deleteOldDataIfNeeded();
    }

    /** Check if database exists; create it if not */
    private void checkAndCreateDatabase() {
        try (Connection conn = DriverManager.getConnection(BASE_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Database checked/created successfully.");
        } catch (SQLException e) {
            System.out.println("Error checking/creating database: " + e.getMessage());
        }
    }

    /** Check if prescriptions table exists; create it if not */
    private void checkAndCreateTable() {
        String sql = "CREATE TABLE IF NOT EXISTS prescriptions (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "patient_name VARCHAR(255) NOT NULL," +
                "medicine_name VARCHAR(255) NOT NULL," +
                "morning_time VARCHAR(10)," +
                "afternoon_time VARCHAR(10)," +
                "evening_time VARCHAR(10)," +
                "alarm_time VARCHAR(50)," +
                "image_path TEXT," +
                "duration INT NOT NULL," +
                "start_date DATE NOT NULL," +
                "end_date DATE NOT NULL" +
                ")";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table checked/created successfully.");
        } catch (SQLException e) {
            System.out.println("Error checking/creating table: " + e.getMessage());
        }
    }

    /**
     * Delete all old data if the longest end_date in the table is before today.
     */
    private void deleteOldDataIfNeeded() {
        LocalDate today = LocalDate.now();

        String maxDateQuery = "SELECT MAX(end_date) AS max_end_date FROM prescriptions";
        String deleteSQL = "DELETE FROM prescriptions";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(maxDateQuery);
            if (rs.next()) {
                Date maxEndDateSql = rs.getDate("max_end_date");
                if (maxEndDateSql != null) {
                    LocalDate maxEndDate = maxEndDateSql.toLocalDate();

                    if (maxEndDate.isBefore(today)) {
                        int deletedCount = stmt.executeUpdate(deleteSQL);
                        System.out.println("Deleted " + deletedCount + " old prescriptions because max end_date was " + maxEndDate);
                    } else {
                        System.out.println("No old data deleted. Max end_date is " + maxEndDate);
                    }
                } else {
                    System.out.println("No data found in prescriptions table.");
                }
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("Error deleting old data: " + e.getMessage());
        }
    }

    /** Button: Navigate to Add Prescription */
    @FXML
    protected void onAddPrescription() {
        try {
            Stage stage = (Stage) quoteText.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_prescription.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println("Failed to load add_prescription.fxml: " + e.getMessage());
        }
    }

    /** Button: Navigate to View Medicines */
    @FXML
    protected void handleViewMedicinesClick() {
        try {
            Stage stage = (Stage) btnViewMedicines.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-medicines.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println("Failed to load view-medicines.fxml: " + e.getMessage());
        }
    }

    /** Button: Navigate to Manage Settings */
    @FXML
    public void onManageSettings() {
        try {
            Stage stage = (Stage) quoteText.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manage_settings.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println("Failed to load manage_settings.fxml: " + e.getMessage());
        }
    }
}

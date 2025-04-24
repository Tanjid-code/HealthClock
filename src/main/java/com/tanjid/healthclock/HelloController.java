package com.tanjid.healthclock;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

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

        // Clean up expired prescriptions
        deleteExpiredPrescriptions();

        // Set automatic alarms
        setAutomaticAlarms();
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

    /** Delete expired prescriptions based on duration */
    private void deleteExpiredPrescriptions() {
        String currentDate = LocalDate.now().toString();
        String sql = "DELETE FROM prescriptions WHERE DATE_ADD(start_date, INTERVAL duration DAY) < ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, currentDate);
            int deleted = stmt.executeUpdate();
            System.out.println("Deleted expired prescriptions: " + deleted);
        } catch (SQLException e) {
            System.out.println("Error deleting expired prescriptions: " + e.getMessage());
        }
    }

    /** Automatically set alarms for valid prescriptions */
    private void setAutomaticAlarms() {
        String today = LocalDate.now().toString();
        String sql = "SELECT patient_name, medicine_name, alarm_time, start_date, duration " +
                "FROM prescriptions WHERE start_date <= ? AND DATE_ADD(start_date, INTERVAL duration DAY) >= ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, today);
            stmt.setString(2, today);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String patientName = rs.getString("patient_name");
                String medicineName = rs.getString("medicine_name");
                String alarmTimeStr = rs.getString("alarm_time");
                LocalDate startDate = rs.getDate("start_date").toLocalDate();
                int duration = rs.getInt("duration");
                LocalDate endDate = startDate.plusDays(duration);

                if (alarmTimeStr != null && !alarmTimeStr.isEmpty()) {
                    for (String timeStr : alarmTimeStr.split(",")) {
                        LocalTime alarmTime = LocalTime.parse(timeStr.trim());
                        scheduleAlarm(patientName, medicineName, alarmTime, endDate);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error setting alarms: " + e.getMessage());
        }
    }

    /** Schedule a daily alarm at given time until end date */
    private void scheduleAlarm(String patientName, String medicineName, LocalTime alarmTime, LocalDate endDate) {
        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (LocalDate.now().isAfter(endDate)) {
                    this.cancel();
                    return;
                }

                Platform.runLater(() -> {
                    try {
                        AudioClip clip = new AudioClip(getClass().getResource("alert.mp3").toExternalForm());
                        clip.play();
                    } catch (Exception e) {
                        System.out.println("Error playing alarm sound: " + e.getMessage());
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Medicine Reminder");
                    alert.setHeaderText("Time to take medicine!");
                    alert.setContentText("Patient: " + patientName +
                            "\nMedicine: " + medicineName +
                            "\nTime: " + alarmTime);
                    alert.show();
                });
            }
        };

        LocalTime now = LocalTime.now();
        long delay = java.time.Duration.between(now, alarmTime).toMillis();
        if (delay < 0) delay += 24 * 60 * 60 * 1000;

        long repeatInterval = 24 * 60 * 60 * 1000; // 24 hours
        timer.scheduleAtFixedRate(task, delay, repeatInterval);
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
    public void onManageSettings(ActionEvent event) {
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

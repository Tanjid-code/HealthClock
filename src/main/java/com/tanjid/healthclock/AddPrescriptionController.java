package com.tanjid.healthclock;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.*;
import java.util.*;

public class AddPrescriptionController {

    @FXML
    private TextField patientNameField;

    @FXML
    private VBox medicineContainer;

    private static final int MAX_MEDICINES = 5;
    private final List<MedicineEntry> medicineEntries = new ArrayList<>();

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/health_clock";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private final Timer timer = new Timer(true); // Background daemon timer

    @FXML
    public void initialize() {
        addMedicineEntry(); // Add one default entry
    }

    @FXML
    private void goBackToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tanjid/healthclock/home-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) patientNameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load home page.");
        }
    }

    @FXML
    private void addMedicineEntry() {
        if (medicineEntries.size() >= MAX_MEDICINES) return;

        VBox entryBox = new VBox(5);
        entryBox.setStyle("-fx-border-color: #cccccc; -fx-padding: 10; -fx-background-radius: 5;");

        TextField medicineName = new TextField();
        medicineName.setPromptText("Medicine Name");

        TextField morningTime = new TextField();
        morningTime.setPromptText("Morning Time (08:00)");

        TextField afternoonTime = new TextField();
        afternoonTime.setPromptText("Afternoon Time (14:00)");

        TextField eveningTime = new TextField();
        eveningTime.setPromptText("Evening Time (20:00)");

        DatePicker endDate = new DatePicker();
        endDate.setPromptText("End Date");

        entryBox.getChildren().addAll(
                new Label("Medicine Name:"), medicineName,
                new Label("Morning Time:"), morningTime,
                new Label("Afternoon Time:"), afternoonTime,
                new Label("Evening Time:"), eveningTime,
                new Label("End Date:"), endDate
        );

        medicineContainer.getChildren().add(entryBox);
        medicineEntries.add(new MedicineEntry(medicineName, morningTime, afternoonTime, eveningTime, endDate));
    }

    @FXML
    private void saveAllPrescriptions() {
        String patientName = patientNameField.getText().trim();
        if (patientName.isEmpty()) {
            showAlert("Patient name is required.");
            return;
        }

        String[] names = new String[5];
        String[] morning = new String[5];
        String[] afternoon = new String[5];
        String[] evening = new String[5];
        java.sql.Date[] endDates = new java.sql.Date[5];

        for (int i = 0; i < medicineEntries.size(); i++) {
            MedicineEntry entry = medicineEntries.get(i);

            if (entry.medicineName.getText().trim().isEmpty() || entry.endDate.getValue() == null) {
                showAlert("Please complete all fields for medicine #" + (i + 1));
                return;
            }

            names[i] = entry.medicineName.getText().trim();
            morning[i] = entry.morningTime.getText().trim();
            afternoon[i] = entry.afternoonTime.getText().trim();
            evening[i] = entry.eveningTime.getText().trim();
            endDates[i] = java.sql.Date.valueOf(entry.endDate.getValue());
        }

        String sql = """
            INSERT INTO patient_prescriptions (
                patient_name,
                med1_name, med1_morning_time, med1_afternoon_time, med1_evening_time, med1_end_date,
                med2_name, med2_morning_time, med2_afternoon_time, med2_evening_time, med2_end_date,
                med3_name, med3_morning_time, med3_afternoon_time, med3_evening_time, med3_end_date,
                med4_name, med4_morning_time, med4_afternoon_time, med4_evening_time, med4_end_date,
                med5_name, med5_morning_time, med5_afternoon_time, med5_evening_time, med5_end_date
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientName);

            int index = 2;
            for (int i = 0; i < 5; i++) {
                stmt.setString(index++, names[i]);
                stmt.setString(index++, morning[i]);
                stmt.setString(index++, afternoon[i]);
                stmt.setString(index++, evening[i]);
                stmt.setDate(index++, endDates[i]);
            }

            stmt.executeUpdate();

            for (int i = 0; i < medicineEntries.size(); i++) {
                MedicineEntry entry = medicineEntries.get(i);
                scheduleAlarm(patientName, names[i], morning[i], endDates[i]);
                scheduleAlarm(patientName, names[i], afternoon[i], endDates[i]);
                scheduleAlarm(patientName, names[i], evening[i], endDates[i]);
            }

            showAlert("Prescription saved and alarms scheduled successfully.");
            clearForm();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to save prescription: " + e.getMessage());
        }
    }

    private void scheduleAlarm(String patient, String medicine, String timeStr, java.sql.Date endDate) {
        if (timeStr == null || timeStr.isEmpty()) return;

        try {
            String[] parts = timeStr.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            LocalDate end = endDate.toLocalDate();
            LocalDate today = LocalDate.now();

            for (LocalDate date = today; !date.isAfter(end); date = date.plusDays(1)) {
                LocalDateTime alarmTime = date.atTime(hour, minute);
                long delay = Duration.between(LocalDateTime.now(), alarmTime).toMillis();
                if (delay < 0) continue;

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            playSound();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Medicine Alarm");
                            alert.setHeaderText("Time to take medicine!");
                            alert.setContentText("Patient: " + patient + "\nMedicine: " + medicine);
                            alert.showAndWait();
                        });
                    }
                }, delay);
            }
        } catch (Exception e) {
            System.err.println("Invalid time format: " + timeStr);
        }
    }

    private void playSound() {
        try {
            AudioClip clip = new AudioClip(getClass().getResource("alarm.mp3").toString());
            clip.setCycleCount(AudioClip.INDEFINITE); // Loop the sound indefinitely
            clip.play();

            // Stop the clip after 10 seconds
            new Timer(true).schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(clip::stop);
                }
            }, 10_000); // 10 seconds in milliseconds

        } catch (Exception e) {
            System.err.println("Failed to play sound: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Prescription Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        patientNameField.clear();
        medicineContainer.getChildren().clear();
        medicineEntries.clear();
        addMedicineEntry();
    }

    private static class MedicineEntry {
        TextField medicineName;
        TextField morningTime;
        TextField afternoonTime;
        TextField eveningTime;
        DatePicker endDate;

        MedicineEntry(TextField name, TextField morning, TextField afternoon, TextField evening, DatePicker end) {
            this.medicineName = name;
            this.morningTime = morning;
            this.afternoonTime = afternoon;
            this.eveningTime = evening;
            this.endDate = end;
        }
    }
}

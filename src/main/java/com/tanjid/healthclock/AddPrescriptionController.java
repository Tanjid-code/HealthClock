package com.tanjid.healthclock;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Timer;
import java.util.TimerTask;

public class AddPrescriptionController {
    @FXML
    private ImageView prescriptionImageView;

    @FXML
    private TextField patientNameField, medicineNameField, durationField;
    @FXML
    private TextField morningTimeField, afternoonTimeField, eveningTimeField;

    private File selectedImageFile;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/prescription_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private final Timer alarmTimer = new Timer(true);

    @FXML
    private void initialize() {
        prescriptionImageView.setImage(new Image(getClass().getResourceAsStream("choose_image_icon.png")));
        prescriptionImageView.setOnMouseClicked(event -> uploadImage());
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedImageFile = fileChooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            prescriptionImageView.setImage(image);
        }
    }

    @FXML
    private void savePrescription() {
        String patientName = patientNameField.getText().trim();
        String medicineName = medicineNameField.getText().trim();
        String durationText = durationField.getText().trim();
        String morningTime = morningTimeField.getText().trim();
        String afternoonTime = afternoonTimeField.getText().trim();
        String eveningTime = eveningTimeField.getText().trim();
        String imagePath = selectedImageFile != null ? selectedImageFile.getAbsolutePath() : null;

        if (patientName.isEmpty() || medicineName.isEmpty() || durationText.isEmpty()) {
            showAlert("Error", "Please fill all required fields.");
            return;
        }

        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

        if (!morningTime.isEmpty() && !isValidTime(morningTime, timeFormat)) {
            showAlert("Error", "Morning time must be in HH:mm format (e.g., 08:00).");
            return;
        }
        if (!afternoonTime.isEmpty() && !isValidTime(afternoonTime, timeFormat)) {
            showAlert("Error", "Afternoon time must be in HH:mm format (e.g., 14:00).");
            return;
        }
        if (!eveningTime.isEmpty() && !isValidTime(eveningTime, timeFormat)) {
            showAlert("Error", "Evening time must be in HH:mm format (e.g., 20:00).");
            return;
        }

        int duration;
        try {
            duration = Integer.parseInt(durationText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Duration must be a number.");
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(duration);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO prescriptions (patient_name, medicine_name, morning_time, afternoon_time, evening_time, image_path, duration, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            pstmt.setString(1, patientName);
            pstmt.setString(2, medicineName);
            pstmt.setString(3, morningTime);
            pstmt.setString(4, afternoonTime);
            pstmt.setString(5, eveningTime);
            pstmt.setString(6, imagePath);
            pstmt.setInt(7, duration);
            pstmt.setDate(8, Date.valueOf(endDate));

            pstmt.executeUpdate();

            scheduleAlarms(patientName, medicineName, morningTime, afternoonTime, eveningTime, endDate);

            showAlert("Success", "Prescription saved successfully!\nEnd date: " + endDate);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save prescription.");
        }
    }

    private boolean isValidTime(String time, DateTimeFormatter formatter) {
        try {
            formatter.parse(time);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void scheduleAlarms(String patient, String medicine, String morning, String afternoon, String evening, LocalDate endDate) {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate today = LocalDate.now();

        scheduleAlarmTask("Morning", patient, medicine, morning, timeFormat, today, endDate);
        scheduleAlarmTask("Afternoon", patient, medicine, afternoon, timeFormat, today, endDate);
        scheduleAlarmTask("Evening", patient, medicine, evening, timeFormat, today, endDate);
    }

    private void scheduleAlarmTask(String period, String patient, String medicine, String timeStr, DateTimeFormatter formatter, LocalDate start, LocalDate end) {
        if (timeStr == null || timeStr.isEmpty()) return;

        try {
            LocalTime targetTime = LocalTime.parse(timeStr, formatter);

            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                LocalDateTime dateTime = LocalDateTime.of(date, targetTime);
                long delay = Duration.between(LocalDateTime.now(), dateTime).toMillis();

                if (delay > 0) {
                    alarmTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                playAlarmSoundRepeatedly();
                                showAlert("Alarm", "Time to take " + medicine + " (" + period + ") for patient: " + patient);
                            });
                        }
                    }, delay);
                }
            }
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
    }

    private void playAlarmSoundRepeatedly() {
        try {
            URL soundURL = getClass().getResource("alarm.mp3");
            if (soundURL != null) {
                AudioClip clip = new AudioClip(soundURL.toString());
                for (int i = 0; i < 10; i++) {
                    int finalI = i;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(clip::play);
                        }
                    }, finalI * 1000L);
                }
            } else {
                System.err.println("Alarm sound file not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBackToHome() throws IOException {
        Stage stage = (Stage) prescriptionImageView.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

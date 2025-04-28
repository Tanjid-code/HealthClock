package com.tanjid.healthclock;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;

import java.io.IOException;
import java.sql.*;
import java.time.*;
import java.util.Timer;
import java.util.TimerTask;

public class ViewMedicinesController {

    @FXML private TableView<Prescription> prescriptionTable;
    @FXML private TableColumn<Prescription, String> patientNameCol;
    @FXML private TableColumn<Prescription, String> medicineNameCol;
    @FXML private TableColumn<Prescription, String> morningCol;
    @FXML private TableColumn<Prescription, String> afternoonCol;
    @FXML private TableColumn<Prescription, String> eveningCol;
    @FXML private TableColumn<Prescription, Integer> durationCol;
    @FXML private TableColumn<Prescription, String> imagePathCol;
    @FXML private TableColumn<Prescription, String> endDateCol;
    @FXML private TextField searchField;

    private static final String URL = "jdbc:mysql://localhost:3306/prescription_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private ObservableList<Prescription> allPrescriptions = FXCollections.observableArrayList();
    private Timer timer = new Timer(true);

    @FXML
    public void initialize() {
        patientNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        medicineNameCol.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        morningCol.setCellValueFactory(new PropertyValueFactory<>("morningTime"));
        afternoonCol.setCellValueFactory(new PropertyValueFactory<>("afternoonTime"));
        eveningCol.setCellValueFactory(new PropertyValueFactory<>("eveningTime"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        imagePathCol.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        loadPrescriptionData();
    }

    private void loadPrescriptionData() {
        allPrescriptions.clear();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM prescriptions")) {

            while (rs.next()) {
                Prescription p = new Prescription(
                        rs.getString("patient_name"),
                        rs.getString("medicine_name"),
                        rs.getString("morning_time"),
                        rs.getString("afternoon_time"),
                        rs.getString("evening_time"),
                        rs.getInt("duration"),
                        rs.getString("image_path"),
                        rs.getString("end_date")
                );
                allPrescriptions.add(p);
            }
            prescriptionTable.setItems(allPrescriptions);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load data: " + e.getMessage());
        }
    }

    @FXML
    private void goBackToHome(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Could not load home view.");
        }
    }

    @FXML
    private void searchMedicines(ActionEvent event) {
        String keyword = searchField.getText().toLowerCase();
        if (keyword.isEmpty()) {
            prescriptionTable.setItems(allPrescriptions);
            return;
        }
        ObservableList<Prescription> filtered = FXCollections.observableArrayList();
        for (Prescription p : allPrescriptions) {
            if (p.getPatientName().toLowerCase().contains(keyword) ||
                    p.getMedicineName().toLowerCase().contains(keyword)) {
                filtered.add(p);
            }
        }
        prescriptionTable.setItems(filtered);
    }

    @FXML
    private void deleteSelected(ActionEvent event) {
        Prescription selected = prescriptionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a prescription to delete.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM prescriptions WHERE patient_name = ? AND medicine_name = ?")) {

            stmt.setString(1, selected.getPatientName());
            stmt.setString(2, selected.getMedicineName());
            stmt.executeUpdate();

            allPrescriptions.remove(selected);
            prescriptionTable.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Prescription deleted successfully.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not delete prescription.");
        }
    }

    @FXML
    private void editSelected(ActionEvent event) {
        Prescription selected = prescriptionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a prescription to edit.");
            return;
        }

        TextField patientField = new TextField(selected.getPatientName());
        TextField medicineField = new TextField(selected.getMedicineName());
        TextField morningField = new TextField(selected.getMorningTime());
        TextField afternoonField = new TextField(selected.getAfternoonTime());
        TextField eveningField = new TextField(selected.getEveningTime());
        TextField durationField = new TextField(String.valueOf(selected.getDuration()));
        TextField imagePathField = new TextField(selected.getImagePath());

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(new Label("Patient Name:"), 0, 0);
        grid.add(patientField, 1, 0);
        grid.add(new Label("Medicine Name:"), 0, 1);
        grid.add(medicineField, 1, 1);
        grid.add(new Label("Morning Time:"), 0, 2);
        grid.add(morningField, 1, 2);
        grid.add(new Label("Afternoon Time:"), 0, 3);
        grid.add(afternoonField, 1, 3);
        grid.add(new Label("Evening Time:"), 0, 4);
        grid.add(eveningField, 1, 4);
        grid.add(new Label("Duration (days):"), 0, 5);
        grid.add(durationField, 1, 5);
        grid.add(new Label("Image Path:"), 0, 6);
        grid.add(imagePathField, 1, 6);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Prescription");
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    int duration = Integer.parseInt(durationField.getText());
                    LocalDate endDate = LocalDate.now().plusDays(duration);

                    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                         PreparedStatement stmt = conn.prepareStatement(
                                 "UPDATE prescriptions SET patient_name = ?, medicine_name = ?, morning_time = ?, afternoon_time = ?, evening_time = ?, duration = ?, image_path = ?, end_date = ? WHERE patient_name = ? AND medicine_name = ?")) {

                        stmt.setString(1, patientField.getText());
                        stmt.setString(2, medicineField.getText());
                        stmt.setString(3, morningField.getText());
                        stmt.setString(4, afternoonField.getText());
                        stmt.setString(5, eveningField.getText());
                        stmt.setInt(6, duration);
                        stmt.setString(7, imagePathField.getText());
                        stmt.setString(8, endDate.toString());
                        stmt.setString(9, selected.getPatientName());
                        stmt.setString(10, selected.getMedicineName());

                        stmt.executeUpdate();

                        selected.setPatientName(patientField.getText());
                        selected.setMedicineName(medicineField.getText());
                        selected.setMorningTime(morningField.getText());
                        selected.setAfternoonTime(afternoonField.getText());
                        selected.setEveningTime(eveningField.getText());
                        selected.setDuration(duration);
                        selected.setImagePath(imagePathField.getText());
                        selected.setEndDate(endDate.toString());

                        prescriptionTable.refresh();
                        showAlert(Alert.AlertType.INFORMATION, "Updated", "Prescription updated successfully!");

                        // Only schedule alarms AFTER successful update
                        scheduleAllAlarms(selected);

                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Duration must be a number.");
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Could not update prescription.");
                }
            }
        });
    }

    private void scheduleAllAlarms(Prescription prescription) {
        scheduleAlarm(prescription.getMorningTime(), "Morning medicine for " + prescription.getPatientName());
        scheduleAlarm(prescription.getAfternoonTime(), "Afternoon medicine for " + prescription.getPatientName());
        scheduleAlarm(prescription.getEveningTime(), "Evening medicine for " + prescription.getPatientName());
    }

    private void scheduleAlarm(String time, String message) {
        if (time == null || time.isEmpty()) return;
        try {
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime alarmTime = now.withHour(hour).withMinute(minute).withSecond(0);

            if (alarmTime.isBefore(now)) {
                alarmTime = alarmTime.plusDays(1); // schedule for next day
            }

            long delayMillis = Duration.between(now, alarmTime).toMillis();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(() -> {
                        showAlertWithSound(Alert.AlertType.INFORMATION, "Alarm", message);
                    });
                }
            }, delayMillis);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlertWithSound(Alert.AlertType type, String title, String content) {
        String soundUrl = getClass().getResource("alarm.mp3").toString();
        AudioClip clip = new AudioClip(soundUrl);
        clip.play();

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

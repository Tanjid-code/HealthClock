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

import java.io.IOException;
import java.sql.*;

public class ViewMedicinesController {

    @FXML private TableView<Prescription> prescriptionTable;
    @FXML private TableColumn<Prescription, String> patientNameCol;
    @FXML private TableColumn<Prescription, String> medicineNameCol;
    @FXML private TableColumn<Prescription, Boolean> morningCol;
    @FXML private TableColumn<Prescription, Boolean> afternoonCol;
    @FXML private TableColumn<Prescription, Boolean> eveningCol;
    @FXML private TableColumn<Prescription, Integer> durationCol;
    @FXML private TableColumn<Prescription, String> imagePathCol;
    @FXML private TableColumn<Prescription, String> endDateCol;

    @FXML private TextField searchField;

    private static final String URL = "jdbc:mysql://localhost:3306/prescription_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private ObservableList<Prescription> allPrescriptions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        patientNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        medicineNameCol.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        morningCol.setCellValueFactory(new PropertyValueFactory<>("morning"));
        afternoonCol.setCellValueFactory(new PropertyValueFactory<>("afternoon"));
        eveningCol.setCellValueFactory(new PropertyValueFactory<>("evening"));
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
                        rs.getBoolean("morning"),
                        rs.getBoolean("afternoon"),
                        rs.getBoolean("evening"),
                        rs.getInt("duration"),
                        rs.getString("image_path"),
                        rs.getString("end_date")
                );
                allPrescriptions.add(p);
            }

            prescriptionTable.setItems(allPrescriptions);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
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
            System.out.println("Error loading home-view.fxml: " + e.getMessage());
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
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM prescriptions WHERE patient_name = ? AND medicine_name = ?")) {

            stmt.setString(1, selected.getPatientName());
            stmt.setString(2, selected.getMedicineName());
            stmt.executeUpdate();

            allPrescriptions.remove(selected);
            prescriptionTable.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Prescription deleted successfully.");
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

        // Create fields to edit data (excluding end date)
        TextField patientField = new TextField(selected.getPatientName());
        TextField medicineField = new TextField(selected.getMedicineName());
        CheckBox morningBox = new CheckBox("Morning");
        morningBox.setSelected(selected.isMorning());
        CheckBox afternoonBox = new CheckBox("Afternoon");
        afternoonBox.setSelected(selected.isAfternoon());
        CheckBox eveningBox = new CheckBox("Evening");
        eveningBox.setSelected(selected.isEvening());
        TextField durationField = new TextField(String.valueOf(selected.getDuration()));
        TextField imagePathField = new TextField(selected.getImagePath());

        // Layout for dialog
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(new Label("Patient Name:"), 0, 0);
        grid.add(patientField, 1, 0);
        grid.add(new Label("Medicine Name:"), 0, 1);
        grid.add(medicineField, 1, 1);
        grid.add(morningBox, 0, 2);
        grid.add(afternoonBox, 1, 2);
        grid.add(eveningBox, 2, 2);
        grid.add(new Label("Duration:"), 0, 3);
        grid.add(durationField, 1, 3);
        grid.add(new Label("Image Path:"), 0, 4);
        grid.add(imagePathField, 1, 4);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Prescription");
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    int duration = Integer.parseInt(durationField.getText());

                    // Calculate new end date
                    java.time.LocalDate today = java.time.LocalDate.now();
                    java.time.LocalDate endDate = today.plusDays(duration);

                    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                         PreparedStatement stmt = conn.prepareStatement(
                                 "UPDATE prescriptions SET patient_name = ?, medicine_name = ?, morning = ?, afternoon = ?, evening = ?, duration = ?, image_path = ?, end_date = ? WHERE patient_name = ? AND medicine_name = ?")) {

                        stmt.setString(1, patientField.getText());
                        stmt.setString(2, medicineField.getText());
                        stmt.setBoolean(3, morningBox.isSelected());
                        stmt.setBoolean(4, afternoonBox.isSelected());
                        stmt.setBoolean(5, eveningBox.isSelected());
                        stmt.setInt(6, duration);
                        stmt.setString(7, imagePathField.getText());
                        stmt.setString(8, endDate.toString()); // Save as yyyy-MM-dd
                        stmt.setString(9, selected.getPatientName());
                        stmt.setString(10, selected.getMedicineName());

                        stmt.executeUpdate();

                        // Update object
                        selected.setPatientName(patientField.getText());
                        selected.setMedicineName(medicineField.getText());
                        selected.setMorning(morningBox.isSelected());
                        selected.setAfternoon(afternoonBox.isSelected());
                        selected.setEvening(eveningBox.isSelected());
                        selected.setDuration(duration);
                        selected.setImagePath(imagePathField.getText());
                        selected.setEndDate(endDate.toString());

                        prescriptionTable.refresh();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Prescription updated successfully.");

                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Could not update prescription: " + e.getMessage());
                    }

                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Duration must be a number.");
                }
            }
        });
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

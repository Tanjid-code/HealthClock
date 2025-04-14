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

import java.io.IOException;
import java.sql.*;

public class ViewMedicinesController {

    @FXML private TableView<Prescription> prescriptionTable;
    @FXML private TableColumn<Prescription, String> patientNameCol;
    @FXML private TableColumn<Prescription, String> medicineNameCol;
    @FXML private TableColumn<Prescription, Boolean> morningCol;
    @FXML private TableColumn<Prescription, Boolean> afternoonCol;
    @FXML private TableColumn<Prescription, Boolean> eveningCol;
    @FXML private TableColumn<Prescription, Integer> durationCol; // New column
    @FXML private TableColumn<Prescription, String> imagePathCol;

    @FXML private TextField searchField;

    private static final String URL = "jdbc:mysql://localhost:3306/prescription_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private ObservableList<Prescription> allPrescriptions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Link table columns to Prescription model properties
        patientNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        medicineNameCol.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        morningCol.setCellValueFactory(new PropertyValueFactory<>("morning"));
        afternoonCol.setCellValueFactory(new PropertyValueFactory<>("afternoon"));
        eveningCol.setCellValueFactory(new PropertyValueFactory<>("evening"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration")); // Set duration
        imagePathCol.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        // Load data from the database
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
                        rs.getInt("duration"), // New
                        rs.getString("image_path")
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

        TextInputDialog dialog = new TextInputDialog(selected.getMedicineName());
        dialog.setTitle("Edit Medicine");
        dialog.setHeaderText("Edit medicine name for " + selected.getPatientName());
        dialog.setContentText("New medicine name:");

        dialog.showAndWait().ifPresent(newName -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE prescriptions SET medicine_name = ? WHERE patient_name = ? AND medicine_name = ?")) {

                stmt.setString(1, newName);
                stmt.setString(2, selected.getPatientName());
                stmt.setString(3, selected.getMedicineName());
                stmt.executeUpdate();

                selected.setMedicineName(newName);
                prescriptionTable.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Medicine name updated.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not update medicine.");
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

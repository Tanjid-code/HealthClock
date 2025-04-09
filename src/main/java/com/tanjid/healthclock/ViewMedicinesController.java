package com.tanjid.healthclock;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
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
    @FXML private TableColumn<Prescription, String> imagePathCol;

    private static final String URL = "jdbc:mysql://localhost:3306/prescription_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Initialize method runs automatically after FXML is loaded
    @FXML
    public void initialize() {
        // Link table columns to Prescription model properties
        patientNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        medicineNameCol.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        morningCol.setCellValueFactory(new PropertyValueFactory<>("morning"));
        afternoonCol.setCellValueFactory(new PropertyValueFactory<>("afternoon"));
        eveningCol.setCellValueFactory(new PropertyValueFactory<>("evening"));
        imagePathCol.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        // Load data from DB
        loadPrescriptionData();
    }

    // Load all prescription data from the database
    private void loadPrescriptionData() {
        ObservableList<Prescription> data = FXCollections.observableArrayList();

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
                        rs.getString("image_path")
                );
                data.add(p);
            }

            prescriptionTable.setItems(data);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Back to home button logic
    @FXML
    private void goBackToHome(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
            System.out.println("Navigated back to Home Page.");
        } catch (IOException e) {
            System.out.println("Error loading home-view.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Optional: Search logic placeholder
    @FXML
    private void searchMedicines(ActionEvent event) {
        System.out.println("Search Medicines button clicked!");
        // You can later add search functionality here if needed
    }
}

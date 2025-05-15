package com.tanjid.healthclock;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ViewMedicinesController {

    @FXML private TableView<Prescription> prescriptionTable;
    @FXML private TableColumn<Prescription, String> patientNameCol;

    @FXML private TableColumn<Prescription, String> med1NameCol, med1MorningCol, med1AfternoonCol, med1EveningCol, med1EndDateCol;
    @FXML private TableColumn<Prescription, String> med2NameCol, med2MorningCol, med2AfternoonCol, med2EveningCol, med2EndDateCol;
    @FXML private TableColumn<Prescription, String> med3NameCol, med3MorningCol, med3AfternoonCol, med3EveningCol, med3EndDateCol;
    @FXML private TableColumn<Prescription, String> med4NameCol, med4MorningCol, med4AfternoonCol, med4EveningCol, med4EndDateCol;
    @FXML private TableColumn<Prescription, String> med5NameCol, med5MorningCol, med5AfternoonCol, med5EveningCol, med5EndDateCol;

    @FXML private TextField searchField;

    private ObservableList<Prescription> prescriptionList;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/health_clock";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    @FXML
    public void initialize() {
        prescriptionList = FXCollections.observableArrayList();

        patientNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        med1NameCol.setCellValueFactory(new PropertyValueFactory<>("medicine1Name"));
        med1MorningCol.setCellValueFactory(new PropertyValueFactory<>("morningTime1"));
        med1AfternoonCol.setCellValueFactory(new PropertyValueFactory<>("afternoonTime1"));
        med1EveningCol.setCellValueFactory(new PropertyValueFactory<>("eveningTime1"));
        med1EndDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate1"));

        med2NameCol.setCellValueFactory(new PropertyValueFactory<>("medicine2Name"));
        med2MorningCol.setCellValueFactory(new PropertyValueFactory<>("morningTime2"));
        med2AfternoonCol.setCellValueFactory(new PropertyValueFactory<>("afternoonTime2"));
        med2EveningCol.setCellValueFactory(new PropertyValueFactory<>("eveningTime2"));
        med2EndDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate2"));

        med3NameCol.setCellValueFactory(new PropertyValueFactory<>("medicine3Name"));
        med3MorningCol.setCellValueFactory(new PropertyValueFactory<>("morningTime3"));
        med3AfternoonCol.setCellValueFactory(new PropertyValueFactory<>("afternoonTime3"));
        med3EveningCol.setCellValueFactory(new PropertyValueFactory<>("eveningTime3"));
        med3EndDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate3"));

        med4NameCol.setCellValueFactory(new PropertyValueFactory<>("medicine4Name"));
        med4MorningCol.setCellValueFactory(new PropertyValueFactory<>("morningTime4"));
        med4AfternoonCol.setCellValueFactory(new PropertyValueFactory<>("afternoonTime4"));
        med4EveningCol.setCellValueFactory(new PropertyValueFactory<>("eveningTime4"));
        med4EndDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate4"));

        med5NameCol.setCellValueFactory(new PropertyValueFactory<>("medicine5Name"));
        med5MorningCol.setCellValueFactory(new PropertyValueFactory<>("morningTime5"));
        med5AfternoonCol.setCellValueFactory(new PropertyValueFactory<>("afternoonTime5"));
        med5EveningCol.setCellValueFactory(new PropertyValueFactory<>("eveningTime5"));
        med5EndDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate5"));

        loadData();
    }

    private void loadData() {
        prescriptionList.clear();
        String sql = "SELECT * FROM patient_prescriptions";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Prescription p = new Prescription(
                        rs.getString("patient_name"),
                        rs.getString("med1_name"), rs.getString("med1_morning_time"), rs.getString("med1_afternoon_time"), rs.getString("med1_evening_time"), rs.getString("med1_end_date"),
                        rs.getString("med2_name"), rs.getString("med2_morning_time"), rs.getString("med2_afternoon_time"), rs.getString("med2_evening_time"), rs.getString("med2_end_date"),
                        rs.getString("med3_name"), rs.getString("med3_morning_time"), rs.getString("med3_afternoon_time"), rs.getString("med3_evening_time"), rs.getString("med3_end_date"),
                        rs.getString("med4_name"), rs.getString("med4_morning_time"), rs.getString("med4_afternoon_time"), rs.getString("med4_evening_time"), rs.getString("med4_end_date"),
                        rs.getString("med5_name"), rs.getString("med5_morning_time"), rs.getString("med5_afternoon_time"), rs.getString("med5_evening_time"), rs.getString("med5_end_date")
                );
                prescriptionList.add(p);
            }

            prescriptionTable.setItems(prescriptionList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to load prescriptions: " + e.getMessage());
        }
    }

    @FXML
    private void searchMedicines(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<Prescription> filteredList = FXCollections.observableArrayList();

        for (Prescription p : prescriptionList) {
            if (p.getPatientName().toLowerCase().contains(searchText) ||
                    p.getMedicine1Name().toLowerCase().contains(searchText) ||
                    p.getMedicine2Name().toLowerCase().contains(searchText) ||
                    p.getMedicine3Name().toLowerCase().contains(searchText) ||
                    p.getMedicine4Name().toLowerCase().contains(searchText) ||
                    p.getMedicine5Name().toLowerCase().contains(searchText)) {
                filteredList.add(p);
            }
        }

        prescriptionTable.setItems(filteredList);
    }

    @FXML
    private void editSelected(ActionEvent event) {
        Prescription selected = prescriptionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditPrescription.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                EditPrescriptionController controller = loader.getController();
                controller.setPrescription(selected);
                stage.show();
            } catch (IOException e) {
                showAlert("Failed to load edit window: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a prescription to edit.");
        }
    }

    @FXML
    private void deleteSelected(ActionEvent event) {
        Prescription selected = prescriptionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this prescription?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait();
            if (confirm.getResult() == ButtonType.YES) {
                deletePrescriptionFromDatabase(selected);
                prescriptionList.remove(selected);
            }
        } else {
            showAlert("Please select a prescription to delete.");
        }
    }

    private void deletePrescriptionFromDatabase(Prescription p) {
        String sql = "DELETE FROM patient_prescriptions WHERE patient_name = ? AND med1_name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getPatientName());
            stmt.setString(2, p.getMedicine1Name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to delete from database: " + e.getMessage());
        }
    }

    @FXML
    private void goBackToHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tanjid/healthclock/home-view.fxml"));
            Stage stage = (Stage) prescriptionTable.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Unable to return to home: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

package com.tanjid.healthclock;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL; // For checking resource
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
    private static final String DB_PASS = ""; // Consider more secure password management

    @FXML
    public void initialize() {
        prescriptionList = FXCollections.observableArrayList();

        // --- Column Setup ---
        // IMPORTANT: Your Prescription.java class MUST have public getter methods
        // (e.g., getPatientName(), getMedicine1Name()) for these to work.
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

        prescriptionTable.setPlaceholder(new Label("No prescriptions found. Add new ones or check search criteria."));
        loadData();
    }

    private void loadData() {
        // Consider adding a Prescription ID (e.g., from an auto-increment column in DB)
        // to the Prescription class and fetching it here. This makes editing/deleting more robust.
        prescriptionList.clear();
        String sql = "SELECT * FROM patient_prescriptions"; // Ensure this table and columns exist
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Ensure your Prescription class constructor matches these fields in order and type
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
            showAlert("Database Error: Failed to load prescriptions: " + e.getMessage());
        } catch (Exception e) { // Catch other potential errors during data loading
            e.printStackTrace();
            showAlert("Error: An unexpected error occurred while loading data: " + e.getMessage());
        }
    }

    @FXML
    private void searchMedicines(ActionEvent event) {
        String searchText = searchField.getText();
        if (searchText == null) { // Basic null check for text from field
            searchText = "";
        }
        searchText = searchText.toLowerCase().trim();

        if (searchText.isEmpty()) {
            prescriptionTable.setItems(prescriptionList); // Reset to full list if search is empty
            return;
        }

        ObservableList<Prescription> filteredList = FXCollections.observableArrayList();
        for (Prescription p : prescriptionList) {
            boolean match = false;
            // Null-safe checks for each searchable field
            if (p.getPatientName() != null && p.getPatientName().toLowerCase().contains(searchText)) {
                match = true;
            } else if (p.getMedicine1Name() != null && p.getMedicine1Name().toLowerCase().contains(searchText)) {
                match = true;
            } else if (p.getMedicine2Name() != null && p.getMedicine2Name().toLowerCase().contains(searchText)) {
                match = true;
            } else if (p.getMedicine3Name() != null && p.getMedicine3Name().toLowerCase().contains(searchText)) {
                match = true;
            } else if (p.getMedicine4Name() != null && p.getMedicine4Name().toLowerCase().contains(searchText)) {
                match = true;
            } else if (p.getMedicine5Name() != null && p.getMedicine5Name().toLowerCase().contains(searchText)) {
                match = true;
            }
            // Add more `else if` conditions here if you want to search other fields

            if (match) {
                filteredList.add(p);
            }
        }
        prescriptionTable.setItems(filteredList);
    }


    @FXML
    private void editSelected(ActionEvent event) {
        // STEP 1: Check if this method is even called.
        // Look for this message in your console when you click the edit button.
        // If you don't see it, the onAction="#editSelected" is likely missing or incorrect in your FXML.
        System.out.println("DEBUG: editSelected method called.");

        Prescription selected = prescriptionTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No prescription selected. Please select a prescription from the table to edit.");
            System.out.println("DEBUG: Edit failed - no item selected in the table.");
            return;
        }

        System.out.println("DEBUG: Attempting to edit prescription for patient: " + selected.getPatientName());

        try {
            String fxmlPath = "/com/tanjid/healthclock/EditPrescription.fxml";
            URL fxmlUrl = getClass().getResource(fxmlPath);

            // STEP 2: Check if the FXML file is found.
            if (fxmlUrl == null) {
                showAlert("Error: Cannot find FXML file: " + fxmlPath +
                        "\n\nTroubleshooting:\n" +
                        "1. Ensure 'EditPrescription.fxml' is in the 'com/tanjid/healthclock' package folder.\n" +
                        "2. Ensure the path starts with a '/' if it's relative to the classpath root.\n" +
                        "3. Ensure the file is included in your build (e.g., marked as a resource in Maven/Gradle).");
                System.err.println("ERROR: EditPrescription.fxml not found at path: " + fxmlPath);
                return;
            }
            System.out.println("DEBUG: FXML file found at: " + fxmlUrl.toString());

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Scene scene = new Scene(loader.load()); // This line can throw IOException or IllegalStateException

            Stage stage = new Stage();
            stage.setTitle("Edit Prescription Details");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Set the owner window (the current window) to make the new stage modal to it.
            if (prescriptionTable != null && prescriptionTable.getScene() != null) {
                stage.initOwner(prescriptionTable.getScene().getWindow());
            }

            // STEP 3: Check if the controller for EditPrescription.fxml is loaded.
            EditPrescriptionController controller = loader.getController();
            if (controller == null) {
                showAlert("Error: Failed to load the controller for 'EditPrescription.fxml'.\n\n" +
                        "Troubleshooting:\n" +
                        "1. Ensure 'EditPrescription.fxml' has the correct 'fx:controller' attribute set, e.g.,\n" +
                        "   fx:controller=\"com.tanjid.healthclock.EditPrescriptionController\"\n" +
                        "2. Ensure 'EditPrescriptionController.java' exists and compiles without errors.");
                System.err.println("ERROR: EditPrescriptionController is null. Check fx:controller in EditPrescription.fxml.");
                return;
            }
            System.out.println("DEBUG: EditPrescriptionController loaded successfully.");

            // STEP 4: Call methods on EditPrescriptionController.
            // These methods MUST exist in your EditPrescriptionController.java
            // public void setPrescription(Prescription prescription)
            // public void setOnSave(Runnable onSaveCallback)
            try {
                controller.setPrescription(selected);
                System.out.println("DEBUG: Passed selected prescription to EditPrescriptionController.");

                // The setOnSave method is optional but highly recommended for refreshing data.
                // If EditPrescriptionController doesn't have it, this will cause an error (caught below).
                controller.setOnSave(() -> {
                    System.out.println("DEBUG: Callback from EditPrescription save: Reloading data.");
                    loadData(); // Reloads data in the main table
                    prescriptionTable.refresh(); // Visually refreshes the table
                });
                System.out.println("DEBUG: Save callback set for EditPrescriptionController.");

            } catch (Exception e) {
                showAlert("Error interacting with EditPrescriptionController: " + e.getMessage() +
                        "\n\nTroubleshooting:\n" +
                        "1. Ensure 'EditPrescriptionController.java' has public methods:\n" +
                        "   'void setPrescription(Prescription p)'\n" +
                        "   'void setOnSave(Runnable r)' (if used)\n" +
                        "2. Check for errors within these methods in EditPrescriptionController.");
                System.err.println("ERROR: Exception when calling setPrescription or setOnSave on EditPrescriptionController: " + e.getMessage());
                e.printStackTrace();
                return; // Stop if controller interaction fails
            }

            stage.showAndWait(); // Display the edit window and wait for it to be closed.
            System.out.println("DEBUG: Edit window closed.");

        } catch (IOException e) {
            // This usually means an issue with loading the FXML file itself (e.g., syntax error in FXML, incorrect fx:id).
            showAlert("Error Loading Edit Window (IOException): " + e.getMessage() +
                    "\n\nTroubleshooting:\n" +
                    "1. Check 'EditPrescription.fxml' for syntax errors.\n" +
                    "2. Ensure all fx:id attributes in the FXML match fields in 'EditPrescriptionController'.");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // FXMLLoader.load() can also throw IllegalStateException (e.g., fx:controller class not found or issues during its initialization).
            showAlert("Error Loading Edit Window (IllegalStateException): " + e.getMessage() +
                    "\n\nTroubleshooting:\n" +
                    "1. Verify the 'fx:controller' path in 'EditPrescription.fxml'.\n" +
                    "2. Check the 'initialize()' method of 'EditPrescriptionController' for errors.");
            e.printStackTrace();
        }
        catch (Exception e) { // Catch any other unexpected errors during the process
            showAlert("An unexpected error occurred while trying to open the edit window: " + e.getMessage());
            e.printStackTrace();
            System.err.println("ERROR: Unexpected error in editSelected: " + e.toString());
        }
    }

    @FXML
    private void deleteSelected(ActionEvent event) {
        Prescription selected = prescriptionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete the prescription for '" + selected.getPatientName() + "'?",
                    ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Confirm Deletion");
            // Set owner to ensure dialog is on top and focused
            if (prescriptionTable != null && prescriptionTable.getScene() != null) {
                confirm.initOwner(prescriptionTable.getScene().getWindow());
            }
            confirm.showAndWait();

            if (confirm.getResult() == ButtonType.YES) {
                if (deletePrescriptionFromDatabase(selected)) {
                    prescriptionList.remove(selected); // ObservableList will notify TableView
                    showAlert("Prescription for '" + selected.getPatientName() + "' deleted successfully.");
                }
                // Alert for deletion failure is shown within deletePrescriptionFromDatabase
            }
        } else {
            showAlert("Please select a prescription to delete.");
        }
    }

    private boolean deletePrescriptionFromDatabase(Prescription p) {
        // WARNING: Deleting by patient_name and med1_name can be risky if this combination is not unique.
        // Using a unique Prescription ID (if available) is much safer: "DELETE FROM patient_prescriptions WHERE id = ?"
        String sql = "DELETE FROM patient_prescriptions WHERE patient_name = ? AND med1_name = ?";

        // Prevent SQL errors if key parts are null
        if (p.getPatientName() == null || p.getMedicine1Name() == null) {
            showAlert("Error: Cannot delete prescription. Patient name or Medicine 1 name is missing in the selected record.");
            System.err.println("ERROR: Attempted to delete prescription with null key parts. Patient: " + p.getPatientName() + ", Med1: " + p.getMedicine1Name());
            return false;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getPatientName());
            stmt.setString(2, p.getMedicine1Name()); // Ensure getMedicine1Name() provides the correct value
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("DEBUG: Successfully deleted prescription from DB for: " + p.getPatientName());
                return true;
            } else {
                showAlert("Deletion Failed: No matching prescription found in the database for patient '" +
                        p.getPatientName() + "' with medicine 1 '" + p.getMedicine1Name() + "'.");
                System.out.println("INFO: No rows deleted from DB for Patient: " + p.getPatientName() + ", Med1: " + p.getMedicine1Name());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error: Failed to delete prescription: " + e.getMessage());
            return false;
        }
    }


    @FXML
    private void goBackToHome(ActionEvent event) {
        try {
            String fxmlPath = "/com/tanjid/healthclock/home-view.fxml";
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                showAlert("Error: Cannot find FXML file: " + fxmlPath +
                        "\nEnsure 'home-view.fxml' is in the 'com/tanjid/healthclock' package folder.");
                System.err.println("ERROR: home-view.fxml not found at path: " + fxmlPath);
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Stage stage = (Stage) prescriptionTable.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Health Clock - Home");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error: Unable to return to home screen: " + e.getMessage());
        } catch (Exception e) { // Catch any other unexpected errors
            e.printStackTrace();
            showAlert("Error: An unexpected error occurred while navigating to home: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert.AlertType alertType = Alert.AlertType.INFORMATION; // Default
        String title = "Information";

        String lowerMsg = msg.toLowerCase();
        if (lowerMsg.contains("error") || lowerMsg.contains("failed") || lowerMsg.contains("cannot find")) {
            alertType = Alert.AlertType.ERROR;
            title = "Error";
        } else if (lowerMsg.contains("warning") || lowerMsg.contains("select a prescription")) {
            alertType = Alert.AlertType.WARNING;
            title = "Warning";
        } else if (lowerMsg.contains("success") || lowerMsg.contains("deleted successfully")) {
            alertType = Alert.AlertType.INFORMATION;
            title = "Success";
        }


        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        // Ensure dialog is on top of the current window if possible
        if (prescriptionTable != null && prescriptionTable.getScene() != null && prescriptionTable.getScene().getWindow() != null) {
            alert.initOwner(prescriptionTable.getScene().getWindow());
        }
        alert.showAndWait();
    }
}
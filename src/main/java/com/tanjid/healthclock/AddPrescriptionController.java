package com.tanjid.healthclock;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class AddPrescriptionController {
    @FXML
    private ImageView prescriptionImageView;
    @FXML
    private TextField patientNameField, medicineNameField;
    @FXML
    private CheckBox morningCheck, afternoonCheck, eveningCheck;

    private File selectedImageFile;

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedImageFile = fileChooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            prescriptionImageView.setImage(new Image(selectedImageFile.toURI().toString()));
        }
    }

    @FXML
    private void savePrescription() {
        String patientName = patientNameField.getText();
        String medicineName = medicineNameField.getText();
        boolean morning = morningCheck.isSelected();
        boolean afternoon = afternoonCheck.isSelected();
        boolean evening = eveningCheck.isSelected();

        if (patientName.isEmpty() || medicineName.isEmpty()) {
            showAlert("Error", "Please fill all required fields.");
            return;
        }

        // Here, you would save this data to a database or file
        System.out.println("Saved Prescription: " + patientName + " - " + medicineName + " | Morning: " + morning + " | Afternoon: " + afternoon + " | Evening: " + evening);

        showAlert("Success", "Prescription saved successfully!");
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

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddPrescriptionController {
    @FXML
    private ImageView prescriptionImageView;
    @FXML
    private TextField patientNameField, medicineNameField;
    @FXML
    private CheckBox morningCheck, afternoonCheck, eveningCheck;

    private File selectedImageFile;

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/prescription_db";
    private static final String USER = "root"; // Change if needed
    private static final String PASSWORD = ""; // Set your MySQL password

    @FXML
    private void initialize() {
        // Set the default image
        prescriptionImageView.setImage(new Image(getClass().getResourceAsStream("choose_image_icon.png")));

        // Make image clickable for upload
        prescriptionImageView.setOnMouseClicked(event -> uploadImage());
    }

    @FXML
    private void uploadImage() {
        // Open file chooser to select an image
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedImageFile = fileChooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            // Set the selected image in ImageView
            Image image = new Image(selectedImageFile.toURI().toString());
            prescriptionImageView.setImage(image);
        }
    }

    @FXML
    private void savePrescription() {
        String patientName = patientNameField.getText();
        String medicineName = medicineNameField.getText();
        boolean morning = morningCheck.isSelected();
        boolean afternoon = afternoonCheck.isSelected();
        boolean evening = eveningCheck.isSelected();
        String imagePath = selectedImageFile != null ? selectedImageFile.getAbsolutePath() : null;

        if (patientName.isEmpty() || medicineName.isEmpty()) {
            showAlert("Error", "Please fill all required fields.");
            return;
        }

        // Save to database
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO prescriptions (patient_name, medicine_name, morning, afternoon, evening, image_path) VALUES (?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, patientName);
            pstmt.setString(2, medicineName);
            pstmt.setBoolean(3, morning);
            pstmt.setBoolean(4, afternoon);
            pstmt.setBoolean(5, evening);
            pstmt.setString(6, imagePath); // Allow NULL value for image path

            pstmt.executeUpdate();
            showAlert("Success", "Prescription saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save prescription.");
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

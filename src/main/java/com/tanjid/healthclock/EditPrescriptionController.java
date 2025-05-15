package com.tanjid.healthclock;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class EditPrescriptionController {

    // FXML elements for the prescription form
    @FXML private TextField patientNameField;
    @FXML private TextField medicine1NameField;
    @FXML private TextField morningTime1Field;
    @FXML private TextField afternoonTime1Field;
    @FXML private TextField eveningTime1Field;
    @FXML private TextField endDate1Field;

    @FXML private TextField medicine2NameField;
    @FXML private TextField morningTime2Field;
    @FXML private TextField afternoonTime2Field;
    @FXML private TextField eveningTime2Field;
    @FXML private TextField endDate2Field;

    @FXML private TextField medicine3NameField;
    @FXML private TextField morningTime3Field;
    @FXML private TextField afternoonTime3Field;
    @FXML private TextField eveningTime3Field;
    @FXML private TextField endDate3Field;

    @FXML private TextField medicine4NameField;
    @FXML private TextField morningTime4Field;
    @FXML private TextField afternoonTime4Field;
    @FXML private TextField eveningTime4Field;
    @FXML private TextField endDate4Field;

    @FXML private TextField medicine5NameField;
    @FXML private TextField morningTime5Field;
    @FXML private TextField afternoonTime5Field;
    @FXML private TextField eveningTime5Field;
    @FXML private TextField endDate5Field;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    // Reference to the Prescription object being edited
    private Prescription prescription;

    // This method will set the prescription object and initialize the form with existing prescription details
    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
        initializeFields();
    }

    // Method to initialize form fields with prescription data
    private void initializeFields() {
        if (prescription != null) {
            patientNameField.setText(prescription.getPatientName());
            medicine1NameField.setText(prescription.getMedicine1Name());
            morningTime1Field.setText(prescription.getMorningTime1());
            afternoonTime1Field.setText(prescription.getAfternoonTime1());
            eveningTime1Field.setText(prescription.getEveningTime1());
            endDate1Field.setText(prescription.getEndDate1());

            medicine2NameField.setText(prescription.getMedicine2Name());
            morningTime2Field.setText(prescription.getMorningTime2());
            afternoonTime2Field.setText(prescription.getAfternoonTime2());
            eveningTime2Field.setText(prescription.getEveningTime2());
            endDate2Field.setText(prescription.getEndDate2());

            medicine3NameField.setText(prescription.getMedicine3Name());
            morningTime3Field.setText(prescription.getMorningTime3());
            afternoonTime3Field.setText(prescription.getAfternoonTime3());
            eveningTime3Field.setText(prescription.getEveningTime3());
            endDate3Field.setText(prescription.getEndDate3());

            medicine4NameField.setText(prescription.getMedicine4Name());
            morningTime4Field.setText(prescription.getMorningTime4());
            afternoonTime4Field.setText(prescription.getAfternoonTime4());
            eveningTime4Field.setText(prescription.getEveningTime4());
            endDate4Field.setText(prescription.getEndDate4());

            medicine5NameField.setText(prescription.getMedicine5Name());
            morningTime5Field.setText(prescription.getMorningTime5());
            afternoonTime5Field.setText(prescription.getAfternoonTime5());
            eveningTime5Field.setText(prescription.getEveningTime5());
            endDate5Field.setText(prescription.getEndDate5());
        }
    }

    // Method to handle the save button click
    @FXML
    public void handleSave(ActionEvent event) {
        // Save the updated prescription details
        prescription.setPatientName(patientNameField.getText());
        prescription.setMedicine1Name(medicine1NameField.getText());
        prescription.setMorningTime1(morningTime1Field.getText());
        prescription.setAfternoonTime1(afternoonTime1Field.getText());
        prescription.setEveningTime1(eveningTime1Field.getText());
        prescription.setEndDate1(endDate1Field.getText());

        prescription.setMedicine2Name(medicine2NameField.getText());
        prescription.setMorningTime2(morningTime2Field.getText());
        prescription.setAfternoonTime2(afternoonTime2Field.getText());
        prescription.setEveningTime2(eveningTime2Field.getText());
        prescription.setEndDate2(endDate2Field.getText());

        prescription.setMedicine3Name(medicine3NameField.getText());
        prescription.setMorningTime3(morningTime3Field.getText());
        prescription.setAfternoonTime3(afternoonTime3Field.getText());
        prescription.setEveningTime3(eveningTime3Field.getText());
        prescription.setEndDate3(endDate3Field.getText());

        prescription.setMedicine4Name(medicine4NameField.getText());
        prescription.setMorningTime4(morningTime4Field.getText());
        prescription.setAfternoonTime4(afternoonTime4Field.getText());
        prescription.setEveningTime4(eveningTime4Field.getText());
        prescription.setEndDate4(endDate4Field.getText());

        prescription.setMedicine5Name(medicine5NameField.getText());
        prescription.setMorningTime5(morningTime5Field.getText());
        prescription.setAfternoonTime5(afternoonTime5Field.getText());
        prescription.setEveningTime5(eveningTime5Field.getText());
        prescription.setEndDate5(endDate5Field.getText());

        // Optionally: Save the updated prescription to the database or file here
    }

    // Method to handle the cancel button click
    @FXML
    public void handleCancel(ActionEvent event) {
        // Close the edit window or go back to the previous screen
    }
}

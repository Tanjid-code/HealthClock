package com.tanjid.healthclock;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class EditPrescriptionController {

    // Patient Name
    @FXML private TextField patientNameField;

    // Medicine 1
    @FXML private TextField medicine1NameField;
    @FXML private TextField med1MorningTimeField;
    @FXML private TextField med1AfternoonTimeField;
    @FXML private TextField med1EveningTimeField;
    @FXML private DatePicker med1EndDatePicker;

    // Medicine 2
    @FXML private TextField medicine2NameField;
    @FXML private TextField med2MorningTimeField;
    @FXML private TextField med2AfternoonTimeField;
    @FXML private TextField med2EveningTimeField;
    @FXML private DatePicker med2EndDatePicker;

    // Medicine 3
    @FXML private TextField medicine3NameField;
    @FXML private TextField med3MorningTimeField;
    @FXML private TextField med3AfternoonTimeField;
    @FXML private TextField med3EveningTimeField;
    @FXML private DatePicker med3EndDatePicker;

    // Medicine 4
    @FXML private TextField medicine4NameField;
    @FXML private TextField med4MorningTimeField;
    @FXML private TextField med4AfternoonTimeField;
    @FXML private TextField med4EveningTimeField;
    @FXML private DatePicker med4EndDatePicker;

    // Medicine 5
    @FXML private TextField medicine5NameField;
    @FXML private TextField med5MorningTimeField;
    @FXML private TextField med5AfternoonTimeField;
    @FXML private TextField med5EveningTimeField;
    @FXML private DatePicker med5EndDatePicker;

    // Overall (Placeholder fields from FXML, logic not implemented yet)
    @FXML private TextField durationField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker overallEndDatePicker;

    // Buttons
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private Prescription prescriptionToEdit;
    private String originalPatientNameForUpdate;
    private Runnable onSaveListener;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private final Timer timer = new Timer(true); // Background daemon timer for alarms

    private static final String DB_URL = "jdbc:mysql://localhost:3306/health_clock";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public void setPrescription(Prescription prescription) {
        if (prescription == null) {
            System.err.println("EditPrescriptionController: setPrescription called with null prescription.");
            showAlert(Alert.AlertType.ERROR, "Error", "No prescription data provided to edit form.");
            closeWindow();
            return;
        }
        this.prescriptionToEdit = prescription;
        String patientName = prescription.getPatientName();
        this.originalPatientNameForUpdate = (patientName == null) ? "" : patientName;
        initializeFields();
    }

    public void setOnSave(Runnable listener) {
        this.onSaveListener = listener;
    }

    private void initializeFields() {
        if (prescriptionToEdit == null) {
            System.out.println("DEBUG: prescriptionToEdit is null in initializeFields.");
            return;
        }
        String patientNameToDisplay = prescriptionToEdit.getPatientName() == null ? "" : prescriptionToEdit.getPatientName();
        patientNameField.setText(patientNameToDisplay);

        // Medicine 1
        medicine1NameField.setText(prescriptionToEdit.getMedicine1Name() == null ? "" : prescriptionToEdit.getMedicine1Name());
        med1MorningTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getMorningTime1()));
        med1AfternoonTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getAfternoonTime1()));
        med1EveningTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getEveningTime1()));
        setDatePickerValue(med1EndDatePicker, prescriptionToEdit.getEndDate1());

        // Medicine 2
        medicine2NameField.setText(prescriptionToEdit.getMedicine2Name() == null ? "" : prescriptionToEdit.getMedicine2Name());
        med2MorningTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getMorningTime2()));
        med2AfternoonTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getAfternoonTime2()));
        med2EveningTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getEveningTime2()));
        setDatePickerValue(med2EndDatePicker, prescriptionToEdit.getEndDate2());

        // Medicine 3
        medicine3NameField.setText(prescriptionToEdit.getMedicine3Name() == null ? "" : prescriptionToEdit.getMedicine3Name());
        med3MorningTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getMorningTime3()));
        med3AfternoonTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getAfternoonTime3()));
        med3EveningTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getEveningTime3()));
        setDatePickerValue(med3EndDatePicker, prescriptionToEdit.getEndDate3());

        // Medicine 4
        medicine4NameField.setText(prescriptionToEdit.getMedicine4Name() == null ? "" : prescriptionToEdit.getMedicine4Name());
        med4MorningTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getMorningTime4()));
        med4AfternoonTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getAfternoonTime4()));
        med4EveningTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getEveningTime4()));
        setDatePickerValue(med4EndDatePicker, prescriptionToEdit.getEndDate4());

        // Medicine 5
        medicine5NameField.setText(prescriptionToEdit.getMedicine5Name() == null ? "" : prescriptionToEdit.getMedicine5Name());
        med5MorningTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getMorningTime5()));
        med5AfternoonTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getAfternoonTime5()));
        med5EveningTimeField.setText(getTimeStringForDisplay(prescriptionToEdit.getEveningTime5()));
        setDatePickerValue(med5EndDatePicker, prescriptionToEdit.getEndDate5());
    }

    private String getTimeStringForDisplay(String time) {
        if (time == null || time.trim().isEmpty() || "Scheduled".equalsIgnoreCase(time.trim())) {
            return "";
        }
        return time.trim();
    }

    private void setDatePickerValue(DatePicker datePicker, String dateString) {
        if (dateString != null && !dateString.trim().isEmpty()) {
            try {
                datePicker.setValue(LocalDate.parse(dateString, dateFormatter));
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing date string for DatePicker: " + dateString + " - " + e.getMessage());
                datePicker.setValue(null);
            }
        } else {
            datePicker.setValue(null);
        }
    }

    private String getDatePickerValueAsString(DatePicker datePicker) {
        if (datePicker.getValue() != null) {
            return datePicker.getValue().format(dateFormatter);
        }
        return null;
    }

    private String getTimeFromField(TextField timeField) {
        String textContent = timeField.getText();
        if (textContent == null || textContent.trim().isEmpty()) {
            return null;
        }
        return textContent.trim();
    }

    @FXML
    public void handleSave(ActionEvent event) {
        System.out.println("DEBUG: handleSave called.");
        if (prescriptionToEdit == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No prescription data to update. Please try reopening the edit form.");
            return;
        }
        if (originalPatientNameForUpdate == null || originalPatientNameForUpdate.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Original patient name is missing or empty. Cannot update.");
            return;
        }

        String patientNameText = patientNameField.getText();
        String newPatientName = (patientNameText == null) ? "" : patientNameText.trim();

        if (newPatientName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Patient name cannot be empty.");
            return;
        }
        prescriptionToEdit.setPatientName(newPatientName);

        Function<TextField, String> getTrimmedName = (tf) -> {
            String text = tf.getText();
            return (text == null) ? "" : text.trim();
        };

        prescriptionToEdit.setMedicine1Name(getTrimmedName.apply(medicine1NameField));
        prescriptionToEdit.setMorningTime1(getTimeFromField(med1MorningTimeField));
        prescriptionToEdit.setAfternoonTime1(getTimeFromField(med1AfternoonTimeField));
        prescriptionToEdit.setEveningTime1(getTimeFromField(med1EveningTimeField));
        prescriptionToEdit.setEndDate1(getDatePickerValueAsString(med1EndDatePicker));

        prescriptionToEdit.setMedicine2Name(getTrimmedName.apply(medicine2NameField));
        prescriptionToEdit.setMorningTime2(getTimeFromField(med2MorningTimeField));
        prescriptionToEdit.setAfternoonTime2(getTimeFromField(med2AfternoonTimeField));
        prescriptionToEdit.setEveningTime2(getTimeFromField(med2EveningTimeField));
        prescriptionToEdit.setEndDate2(getDatePickerValueAsString(med2EndDatePicker));

        prescriptionToEdit.setMedicine3Name(getTrimmedName.apply(medicine3NameField));
        prescriptionToEdit.setMorningTime3(getTimeFromField(med3MorningTimeField));
        prescriptionToEdit.setAfternoonTime3(getTimeFromField(med3AfternoonTimeField));
        prescriptionToEdit.setEveningTime3(getTimeFromField(med3EveningTimeField));
        prescriptionToEdit.setEndDate3(getDatePickerValueAsString(med3EndDatePicker));

        prescriptionToEdit.setMedicine4Name(getTrimmedName.apply(medicine4NameField));
        prescriptionToEdit.setMorningTime4(getTimeFromField(med4MorningTimeField));
        prescriptionToEdit.setAfternoonTime4(getTimeFromField(med4AfternoonTimeField));
        prescriptionToEdit.setEveningTime4(getTimeFromField(med4EveningTimeField));
        prescriptionToEdit.setEndDate4(getDatePickerValueAsString(med4EndDatePicker));

        prescriptionToEdit.setMedicine5Name(getTrimmedName.apply(medicine5NameField));
        prescriptionToEdit.setMorningTime5(getTimeFromField(med5MorningTimeField));
        prescriptionToEdit.setAfternoonTime5(getTimeFromField(med5AfternoonTimeField));
        prescriptionToEdit.setEveningTime5(getTimeFromField(med5EveningTimeField));
        prescriptionToEdit.setEndDate5(getDatePickerValueAsString(med5EndDatePicker));

        String sql = "UPDATE patient_prescriptions SET " +
                "patient_name = ?, " +
                "med1_name = ?, med1_morning_time = ?, med1_afternoon_time = ?, med1_evening_time = ?, med1_end_date = ?, " +
                "med2_name = ?, med2_morning_time = ?, med2_afternoon_time = ?, med2_evening_time = ?, med2_end_date = ?, " +
                "med3_name = ?, med3_morning_time = ?, med3_afternoon_time = ?, med3_evening_time = ?, med3_end_date = ?, " +
                "med4_name = ?, med4_morning_time = ?, med4_afternoon_time = ?, med4_evening_time = ?, med4_end_date = ?, " +
                "med5_name = ?, med5_morning_time = ?, med5_afternoon_time = ?, med5_evening_time = ?, med5_end_date = ? " +
                "WHERE patient_name = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            BiConsumer<Integer, String> setStringOrNull = (idx, val) -> {
                try {
                    if (val != null && !val.isEmpty()) {
                        stmt.setString(idx, val);
                    } else { // Handles null or empty string
                        stmt.setNull(idx, Types.VARCHAR);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("SQLException in setStringOrNull lambda", e);
                }
            };

            BiConsumer<Integer, String> setDateOrNull = (idx, dateStr) -> {
                try {
                    if (dateStr != null && !dateStr.isEmpty()) {
                        stmt.setDate(idx, java.sql.Date.valueOf(LocalDate.parse(dateStr, dateFormatter)));
                    } else {
                        stmt.setNull(idx, Types.DATE);
                    }
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid date format for DB: " + dateStr + ". Setting field to NULL.");
                    try {
                        stmt.setNull(idx, Types.DATE);
                    } catch (SQLException se) { throw new RuntimeException("SQLException in setDateOrNull lambda (parse error handling)", se); }
                } catch (SQLException e) {
                    throw new RuntimeException("SQLException in setDateOrNull lambda", e);
                }
            };

            stmt.setString(1, prescriptionToEdit.getPatientName());

            setStringOrNull.accept(2, prescriptionToEdit.getMedicine1Name());
            setStringOrNull.accept(3, prescriptionToEdit.getMorningTime1());
            setStringOrNull.accept(4, prescriptionToEdit.getAfternoonTime1());
            setStringOrNull.accept(5, prescriptionToEdit.getEveningTime1());
            setDateOrNull.accept(6, prescriptionToEdit.getEndDate1());

            setStringOrNull.accept(7, prescriptionToEdit.getMedicine2Name());
            setStringOrNull.accept(8, prescriptionToEdit.getMorningTime2());
            setStringOrNull.accept(9, prescriptionToEdit.getAfternoonTime2());
            setStringOrNull.accept(10, prescriptionToEdit.getEveningTime2());
            setDateOrNull.accept(11, prescriptionToEdit.getEndDate2());

            setStringOrNull.accept(12, prescriptionToEdit.getMedicine3Name());
            setStringOrNull.accept(13, prescriptionToEdit.getMorningTime3());
            setStringOrNull.accept(14, prescriptionToEdit.getAfternoonTime3());
            setStringOrNull.accept(15, prescriptionToEdit.getEveningTime3());
            setDateOrNull.accept(16, prescriptionToEdit.getEndDate3());

            setStringOrNull.accept(17, prescriptionToEdit.getMedicine4Name());
            setStringOrNull.accept(18, prescriptionToEdit.getMorningTime4());
            setStringOrNull.accept(19, prescriptionToEdit.getAfternoonTime4());
            setStringOrNull.accept(20, prescriptionToEdit.getEveningTime4());
            setDateOrNull.accept(21, prescriptionToEdit.getEndDate4());

            setStringOrNull.accept(22, prescriptionToEdit.getMedicine5Name());
            setStringOrNull.accept(23, prescriptionToEdit.getMorningTime5());
            setStringOrNull.accept(24, prescriptionToEdit.getAfternoonTime5());
            setStringOrNull.accept(25, prescriptionToEdit.getEveningTime5());
            setDateOrNull.accept(26, prescriptionToEdit.getEndDate5());

            stmt.setString(27, originalPatientNameForUpdate);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Prescription updated successfully for '" + prescriptionToEdit.getPatientName() + "'. Alarms will be rescheduled.");
                this.originalPatientNameForUpdate = prescriptionToEdit.getPatientName();

                scheduleAlarmsForEditedPrescription(prescriptionToEdit); // Schedule alarms

                if (onSaveListener != null) {
                    onSaveListener.run();
                }
                closeWindow();
            } else {
                showAlert(Alert.AlertType.WARNING, "Update Failed", "No matching prescription found in the database for '" + originalPatientNameForUpdate + "'.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error saving prescription: " + e.getMessage());
        } catch (RuntimeException e) {
            // Catch runtime exceptions from lambdas
            if (e.getCause() instanceof SQLException) {
                e.getCause().printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error during database operation: " + e.getCause().getMessage());
            } else {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Application Error", "An unexpected error occurred during save: " + e.getMessage());
            }
        } catch (Exception e) { // Generic catch for other unexpected errors
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Application Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void scheduleAlarmsForEditedPrescription(Prescription p) {
        String patient = p.getPatientName();

        // Medicine 1
        if (p.getMedicine1Name() != null && !p.getMedicine1Name().isEmpty() && p.getEndDate1() != null) {
            scheduleAlarm(patient, p.getMedicine1Name(), p.getMorningTime1(), p.getEndDate1());
            scheduleAlarm(patient, p.getMedicine1Name(), p.getAfternoonTime1(), p.getEndDate1());
            scheduleAlarm(patient, p.getMedicine1Name(), p.getEveningTime1(), p.getEndDate1());
        }
        // Medicine 2
        if (p.getMedicine2Name() != null && !p.getMedicine2Name().isEmpty() && p.getEndDate2() != null) {
            scheduleAlarm(patient, p.getMedicine2Name(), p.getMorningTime2(), p.getEndDate2());
            scheduleAlarm(patient, p.getMedicine2Name(), p.getAfternoonTime2(), p.getEndDate2());
            scheduleAlarm(patient, p.getMedicine2Name(), p.getEveningTime2(), p.getEndDate2());
        }
        // Medicine 3
        if (p.getMedicine3Name() != null && !p.getMedicine3Name().isEmpty() && p.getEndDate3() != null) {
            scheduleAlarm(patient, p.getMedicine3Name(), p.getMorningTime3(), p.getEndDate3());
            scheduleAlarm(patient, p.getMedicine3Name(), p.getAfternoonTime3(), p.getEndDate3());
            scheduleAlarm(patient, p.getMedicine3Name(), p.getEveningTime3(), p.getEndDate3());
        }
        // Medicine 4
        if (p.getMedicine4Name() != null && !p.getMedicine4Name().isEmpty() && p.getEndDate4() != null) {
            scheduleAlarm(patient, p.getMedicine4Name(), p.getMorningTime4(), p.getEndDate4());
            scheduleAlarm(patient, p.getMedicine4Name(), p.getAfternoonTime4(), p.getEndDate4());
            scheduleAlarm(patient, p.getMedicine4Name(), p.getEveningTime4(), p.getEndDate4());
        }
        // Medicine 5
        if (p.getMedicine5Name() != null && !p.getMedicine5Name().isEmpty() && p.getEndDate5() != null) {
            scheduleAlarm(patient, p.getMedicine5Name(), p.getMorningTime5(), p.getEndDate5());
            scheduleAlarm(patient, p.getMedicine5Name(), p.getAfternoonTime5(), p.getEndDate5());
            scheduleAlarm(patient, p.getMedicine5Name(), p.getEveningTime5(), p.getEndDate5());
        }
    }

    private void scheduleAlarm(String patient, String medicine, String timeStr, String endDateStr) {
        if (timeStr == null || timeStr.trim().isEmpty() ||
                medicine == null || medicine.trim().isEmpty() ||
                endDateStr == null || endDateStr.trim().isEmpty()) {
            // System.out.println("DEBUG: Skipping alarm for " + medicine + " due to missing time/medicine/endDate.");
            return;
        }

        try {
            String[] parts = timeStr.split(":");
            if (parts.length != 2) {
                System.err.println("Invalid time format (not HH:mm): " + timeStr + " for medicine " + medicine);
                return;
            }
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                System.err.println("Invalid time values (HH must be 0-23, mm 0-59): " + timeStr + " for medicine " + medicine);
                return;
            }

            LocalDate end = LocalDate.parse(endDateStr, dateFormatter);
            LocalDate today = LocalDate.now();

            for (LocalDate date = today; !date.isAfter(end); date = date.plusDays(1)) {
                LocalDateTime alarmDateTime = date.atTime(hour, minute);
                LocalDateTime now = LocalDateTime.now();

                if (alarmDateTime.isAfter(now)) { // Only schedule if the alarm time is in the future
                    long delay = Duration.between(now, alarmDateTime).toMillis();
                    // System.out.println("DEBUG: Scheduling alarm for " + medicine + " at " + alarmDateTime + " with delay " + delay + "ms");

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                playSound();
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Medicine Alarm");
                                alert.setHeaderText("Time to take medicine!");
                                alert.setContentText("Patient: " + patient + "\nMedicine: " + medicine + "\nTime: " + timeStr);
                                // Not setting owner, so it behaves like a system notification for the app
                                alert.showAndWait();
                            });
                        }
                    }, delay);
                } else {
                    // System.out.println("DEBUG: Skipping past alarm for " + medicine + " at " + alarmDateTime);
                }
            }
        } catch (DateTimeParseException e) {
            System.err.println("Invalid end date format: " + endDateStr + " for medicine " + medicine + ". Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid time format (HH:mm parts not numeric): " + timeStr + " for medicine " + medicine + ". Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error scheduling alarm for " + medicine + " at " + timeStr + " until " + endDateStr + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void playSound() {
        try {
            // Ensure "alarm.mp3" is in the same package as this controller class,
            // or provide an absolute path if it's elsewhere.
            String soundFile = "alarm.mp3";
            java.net.URL resourceUrl = getClass().getResource(soundFile);
            if (resourceUrl == null) {
                System.err.println("Failed to find sound file: " + soundFile);
                return;
            }

            AudioClip clip = new AudioClip(resourceUrl.toString());
            clip.setCycleCount(AudioClip.INDEFINITE); // Loop the sound indefinitely
            clip.play();

            // Stop the clip after 10 seconds
            new Timer(true).schedule(new TimerTask() { // Use a new daemon timer for this short-lived task
                @Override
                public void run() {
                    Platform.runLater(clip::stop);
                }
            }, 10_000); // 10 seconds in milliseconds

        } catch (Exception e) {
            System.err.println("Failed to play sound: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void handleCancel(ActionEvent event) {
        System.out.println("DEBUG: handleCancel called.");
        closeWindow();
    }

    private void closeWindow() {
        Control controlForScene = patientNameField != null ? patientNameField : (saveButton != null ? saveButton : cancelButton);
        if (controlForScene == null || controlForScene.getScene() == null) {
            System.err.println("Cannot close window: control or scene is null.");
            return;
        }
        Stage stage = (Stage) controlForScene.getScene().getWindow();
        if (stage != null) {
            stage.close();
        } else {
            System.err.println("Error: Could not get stage to close window.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        Control controlForOwner = patientNameField != null ? patientNameField : (saveButton != null ? saveButton : cancelButton);
        if (controlForOwner != null && controlForOwner.getScene() != null && controlForOwner.getScene().getWindow() != null) {
            alert.initOwner(controlForOwner.getScene().getWindow());
        }
        alert.showAndWait();
    }
}
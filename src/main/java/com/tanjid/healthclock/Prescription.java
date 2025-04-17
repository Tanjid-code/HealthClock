package com.tanjid.healthclock;

import javafx.beans.property.*;

public class Prescription {

    private final StringProperty patientName;
    private final StringProperty medicineName;
    private final StringProperty morningTime;
    private final StringProperty afternoonTime;
    private final StringProperty eveningTime;
    private final IntegerProperty duration;
    private final StringProperty imagePath;
    private final StringProperty endDate;

    public Prescription(String patientName, String medicineName,
                        String morningTime, String afternoonTime, String eveningTime,
                        int duration, String imagePath, String endDate) {
        this.patientName = new SimpleStringProperty(patientName);
        this.medicineName = new SimpleStringProperty(medicineName);
        this.morningTime = new SimpleStringProperty(morningTime);
        this.afternoonTime = new SimpleStringProperty(afternoonTime);
        this.eveningTime = new SimpleStringProperty(eveningTime);
        this.duration = new SimpleIntegerProperty(duration);
        this.imagePath = new SimpleStringProperty(imagePath);
        this.endDate = new SimpleStringProperty(endDate);
    }

    // Patient Name
    public String getPatientName() {
        return patientName.get();
    }
    public void setPatientName(String patientName) {
        this.patientName.set(patientName);
    }
    public StringProperty patientNameProperty() {
        return patientName;
    }

    // Medicine Name
    public String getMedicineName() {
        return medicineName.get();
    }
    public void setMedicineName(String medicineName) {
        this.medicineName.set(medicineName);
    }
    public StringProperty medicineNameProperty() {
        return medicineName;
    }

    // Morning Time
    public String getMorningTime() {
        return morningTime.get();
    }
    public void setMorningTime(String morningTime) {
        this.morningTime.set(morningTime);
    }
    public StringProperty morningTimeProperty() {
        return morningTime;
    }

    // Afternoon Time
    public String getAfternoonTime() {
        return afternoonTime.get();
    }
    public void setAfternoonTime(String afternoonTime) {
        this.afternoonTime.set(afternoonTime);
    }
    public StringProperty afternoonTimeProperty() {
        return afternoonTime;
    }

    // Evening Time
    public String getEveningTime() {
        return eveningTime.get();
    }
    public void setEveningTime(String eveningTime) {
        this.eveningTime.set(eveningTime);
    }
    public StringProperty eveningTimeProperty() {
        return eveningTime;
    }

    // Duration
    public int getDuration() {
        return duration.get();
    }
    public void setDuration(int duration) {
        this.duration.set(duration);
    }
    public IntegerProperty durationProperty() {
        return duration;
    }

    // Image Path
    public String getImagePath() {
        return imagePath.get();
    }
    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }
    public StringProperty imagePathProperty() {
        return imagePath;
    }

    // End Date
    public String getEndDate() {
        return endDate.get();
    }
    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }
    public StringProperty endDateProperty() {
        return endDate;
    }
}

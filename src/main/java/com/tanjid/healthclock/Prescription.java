package com.tanjid.healthclock;

import javafx.beans.property.*;

public class Prescription {

    private final StringProperty patientName;
    private final StringProperty medicineName;
    private final BooleanProperty morning;
    private final BooleanProperty afternoon;
    private final BooleanProperty evening;
    private final IntegerProperty duration;
    private final StringProperty imagePath;
    private final StringProperty endDate; // ✅ New endDate field

    public Prescription(String patientName, String medicineName, boolean morning, boolean afternoon, boolean evening, int duration, String imagePath, String endDate) {
        this.patientName = new SimpleStringProperty(patientName);
        this.medicineName = new SimpleStringProperty(medicineName);
        this.morning = new SimpleBooleanProperty(morning);
        this.afternoon = new SimpleBooleanProperty(afternoon);
        this.evening = new SimpleBooleanProperty(evening);
        this.duration = new SimpleIntegerProperty(duration);
        this.imagePath = new SimpleStringProperty(imagePath);
        this.endDate = new SimpleStringProperty(endDate); // ✅
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

    // Morning
    public boolean isMorning() {
        return morning.get();
    }
    public void setMorning(boolean morning) {
        this.morning.set(morning);
    }
    public BooleanProperty morningProperty() {
        return morning;
    }

    // Afternoon
    public boolean isAfternoon() {
        return afternoon.get();
    }
    public void setAfternoon(boolean afternoon) {
        this.afternoon.set(afternoon);
    }
    public BooleanProperty afternoonProperty() {
        return afternoon;
    }

    // Evening
    public boolean isEvening() {
        return evening.get();
    }
    public void setEvening(boolean evening) {
        this.evening.set(evening);
    }
    public BooleanProperty eveningProperty() {
        return evening;
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

    // ✅ End Date
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

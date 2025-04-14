package com.tanjid.healthclock;

import javafx.beans.property.*;

public class Prescription {

    // JavaFX properties for data binding
    private final StringProperty patientName;
    private final StringProperty medicineName;
    private final BooleanProperty morning;
    private final BooleanProperty afternoon;
    private final BooleanProperty evening;
    private final IntegerProperty duration; // ✅ New duration field
    private final StringProperty imagePath;

    // ✅ Updated Constructor with duration
    public Prescription(String patientName, String medicineName, boolean morning, boolean afternoon, boolean evening, int duration, String imagePath) {
        this.patientName = new SimpleStringProperty(patientName);
        this.medicineName = new SimpleStringProperty(medicineName);
        this.morning = new SimpleBooleanProperty(morning);
        this.afternoon = new SimpleBooleanProperty(afternoon);
        this.evening = new SimpleBooleanProperty(evening);
        this.duration = new SimpleIntegerProperty(duration); // ✅
        this.imagePath = new SimpleStringProperty(imagePath);
    }

    // Getters and property accessors

    public String getPatientName() {
        return patientName.get();
    }
    public StringProperty patientNameProperty() {
        return patientName;
    }

    public String getMedicineName() {
        return medicineName.get();
    }
    public void setMedicineName(String medicineName) {
        this.medicineName.set(medicineName);
    }
    public StringProperty medicineNameProperty() {
        return medicineName;
    }

    public boolean isMorning() {
        return morning.get();
    }
    public BooleanProperty morningProperty() {
        return morning;
    }

    public boolean isAfternoon() {
        return afternoon.get();
    }
    public BooleanProperty afternoonProperty() {
        return afternoon;
    }

    public boolean isEvening() {
        return evening.get();
    }
    public BooleanProperty eveningProperty() {
        return evening;
    }

    // ✅ Duration getter and property
    public int getDuration() {
        return duration.get();
    }
    public void setDuration(int duration) {
        this.duration.set(duration);
    }
    public IntegerProperty durationProperty() {
        return duration;
    }

    public String getImagePath() {
        return imagePath.get();
    }
    public StringProperty imagePathProperty() {
        return imagePath;
    }
}

package com.tanjid.healthclock;

import javafx.beans.property.*;

public class Prescription {

    // Use JavaFX properties for data binding
    private final StringProperty patientName;
    private final StringProperty medicineName;
    private final BooleanProperty morning;
    private final BooleanProperty afternoon;
    private final BooleanProperty evening;
    private final StringProperty imagePath;

    // Constructor
    public Prescription(String patientName, String medicineName, boolean morning, boolean afternoon, boolean evening, String imagePath) {
        this.patientName = new SimpleStringProperty(patientName);
        this.medicineName = new SimpleStringProperty(medicineName);
        this.morning = new SimpleBooleanProperty(morning);
        this.afternoon = new SimpleBooleanProperty(afternoon);
        this.evening = new SimpleBooleanProperty(evening);
        this.imagePath = new SimpleStringProperty(imagePath);
    }

    // Getters for properties (for use in JavaFX binding)
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

    public String getImagePath() {
        return imagePath.get();
    }
    public StringProperty imagePathProperty() {
        return imagePath;
    }
}

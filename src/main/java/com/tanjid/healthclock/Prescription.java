package com.tanjid.healthclock;

public class Prescription {
    private String patientName;
    private String medicineName;
    private boolean morning;
    private boolean afternoon;
    private boolean evening;
    private String imagePath;

    public Prescription(String patientName, String medicineName, boolean morning, boolean afternoon, boolean evening, String imagePath) {
        this.patientName = patientName;
        this.medicineName = medicineName;
        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.imagePath = imagePath;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public boolean getMorning() {  // ✅ Use getMorning() instead of isMorning()
        return morning;
    }

    public boolean getAfternoon() {
        return afternoon;
    }

    public boolean getEvening() {
        return evening;
    }

    public String getImagePath() {
        return imagePath;
    }
}

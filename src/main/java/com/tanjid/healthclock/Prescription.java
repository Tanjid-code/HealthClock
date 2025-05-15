package com.tanjid.healthclock;

import javafx.beans.property.*;

public class Prescription {

    private final StringProperty patientName;

    private final StringProperty medicine1Name;
    private final StringProperty morningTime1;
    private final StringProperty afternoonTime1;
    private final StringProperty eveningTime1;
    private final StringProperty endDate1;

    private final StringProperty medicine2Name;
    private final StringProperty morningTime2;
    private final StringProperty afternoonTime2;
    private final StringProperty eveningTime2;
    private final StringProperty endDate2;

    private final StringProperty medicine3Name;
    private final StringProperty morningTime3;
    private final StringProperty afternoonTime3;
    private final StringProperty eveningTime3;
    private final StringProperty endDate3;

    private final StringProperty medicine4Name;
    private final StringProperty morningTime4;
    private final StringProperty afternoonTime4;
    private final StringProperty eveningTime4;
    private final StringProperty endDate4;

    private final StringProperty medicine5Name;
    private final StringProperty morningTime5;
    private final StringProperty afternoonTime5;
    private final StringProperty eveningTime5;
    private final StringProperty endDate5;

    public Prescription(String patientName,
                        String medicine1Name, String morningTime1, String afternoonTime1, String eveningTime1, String endDate1,
                        String medicine2Name, String morningTime2, String afternoonTime2, String eveningTime2, String endDate2,
                        String medicine3Name, String morningTime3, String afternoonTime3, String eveningTime3, String endDate3,
                        String medicine4Name, String morningTime4, String afternoonTime4, String eveningTime4, String endDate4,
                        String medicine5Name, String morningTime5, String afternoonTime5, String eveningTime5, String endDate5) {

        this.patientName = new SimpleStringProperty(patientName);

        this.medicine1Name = new SimpleStringProperty(medicine1Name);
        this.morningTime1 = new SimpleStringProperty(morningTime1);
        this.afternoonTime1 = new SimpleStringProperty(afternoonTime1);
        this.eveningTime1 = new SimpleStringProperty(eveningTime1);
        this.endDate1 = new SimpleStringProperty(endDate1);

        this.medicine2Name = new SimpleStringProperty(medicine2Name);
        this.morningTime2 = new SimpleStringProperty(morningTime2);
        this.afternoonTime2 = new SimpleStringProperty(afternoonTime2);
        this.eveningTime2 = new SimpleStringProperty(eveningTime2);
        this.endDate2 = new SimpleStringProperty(endDate2);

        this.medicine3Name = new SimpleStringProperty(medicine3Name);
        this.morningTime3 = new SimpleStringProperty(morningTime3);
        this.afternoonTime3 = new SimpleStringProperty(afternoonTime3);
        this.eveningTime3 = new SimpleStringProperty(eveningTime3);
        this.endDate3 = new SimpleStringProperty(endDate3);

        this.medicine4Name = new SimpleStringProperty(medicine4Name);
        this.morningTime4 = new SimpleStringProperty(morningTime4);
        this.afternoonTime4 = new SimpleStringProperty(afternoonTime4);
        this.eveningTime4 = new SimpleStringProperty(eveningTime4);
        this.endDate4 = new SimpleStringProperty(endDate4);

        this.medicine5Name = new SimpleStringProperty(medicine5Name);
        this.morningTime5 = new SimpleStringProperty(morningTime5);
        this.afternoonTime5 = new SimpleStringProperty(afternoonTime5);
        this.eveningTime5 = new SimpleStringProperty(eveningTime5);
        this.endDate5 = new SimpleStringProperty(endDate5);
    }

    public String getPatientName() {
        return patientName.get();
    }

    public void setPatientName(String value) {
        patientName.set(value);
    }

    public StringProperty patientNameProperty() {
        return patientName;
    }

    // --- Medicine 1 ---
    public String getMedicine1Name() { return medicine1Name.get(); }
    public void setMedicine1Name(String value) { medicine1Name.set(value); }
    public StringProperty medicine1NameProperty() { return medicine1Name; }

    public String getMorningTime1() { return morningTime1.get(); }
    public void setMorningTime1(String value) { morningTime1.set(value); }
    public StringProperty morningTime1Property() { return morningTime1; }

    public String getAfternoonTime1() { return afternoonTime1.get(); }
    public void setAfternoonTime1(String value) { afternoonTime1.set(value); }
    public StringProperty afternoonTime1Property() { return afternoonTime1; }

    public String getEveningTime1() { return eveningTime1.get(); }
    public void setEveningTime1(String value) { eveningTime1.set(value); }
    public StringProperty eveningTime1Property() { return eveningTime1; }

    public String getEndDate1() { return endDate1.get(); }
    public void setEndDate1(String value) { endDate1.set(value); }
    public StringProperty endDate1Property() { return endDate1; }

    // --- Medicine 2 ---
    public String getMedicine2Name() { return medicine2Name.get(); }
    public void setMedicine2Name(String value) { medicine2Name.set(value); }
    public StringProperty medicine2NameProperty() { return medicine2Name; }

    public String getMorningTime2() { return morningTime2.get(); }
    public void setMorningTime2(String value) { morningTime2.set(value); }
    public StringProperty morningTime2Property() { return morningTime2; }

    public String getAfternoonTime2() { return afternoonTime2.get(); }
    public void setAfternoonTime2(String value) { afternoonTime2.set(value); }
    public StringProperty afternoonTime2Property() { return afternoonTime2; }

    public String getEveningTime2() { return eveningTime2.get(); }
    public void setEveningTime2(String value) { eveningTime2.set(value); }
    public StringProperty eveningTime2Property() { return eveningTime2; }

    public String getEndDate2() { return endDate2.get(); }
    public void setEndDate2(String value) { endDate2.set(value); }
    public StringProperty endDate2Property() { return endDate2; }

    // --- Medicine 3 ---
    public String getMedicine3Name() { return medicine3Name.get(); }
    public void setMedicine3Name(String value) { medicine3Name.set(value); }
    public StringProperty medicine3NameProperty() { return medicine3Name; }

    public String getMorningTime3() { return morningTime3.get(); }
    public void setMorningTime3(String value) { morningTime3.set(value); }
    public StringProperty morningTime3Property() { return morningTime3; }

    public String getAfternoonTime3() { return afternoonTime3.get(); }
    public void setAfternoonTime3(String value) { afternoonTime3.set(value); }
    public StringProperty afternoonTime3Property() { return afternoonTime3; }

    public String getEveningTime3() { return eveningTime3.get(); }
    public void setEveningTime3(String value) { eveningTime3.set(value); }
    public StringProperty eveningTime3Property() { return eveningTime3; }

    public String getEndDate3() { return endDate3.get(); }
    public void setEndDate3(String value) { endDate3.set(value); }
    public StringProperty endDate3Property() { return endDate3; }

    // --- Medicine 4 ---
    public String getMedicine4Name() { return medicine4Name.get(); }
    public void setMedicine4Name(String value) { medicine4Name.set(value); }
    public StringProperty medicine4NameProperty() { return medicine4Name; }

    public String getMorningTime4() { return morningTime4.get(); }
    public void setMorningTime4(String value) { morningTime4.set(value); }
    public StringProperty morningTime4Property() { return morningTime4; }

    public String getAfternoonTime4() { return afternoonTime4.get(); }
    public void setAfternoonTime4(String value) { afternoonTime4.set(value); }
    public StringProperty afternoonTime4Property() { return afternoonTime4; }

    public String getEveningTime4() { return eveningTime4.get(); }
    public void setEveningTime4(String value) { eveningTime4.set(value); }
    public StringProperty eveningTime4Property() { return eveningTime4; }

    public String getEndDate4() { return endDate4.get(); }
    public void setEndDate4(String value) { endDate4.set(value); }
    public StringProperty endDate4Property() { return endDate4; }

    // --- Medicine 5 ---
    public String getMedicine5Name() { return medicine5Name.get(); }
    public void setMedicine5Name(String value) { medicine5Name.set(value); }
    public StringProperty medicine5NameProperty() { return medicine5Name; }

    public String getMorningTime5() { return morningTime5.get(); }
    public void setMorningTime5(String value) { morningTime5.set(value); }
    public StringProperty morningTime5Property() { return morningTime5; }

    public String getAfternoonTime5() { return afternoonTime5.get(); }
    public void setAfternoonTime5(String value) { afternoonTime5.set(value); }
    public StringProperty afternoonTime5Property() { return afternoonTime5; }

    public String getEveningTime5() { return eveningTime5.get(); }
    public void setEveningTime5(String value) { eveningTime5.set(value); }
    public StringProperty eveningTime5Property() { return eveningTime5; }

    public String getEndDate5() { return endDate5.get(); }
    public void setEndDate5(String value) { endDate5.set(value); }
    public StringProperty endDate5Property() { return endDate5; }
}

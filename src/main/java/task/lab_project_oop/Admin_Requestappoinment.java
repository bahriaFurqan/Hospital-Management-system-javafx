package task.lab_project_oop;

import javafx.beans.property.SimpleStringProperty;

public class Admin_Requestappoinment {

    private SimpleStringProperty doctor;
    private SimpleStringProperty patient;
    private SimpleStringProperty date;
    private SimpleStringProperty time;

    public Admin_Requestappoinment( String doctor, String patient, String date, String time) {

        this.doctor = new SimpleStringProperty(doctor);
        this.patient = new SimpleStringProperty(patient);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
    }



    public String getDoctor() {
        return doctor.get();
    }

    public String getPatient() {
        return patient.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTime() {
        return time.get();
    }
}

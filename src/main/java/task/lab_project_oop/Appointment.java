package task.lab_project_oop;

public class Appointment {
    private String appointmentNo;
    private String date; // Ensure this is the right field
    private String doctor;
    private String patient;
    private String time;

    // Constructor
    public Appointment(String appointmentNo, String date, String doctor, String patient, String time) {
        this.appointmentNo = appointmentNo;
        this.date = date;
        this.doctor = doctor;
        this.patient = patient;
        this.time = time;
    }

    // Getters
    public String getAppointmentNo() {
        return appointmentNo;
    }

    public String getDate() {
        return date;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getPatient() {
        return patient;
    }

    public String getTime() {
        return time;
    }

    // Setters
    public void setAppointmentNo(String appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

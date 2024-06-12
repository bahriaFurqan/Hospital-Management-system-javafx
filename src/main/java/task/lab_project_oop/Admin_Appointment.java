package task.lab_project_oop;


public class Admin_Appointment {
    private int appointmentNo;
    private String doctor;
    private String patient;
    private String date;
    private String time;


    public Admin_Appointment(int appointmentNo, String doctor, String patient, String date, String time) {
        this.appointmentNo = appointmentNo;
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.time = time;
    }
    public Admin_Appointment(String doctor, String patient, String date, String time) {
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.time = time;
    }

    public int getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(int appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
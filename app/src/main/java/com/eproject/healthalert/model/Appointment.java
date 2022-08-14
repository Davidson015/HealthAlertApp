package com.eproject.healthalert.model;

public class Appointment {
    private String userEmail;
    private int appointmentId;
    private String appointmentDescription;
    private String appointmentDate;
    private String appointmentTime;
    private String appointmentLocation;

    public Appointment() {}

    public Appointment(String userEmail, int appointmentId, String appointmentDescription, String appointmentDate, String appointmentTime, String appointmentLocation) {
        this.userEmail = userEmail;
        this.appointmentId = appointmentId;
        this.appointmentDescription = appointmentDescription;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentLocation = appointmentLocation;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }


    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }
}

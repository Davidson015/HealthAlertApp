package com.eproject.healthalert.model;

public class Appointment {
    private String userId;
    private String appointmentId;
    private String appointmentDescription;
    private String appointmentDate;
    private String appointmentTime;
    private String appointmentLocation;
    private Boolean appointmentStatus;

    public Appointment(String userId, String appointmentId, String appointmentDescription, String appointmentDate, String appointmentTime, String appointmentLocation, Boolean appointmentStatus) {
        this.userId = userId;
        this.appointmentId = appointmentId;
        this.appointmentDescription = appointmentDescription;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentLocation = appointmentLocation;
        this.appointmentStatus = appointmentStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
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

    public Boolean getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(Boolean appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }
}

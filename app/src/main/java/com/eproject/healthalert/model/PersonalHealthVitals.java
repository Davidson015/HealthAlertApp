package com.eproject.healthalert.model;

public class PersonalHealthVitals {
    private String userId;
    private String height;
    private String weight;
    private String bloodPressure;
    private String bloodGlucose;
    private String bodyTemperature;
    private String respiratoryRate;
    private String pulseRate;

    public PersonalHealthVitals(String userId, String height, String weight, String bloodPressure, String bloodGlucose, String bodyTemperature, String respiratoryRate, String pulseRate) {
        this.userId = userId;
        this.height = height;
        this.weight = weight;
        this.bloodPressure = bloodPressure;
        this.bloodGlucose = bloodGlucose;
        this.bodyTemperature = bodyTemperature;
        this.respiratoryRate = respiratoryRate;
        this.pulseRate = pulseRate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getBloodGlucose() {
        return bloodGlucose;
    }

    public void setBloodGlucose(String bloodGlucose) {
        this.bloodGlucose = bloodGlucose;
    }

    public String getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(String bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public String getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(String respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public String getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(String pulseRate) {
        this.pulseRate = pulseRate;
    }
}

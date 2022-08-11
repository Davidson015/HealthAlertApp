package com.eproject.healthalert.model;

public class MedicineDosage {
    private String userId;
    private String medicineId;
    private String medicineName;
    private String medicineDosage;
    private String medicineDosageTime;
    private String medicineDosageStartDate;
    private String medicineDosageEndDate;
    private String medicineDosageFrequency;

    public  MedicineDosage() {}

    public MedicineDosage(String userId, String medicineId, String medicineName, String medicineDosage, String medicineDosageTime, String medicineDosageStartDate, String medicineDosageEndDate, String medicineDosageFrequency) {
        this.userId = userId;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.medicineDosage = medicineDosage;
        this.medicineDosageTime = medicineDosageTime;
        this.medicineDosageStartDate = medicineDosageStartDate;
        this.medicineDosageEndDate = medicineDosageEndDate;
        this.medicineDosageFrequency = medicineDosageFrequency;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineDosage() {
        return medicineDosage;
    }

    public void setMedicineDosage(String medicineDosage) {
        this.medicineDosage = medicineDosage;
    }

    public String getMedicineDosageTime() {
        return medicineDosageTime;
    }

    public void setMedicineDosageTime(String medicineDosageTime) {
        this.medicineDosageTime = medicineDosageTime;
    }

    public String getMedicineDosageStartDate() {
        return medicineDosageStartDate;
    }

    public void setMedicineDosageStartDate(String medicineDosageStartDate) {
        this.medicineDosageStartDate = medicineDosageStartDate;
    }

    public String getMedicineDosageEndDate() {
        return medicineDosageEndDate;
    }

    public void setMedicineDosageEndDate(String medicineDosageEndDate) {
        this.medicineDosageEndDate = medicineDosageEndDate;
    }

    public String getMedicineDosageFrequency() {
        return medicineDosageFrequency;
    }

    public void setMedicineDosageFrequency(String medicineDosageFrequency) {
        this.medicineDosageFrequency = medicineDosageFrequency;
    }
}

package com.myprivatehealth.app;

public class HealthData {
    private int id;
    private int userId;
    private String date;
    private int systolicPressure;
    private int diastolicPressure;
    private double weight;
    private double fatPercentage;
    private double bmi;

    public HealthData() {
    }

    public HealthData(int userId, String date, int systolicPressure, int diastolicPressure, 
                     double weight, double fatPercentage, double bmi) {
        this.userId = userId;
        this.date = date;
        this.systolicPressure = systolicPressure;
        this.diastolicPressure = diastolicPressure;
        this.weight = weight;
        this.fatPercentage = fatPercentage;
        this.bmi = bmi;
    }

    public HealthData(int id, int userId, String date, int systolicPressure, int diastolicPressure, 
                     double weight, double fatPercentage, double bmi) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.systolicPressure = systolicPressure;
        this.diastolicPressure = diastolicPressure;
        this.weight = weight;
        this.fatPercentage = fatPercentage;
        this.bmi = bmi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(int systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public int getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(int diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getFatPercentage() {
        return fatPercentage;
    }

    public void setFatPercentage(double fatPercentage) {
        this.fatPercentage = fatPercentage;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    // Calculate BMI based on weight and height
    public static double calculateBMI(double weight, double height) {
        if (height <= 0) return 0;
        // Convert height from cm to meters and calculate BMI
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }

    @Override
    public String toString() {
        return "Date: " + date + 
               ", BP: " + systolicPressure + "/" + diastolicPressure + 
               ", Weight: " + weight + "kg" +
               ", Fat: " + fatPercentage + "%" +
               ", BMI: " + bmi;
    }
} 
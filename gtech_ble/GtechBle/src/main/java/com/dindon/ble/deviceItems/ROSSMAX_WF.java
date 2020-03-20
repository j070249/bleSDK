package com.dindon.ble.deviceItems;

/**
 * Created by G-Tech on 2019/8/12.
 * E-mail: j070249@gmail.com
 */

public class ROSSMAX_WF extends BaseDevice {

    private String dataTime = "";
    private double weight = 0;
    private double visceralFat= 0;
    private double proteinContent= 0;
    private double muscleMass= 0;
    private double boneDensity= 0;
    private double bodyWater= 0;
    private double bodyFat= 0;
    private double BMI= 0;
    private double basalMetabolism= 0;

    public ROSSMAX_WF(String name, String mac, String dataTime, double weight, double visceralFat, double proteinContent, double muscleMass, double boneDensity, double bodyWater, double bodyFat, double BMI, double basalMetabolism) {
        super(name, mac);
        this.dataTime = dataTime;
        this.weight = weight;
        this.visceralFat = visceralFat;
        this.proteinContent = proteinContent;
        this.muscleMass = muscleMass;
        this.boneDensity = boneDensity;
        this.bodyWater = bodyWater;
        this.bodyFat = bodyFat;
        this.BMI = BMI;
        this.basalMetabolism = basalMetabolism;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getVisceralFat() {
        return visceralFat;
    }

    public void setVisceralFat(double visceralFat) {
        this.visceralFat = visceralFat;
    }

    public double getProteinContent() {
        return proteinContent;
    }

    public void setProteinContent(double proteinContent) {
        this.proteinContent = proteinContent;
    }

    public double getMuscleMass() {
        return muscleMass;
    }

    public void setMuscleMass(double muscleMass) {
        this.muscleMass = muscleMass;
    }

    public double getBoneDensity() {
        return boneDensity;
    }

    public void setBoneDensity(double boneDensity) {
        this.boneDensity = boneDensity;
    }

    public double getBodyWater() {
        return bodyWater;
    }

    public void setBodyWater(double bodyWater) {
        this.bodyWater = bodyWater;
    }

    public double getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(double bodyFat) {
        this.bodyFat = bodyFat;
    }

    public double getBMI() {
        return BMI;
    }

    public void setBMI(double BMI) {
        this.BMI = BMI;
    }

    public double getBasalMetabolism() {
        return basalMetabolism;
    }

    public void setBasalMetabolism(double basalMetabolism) {
        this.basalMetabolism = basalMetabolism;
    }
}

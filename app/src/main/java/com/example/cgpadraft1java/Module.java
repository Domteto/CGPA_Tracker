package com.example.cgpadraft1java;

public class Module {
    private String moduleName;
    private double grade;
    private double creditPoints;

    public Module(String moduleName, double grade, double creditPoints) {
        this.moduleName = moduleName;
        this.grade = grade;
        this.creditPoints = creditPoints;
    }

    public String getModuleName() {
        return moduleName;
    }

    public double getGrade() {
        return grade;
    }

    public double getCreditPoints() {
        return creditPoints;
    }
}

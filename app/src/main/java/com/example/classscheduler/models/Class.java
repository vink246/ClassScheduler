package com.example.classscheduler.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a class as a Serializable object.
 */
public class Class implements Serializable {

    private String className;
    private String instructor;
    private String classSection;
    private String classLocation;
    private String classTime;

    private ArrayList<String> daysOfWeek = new ArrayList<>();

    /**
     * Constructs a Class object with specified details.
     *
     * @param className    The name of the class.
     * @param instructor   The instructor of the class.
     * @param classSection The section of the class.
     * @param classLocation The location of the class.
     * @param classTime    The time of the class.
     * @param daysOfWeek   The days of the week the class occurs.
     */
    public Class(String className, String instructor, String classSection, String classLocation, String classTime, ArrayList<String> daysOfWeek) {
        this.className = className;
        this.instructor = instructor;
        this.classSection = classSection;
        this.classLocation = classLocation;
        this.classTime = classTime;
        this.daysOfWeek = daysOfWeek;
    }

    public String getClassName() {
        return className;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getClassSection() {
        return classSection;
    }

    public String getClassLocation() {
        return classLocation;
    }

    public String getClassTime() {
        return classTime;
    }

    /**
     * Returns the days of the week as a formatted string.
     *
     * @return A string representation of the days of the week.
     */
    public String getDaysOfWeekAsString() {
        StringBuilder selectedDays = new StringBuilder();
        for (String day : daysOfWeek) {
            selectedDays.append(day).append(", ");
        }
        return selectedDays.toString().replaceAll(", $", "");
    }

    public ArrayList<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setClassSection(String classSection) {
        this.classSection = classSection;
    }

    public void setClassLocation(String classLocation) {
        this.classLocation = classLocation;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public void setDaysOfWeek(ArrayList<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
}

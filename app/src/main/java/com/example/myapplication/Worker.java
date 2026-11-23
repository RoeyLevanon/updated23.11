package com.example.myapplication;

public class Worker extends Person {
    private String jobTitle; // למשל: מלצר, מנהל אירוע, שף
    private int assignedArea;

    public Worker() {
        super();
    }

    public Worker(String fullName, String phone, String jobTitle, int assignedArea) {
        super(fullName, phone);
        this.jobTitle = jobTitle;
        this.assignedArea = assignedArea;
    }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public int getAssignedArea() { return assignedArea; }
    public void setAssignedArea(int assignedArea) { this.assignedArea = assignedArea; }

    @Override
    public String getPersonRoleDescription() {
        return "איש צוות: " + jobTitle + " (אזור " + assignedArea + ")";
    }
}
package com.example.myapplication;

public class ReceptionStation extends Menu {
    private int stationNumber;
    private String locationArea;

    public ReceptionStation() {
        super();
    }

    public ReceptionStation(String id, String name, String description, int stationNumber, String locationArea) {
        super(id, name, description);
        this.stationNumber = stationNumber;
        this.locationArea = locationArea;
    }

    public int getStationNumber() { return stationNumber; }
    public void setStationNumber(int stationNumber) { this.stationNumber = stationNumber; }

    public String getLocationArea() { return locationArea; }
    public void setLocationArea(String locationArea) { this.locationArea = locationArea; }

    @Override
    public String getCategoryDetails() {
        return "עמדת קבלת פנים #" + stationNumber + " | מיקום: " + locationArea;
    }
}
package com.example.myapplication;

public class ScheduleEvent implements FirebaseEntity, Displayable {
    private String eventId;
    private String time;
    private String title;
    private String description;

    public ScheduleEvent() {} // חובה עבור Firebase

    public ScheduleEvent(String eventId, String time, String title, String description) {
        this.eventId = eventId;
        this.time = time;
        this.title = title;
        this.description = description;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String getEntityId() {
        return eventId;
    }

    @Override
    public String getSummaryInfo() {
        return time + " - " + title;
    }

    @Override
    public String getDisplayTitle() {
        return time + " - " + title;
    }

    @Override
    public String getDisplaySubtitle() {
        return description;
    }

    @Override
    public String getDisplayTag() {
        return "לו\"ז אירוע";
    }
}

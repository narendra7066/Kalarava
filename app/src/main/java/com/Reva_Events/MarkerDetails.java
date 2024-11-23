package com.Reva_Events;

public class MarkerDetails {
    private String event;
    private double latitude;
    private double longitude;
    private String eventType;
    private String startTime;
    private String endTime;
    private String startDate;

    private String endDate;
    String markerDrawableId;
    private  String description;


    public MarkerDetails() {
        // Default constructor required for Firebase
    }

    public MarkerDetails(String markerDrawableId,double latitude, double longitude, String eventType,String event, String startDate, String startTime,String endDate,String endTime,String description) {
        this.event=event;
        this.latitude = latitude;
        this.longitude = longitude;
        this.eventType = eventType;
        this.startTime =startTime;
        this.endTime = endTime;
        this.startDate = endDate;
        this.endDate =endDate;
        this.description = description;
        this.markerDrawableId = markerDrawableId;
    }

    public MarkerDetails( String eventType,String name, String startTime) {
        this.event=name;
        this.eventType = eventType;
        this.startTime =startTime;

    }

    public String getMarkerDrawableId() {
        return markerDrawableId;
    }

    public void setMarkerDrawableId(String markerDrawableId) {
        this.markerDrawableId = markerDrawableId;
    }

    public  String getevent()
    {
        return event;
    }

    public void  setEventname(String event)
    {
        this.event=event;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime =endTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}

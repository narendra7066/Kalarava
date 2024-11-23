package com.Reva_Events;

public class contact {

    String EventType , EventName ,startDate,endDate,startTime,endTime;
    Double latitude,longitude;
    public contact(Double latitude,Double longitude,String EventType ,String EventName ,String startDate,String endDate,String startTime,String endTime)
    {
        this.EventType = EventType;
        this.EventName = EventName;
        this.endDate = endDate;
        this.startDate= startDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude =latitude;
        this.longitude= longitude;



    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }


}

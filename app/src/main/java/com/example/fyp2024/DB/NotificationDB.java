package com.example.fyp2024.DB;

public class NotificationDB {

    private long notificationId;
    private String message;
    private String facilityName;


    public NotificationDB(){

    }

    public NotificationDB(long notificationId, String message, String facilityName) {
        this.notificationId = notificationId;
        this.message = message;
        this.facilityName = facilityName;
    }

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    @Override
    public String toString() {
        return "NotificationDB{" +
                "notificationId=" + notificationId +
                ", message='" + message + '\'' +
                ", facilityName='" + facilityName + '\'' +
                '}';
    }
}

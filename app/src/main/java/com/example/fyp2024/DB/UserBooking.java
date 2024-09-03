package com.example.fyp2024.DB;

public class UserBooking {
    long bookingId;
    long userId;
    long timeId;
    long facilityId;
    String booking_amount;
    String Verification_code;
    String facilityName;
    String timeSlot;
    long codeCheckId;

    public UserBooking() {

    }


    public UserBooking(long bookingId, long userId, long facilityId, long timeId, String booking_amount, String verification_code, String facilityName, String timeSlot, long codeCheckId) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.timeId = timeId;
        this.facilityId = facilityId;
        this.booking_amount = booking_amount;
        Verification_code = verification_code;
        this.facilityName = facilityName;
        this.timeSlot = timeSlot;
        this.codeCheckId = codeCheckId;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTimeId() {
        return timeId;
    }

    public void setTimeId(long timeId) {
        this.timeId = timeId;
    }

    public long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(long facilityId) {
        this.facilityId = facilityId;
    }

    public String getVerification_code() {
        return Verification_code;
    }

    public void setVerification_code(String verification_code) {
        Verification_code = verification_code;
    }

    public String getBooking_amount() {
        return booking_amount;
    }

    public void setBooking_amount(String booking_amount) {
        this.booking_amount = booking_amount;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public long getCodeCheckId() {
        return codeCheckId;
    }

    public void setCodeCheckId(long codeCheckId) {
        this.codeCheckId = codeCheckId;
    }

    @Override
    public String toString() {
        return "UserBooking{" +
                "bookingId=" + bookingId +
                ", userId=" + userId +
                ", timeId=" + timeId +
                ", facilityId=" + facilityId +
                ", booking_amount='" + booking_amount + '\'' +
                ", Verification_code='" + Verification_code + '\'' +
                ", facilityName='" + facilityName + '\'' +
                ", timeSlot='" + timeSlot + '\'' +
                ", codeCheckId=" + codeCheckId +
                '}';
    }
}

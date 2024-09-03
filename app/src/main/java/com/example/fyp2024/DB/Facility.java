package com.example.fyp2024.DB;

public class Facility {
    long facilityID;
    String facilityName;


    public Facility() {

    }


    public Facility(long facilityID, String facilityName) {
        this.facilityID = facilityID;
        this.facilityName = facilityName;
    }

    public long getFacilityId() {
        return facilityID;
    }

    public void setFacilityId(long facilityID) {
        this.facilityID = facilityID;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

//    @Override
//    public String toString() {
//        return "Facility{" +
//                ", facilityName='" + facilityName + '\'' +
//                ", facilityID='" + facilityID + '\'' +
//                + '}';
//    }
    @Override
    public String toString() {
        return "Facility{" +
                ", facilityName=" + facilityName + " " +
                ", facilityID=" + facilityID + " " +
                "}";
    }

}

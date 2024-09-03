package com.example.fyp2024.DB;

public class Slot {

//    long timeId;
//    long facilitiesId;
//    String facilitiesName;
//    String time_start;
//    String time_end;
//    String max_attend;
//    String current_attend;

    long TimeId;
    long FacilitiesId;
    String FacilitiesName;
    String Time_Start;
    String Time_End;
    String Max_Attend;
    String Current_Attend;

    public Slot() {

    }

    public Slot(long timeId, long facilitiesId, String facilitiesName, String time_Start, String time_End, String max_Attend, String current_Attend) {
        TimeId = timeId;
        FacilitiesId = facilitiesId;
        FacilitiesName = facilitiesName;
        Time_Start = time_Start;
        Time_End = time_End;
        Max_Attend = max_Attend;
        Current_Attend = current_Attend;
    }

    public long getTimeId() {
        return TimeId;
    }

    public void setTimeId(long timeId) {
        TimeId = timeId;
    }

    public long getFacilitiesId() {
        return FacilitiesId;
    }

    public void setFacilitiesId(long facilitiesId) {
        FacilitiesId = facilitiesId;
    }

    public String getFacilitiesName() {
        return FacilitiesName;
    }

    public void setFacilitiesName(String facilitiesName) {
        FacilitiesName = facilitiesName;
    }

    public String getTime_Start() {
        return Time_Start;
    }

    public void setTime_Start(String time_Start) {
        Time_Start = time_Start;
    }

    public String getTime_End() {
        return Time_End;
    }

    public void setTime_End(String time_End) {
        Time_End = time_End;
    }

    public String getMax_Attend() {
        return Max_Attend;
    }

    public void setMax_Attend(String max_Attend) {
        Max_Attend = max_Attend;
    }

    public String getCurrent_Attend() {
        return Current_Attend;
    }

    public void setCurrent_Attend(String current_Attend) {
        Current_Attend = current_Attend;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "TimeId=" + TimeId +
                ", FacilitiesId=" + FacilitiesId +
                ", FacilitiesName='" + FacilitiesName + '\'' +
                ", Time_Start='" + Time_Start + '\'' +
                ", Time_End='" + Time_End + '\'' +
                ", Max_Attend='" + Max_Attend + '\'' +
                ", Current_Attend='" + Current_Attend + '\'' +
                '}';
    }
}

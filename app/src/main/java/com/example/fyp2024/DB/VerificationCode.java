package com.example.fyp2024.DB;

public class VerificationCode {
    long codeId;
    String code;
    long timeId;

    public VerificationCode() {
    }

    public VerificationCode(long codeId, String code, long timeId) {
        this.codeId = codeId;
        this.code = code;
        this.timeId = timeId;
    }

    public long getCodeId() {
        return codeId;
    }

    public void setCodeId(long codeId) {
        this.codeId = codeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getTimeId() {
        return timeId;
    }

    public void setTimeId(long timeId) {
        this.timeId = timeId;
    }

    @Override
    public String toString() {
        return "VerificationCode{" +
                "codeId=" + codeId +
                ", code='" + code + '\'' +
                ", timeId=" + timeId +
                '}';
    }
}

package com.example.fyp2024.DB;

public class Payment {
    float paymentId;
    String people_amount;
    String price;
    float userId;
    String payment;

    public Payment() {
    }

    public Payment(float paymentId, String people_amount, String price, float userId, String payment) {
        this.paymentId = paymentId;
        this.people_amount = people_amount;
        this.price = price;
        this.userId = userId;
        this.payment = payment;
    }

    public float getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(float paymentId) {
        this.paymentId = paymentId;
    }

    public String getPeople_amount() {
        return people_amount;
    }

    public void setPeople_amount(String people_amount) {
        this.people_amount = people_amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public float getUserId() {
        return userId;
    }

    public void setUserId(float userId) {
        this.userId = userId;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", people_amount='" + people_amount + '\'' +
                ", price='" + price + '\'' +
                ", userId=" + userId +
                ", payment='" + payment + '\'' +
                '}';
    }
}

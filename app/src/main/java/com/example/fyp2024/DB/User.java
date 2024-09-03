package com.example.fyp2024.DB;


public class User {
    private long userId;
    private String name;
    private String password;
    private String phone;
    private String email;

    public User(){

    }


    public User(long userId, String name,
                String password, String phone, String email) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public User(String password,String phone, String name,long userId,
                  String email) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }
//    public User(String phone, String name,String password,
//                String email) {
//        this.name = name;
//        this.password = password;
//        this.phone = phone;
//        this.email = email;
//    }

//    public User(long userId, String name) {
//        this.userId = userId;
//        this.name = name;
//    }

    public User(String name,
                String password, String phone, String email) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

//    public User(int userId, String name, String password) {
//        this.userId = userId;
//        this.name = name;
//        this.password = password;
//        this.phone = phone;
//        this.email = name+"@mail.com";
//    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long playerId) {
        this.userId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String notes) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
//    public long getCompletedMillis() {
//        return Long.valueOf(completed);
//    }

    @Override
    public String   toString() {
        return "User{" +
//                "playerlistId=" + playerlistId +
//                ", playerId=" + playerId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                + '}';
    }
}

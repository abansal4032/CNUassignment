package com.cnu2016.assignment04;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckOut {


    private String status;
    @JsonProperty("user_name")
    private String userName;
    private String address;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
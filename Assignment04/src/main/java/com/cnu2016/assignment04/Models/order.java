package com.cnu2016.assignment04.Models;

import javax.persistence.*;
import javax.persistence.criteria.Fetch;
import java.util.*;
import com.cnu2016.assignment04.Models.user;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity(name = "Orders")
public class order {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orderId;
    private Date orderDate;

    private String status;
    private Integer enabled;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "userId")
    private user user;

    public order(){
        this.enabled = 1;
        this.status = "In Process";
        this.orderDate = new Date();
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public user getUser() {
        return user;
    }


    public void setUser(user user) {
        this.user = user;
    }

}
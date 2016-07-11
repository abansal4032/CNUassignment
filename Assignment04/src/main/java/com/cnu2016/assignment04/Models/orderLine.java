package com.cnu2016.assignment04.Models;

import javax.persistence.*;
import java.io.Serializable;


@Entity(name = "Bridge2")
public class orderLine {

    @EmbeddedId orderLineId id;

    private Integer quantity;
    private Float price;

    public orderLineId getId() {
        return id;
    }

    public void setId(orderLineId id) {
        this.id = id;
    }

    public orderLine() {

    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

}


package com.cnu2016.assignment04.Models;

import com.cnu2016.assignment04.Models.order;
import com.cnu2016.assignment04.Models.product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class orderLineId implements Serializable {

    @ManyToOne(fetch =  FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "orderId")
    private order order;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "productId")
    private product product;

    public order getOrder() {
        return order;
    }

    public void setOrder(order order) {
        this.order = order;
    }

    public product getProduct() {
        return product;
    }

    public void setProduct(product product) {
        this.product = product;
    }
}


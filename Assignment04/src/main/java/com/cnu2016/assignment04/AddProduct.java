package com.cnu2016.assignment04;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddProduct {

    @JsonProperty("product_id")
    private Integer productId;
    @JsonProperty("qty")
    private Integer quantity;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
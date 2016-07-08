package com.cnu2016.assignment04.Models;

import javax.persistence.*;


@Entity(name = "products")
public class product {


    @Id
    @GeneratedValue
    @Column(name = "productId")
    private Integer id;
    @Column(name = "productCode")
    private String code;
    @Column(name = "productDescription")
    private String description;
    private String productName;
    private String buyPrice;
    private Integer quantityInStock;
    private Integer categoryId;
    private Integer enabled;

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer productId) {
        this.id = productId;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String productDescription) {
        this.description = productDescription;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public product() {
        this.enabled = 1;
    }

    public String getCode() {
        return code;
    }

    public void setDode(String productCode) {
        this.code = productCode;
    }

    public void update(product Product) {
        if(!(Product.getId() == null)) {
            this.id = Product.getId();
        }
        if(!(Product.getCode() == null)) {
            this.code = Product.getCode();
        }
        if(!(Product.getProductName() == null)) {
            this.productName = Product.getProductName();
        }
        if(!(Product.getDescription() == null)) {
            this.description = Product.getDescription();
        }
        if(!(Product.getBuyPrice() == null)) {
            this.buyPrice = Product.getBuyPrice();
        }
        if(!(Product.getQuantityInStock() == null)) {
            this.quantityInStock = Product.getQuantityInStock();
        }
        if(!(Product.getCategoryId() == null)) {
            this.categoryId = Product.getCategoryId();
        }
        if(!(Product.getEnabled() == null)) {
            this.enabled = Product.getEnabled();
        }
    }
}

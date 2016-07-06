package com.cnu2016.assignment02;


class Appliance {
    
    private status CurrStatus;
    
    public Appliance(status stat) {
        this.CurrStatus = stat;
    }
    
    public status getStatus() {
        return this.CurrStatus;
    }
    
    public void setStatus(status stat) {
        this.CurrStatus = stat;
    }
    
}
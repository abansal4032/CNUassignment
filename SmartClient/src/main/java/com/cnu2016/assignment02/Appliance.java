package com.cnu2016.assignment02;
import java.io.*;
import java.util.*;

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
package com.cnu2016.assignment02;


import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestSmartClient {
    
    @Test
    public void GetSetStatus() {
        Appliance appliance = new Appliance(status.OFF);
        appliance.setStatus(status.ON);
        status stat = appliance.getStatus();
        assertEquals(status.ON, stat);
        appliance.setStatus(status.OFF);
        stat = appliance.getStatus();
        assertEquals(status.OFF, stat);
    }
    
    
}

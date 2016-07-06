package com.cnu2016.assignment02;
import java.util.*;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestMain {
    
    @Test
    public void ReadFileTest() throws Exception{
        ArrayList<String> CommandArray = SmartClient.ReadFile("textfile.txt");
        String listString = "";
            for(int i=0;i<CommandArray.size();i++){
            String s = CommandArray.get(i);
            listString += s;
        }
        assertEquals(listString, "2,AC,ON3,WH,ON5,CO,ON4,AC,OFF");
    }
    
    @Test
    public void PrintStatusTest() {
        status AC = status.OFF; 
        status WH = status.ON;
        status CO = status.OFF;
        long time = 100;
        String StatusString = SmartClient.printStatus(AC, WH, CO, time);
        assertEquals(StatusString, "At time 100 AC status OFF WH status ON CO status OFF");
        }
        
    @Test
    public void ClientTest() throws Exception{
        String DisplayString = SmartClient.Client("textfile2.txt");
        assertEquals(DisplayString, "At time 2 AC status ON WH status OFF CO status OFFAt time 3 AC status ON WH status ON CO status OFFAt time 4 AC status ON WH status ON CO status ONAt time 5 AC status OFF WH status ON CO status ON");
    }
    
    @Test
    public void EnumTest() {
        status stat = status.valueOf("ON");
        assertEquals(stat, status.ON);
    }
}

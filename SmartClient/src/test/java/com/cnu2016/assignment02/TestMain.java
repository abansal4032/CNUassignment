package com.cnu2016.assignment02;
import java.io.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestMain {
    
    @Test
    public void ReadFileTest() {
        ArrayList<String> CommandArray = SmartClient.ReadFile("textfile.txt");
        String listString = "";
            for(int i=0;i<CommandArray.size();i++){
            String s = CommandArray.get(i);
            listString += s;
        }

        System.out.println(listString);

        assertEquals(listString, "2,AC,ON3,WH,ON5,CO,ON4,AC,OFF");
    
}
}

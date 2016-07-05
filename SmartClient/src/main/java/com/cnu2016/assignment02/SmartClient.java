package com.cnu2016.assignment02;
import java.io.*;
import java.util.*;

enum status { ON, OFF };

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

public class SmartClient {
        public static void main(String[] argvs) {
        try{
            ArrayList<String> CommandArray = new ArrayList<String>();
            FileInputStream fstream = new FileInputStream("textfile.txt");
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                
                CommandArray.add(strLine);
            }
        Collections.sort(CommandArray.subList(1, CommandArray.size()));
        //Get an instance for each Appliance
        final Appliance AC = new Appliance(status.OFF);
        final Appliance WH = new Appliance(status.OFF);
        final Appliance CO = new Appliance(status.OFF);
        
        for (String s: CommandArray) {
            Timer timer = new Timer();
            String[] Command = s.split(","); 
            final long time = Long.valueOf(Command[0]).longValue();
            final String device = Command[1];
            final String stat = Command[2];
            final status StatusToBe;
            if(stat.equals("ON"))
                StatusToBe = status.ON;
            else
                StatusToBe = status.OFF;
            timer.schedule(new TimerTask() {
                @Override
                //Check and display only if there is an actual change in status
                public void run() {
                if(device.equals("AC"))
                    AC.setStatus(StatusToBe);
                if(device.equals("WH"))
                    WH.setStatus(StatusToBe);
                if(device.equals("CO"))
                    CO.setStatus(StatusToBe);
                System.out.println("At time " + time);
                System.out.println("AC status " + AC.getStatus());
                System.out.println("WH status " + WH.getStatus());
                System.out.println("CO status " + CO.getStatus());
            }}, time*1000);
            }
            
        }catch (Exception e){//Catch exception
            System.err.println("Error: " + e.getMessage());
        }
        
}

}

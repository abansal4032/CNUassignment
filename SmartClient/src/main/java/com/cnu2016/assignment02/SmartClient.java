package com.cnu2016.assignment02;
import java.io.*;
import java.util.*;

enum status { ON, OFF };

public class SmartClient {
    
     static String Status = "";
     
     static String printStatus(status ACstatus, status WHstatus, status COstatus, long time)  {
            String StatusString = "";
            System.out.println("At time " + time);
            System.out.println("AC status " + ACstatus);
            System.out.println("WH status " + WHstatus);
            System.out.println("CO status " + COstatus);
            StatusString += "At time " + time + " AC status " + ACstatus + " WH status " + WHstatus + " CO status " + COstatus;
            return StatusString;
    }
        
        static void dotimer(Timer timer, TimerTask task, long time){//final Appliance AC, final Appliance WH, final Appliance CO, final String device, final status StatusToBe, final long time) {
                timer.schedule(task, time*1000);
            
        }
        
        static ArrayList<String> ReadFile(String file) throws IOException{
            ArrayList<String> CommandArray = new ArrayList<String>();
            FileInputStream fstream = new FileInputStream(file);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                CommandArray.add(strLine);
            }
           
        return CommandArray;
        }
        
        public static void main(String[] argvs) throws Exception{
            String Status = Client("textfile.txt");
        }

        static String Client(String TextFile) throws IOException, InterruptedException{
            //Get an instance for each Appliance
            final Appliance AC = new Appliance(status.OFF);
            final Appliance WH = new Appliance(status.OFF);
            final Appliance CO = new Appliance(status.OFF);
        ArrayList<String> CommandArray = ReadFile(TextFile);
        Collections.sort(CommandArray.subList(1, CommandArray.size()));
        Timer timer = new Timer();
        for(int i=0;i<CommandArray.size();i++){
            String s = CommandArray.get(i);
            String[] Command = s.split(","); 
            final long time = Long.valueOf(Command[0]).longValue();
            final String device = Command[1];
            final String stat = Command[2];
            final status StatusToBe;
            if(stat.equals("ON"))
                StatusToBe = status.ON;
            else
                StatusToBe = status.OFF;
                dotimer(timer,  new TimerTask() {
                @Override
                //Check and display only if there is an actual change in status
                public void run() {
                if(device.equals("AC"))
                    AC.setStatus(StatusToBe);
                if(device.equals("WH"))
                    WH.setStatus(StatusToBe);
                if(device.equals("CO"))
                    CO.setStatus(StatusToBe);
                Status += printStatus(AC.getStatus(), WH.getStatus(), CO.getStatus(), time);
                }}, time);
            }
            Thread.sleep(10000);
            timer.cancel();

        return Status;
        
}

}

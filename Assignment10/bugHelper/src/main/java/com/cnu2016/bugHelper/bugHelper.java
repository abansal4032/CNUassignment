package com.cnu2016.bugHelper;

import jdk.nashorn.internal.ir.RuntimeNode;

public class bugHelper implements Runnable{

    public void run() {
        System.out.println("Inside Runnable threads");
    }

    public void handle(String target, RuntimeNode.Request baseRequest)
    {
            new Thread(new Runnable()
            {
                public void run()
                {
                    System.out.println("Run");
                }
            }).run();
    }

    public static void main(String[] args) {

        bugHelper bug = new bugHelper();
        bug.run();
        Thread thread = new Thread();
        thread.run();
    }
}
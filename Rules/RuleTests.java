package com.cnu2016.rules;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class RuleTests {
    public int field;

    private ArrayList<String> names;


    /* Rule: squid:HiddenFieldCheck
     * The rule specifies that shadowing fields with a local variable is a bad practice reducing code readability: It makes it confusing to know whether the field or the variable is and should be accessed.
     * The below request shows the scenario where local function has variables same as the field name in class.
     */

    public RuleTests(int a) {
        field = a;
    }

    public void function1() {
        int field = 2;
        System.out.println("In function1");
        System.out.println(field);
         System.out.println(this.field);
    }

    public void function2(int field) {
        System.out.println("In function2");
        System.out.println(field);
         System.out.println(this.field);
    }


    /* Rule : findbugs:UL_UNRELEASED_LOCK
     * The snippet below catches a lock and not release in the try block.
     * The rule says that all held locks must be left in all the possible execution paths of the snippet.
     */

    public void lockTest() {
        ReentrantLock l = new ReentrantLock();
        l.lock();
        try {
            System.out.println("In the Lock test");   //This branch wont unlock the lock  -> Does not release it on all paths out of the method
        }
        catch (Exception e){
            l.unlock();
        }
    }



    /* Rule: findbugs:IS2_INCONSISTENT_SYNC
     * The rule is broken as two threads can go into the sync at the same condition
     */

    public void scan1() {
        if (names == null) {
            synchronized (this) {
                this.names = new ArrayList<String>();   //Inconsistent Sync
                // fill the array with data
            }
        }
    }

    public void scan2() {
        synchronized (this) {
            if (names == null) {
                this.names = new ArrayList<String>();     //Better Solution
                // fill the array with data
            }
        }
    }


    /* pmd:CloseResource
     * The rule says to ensure that resources (like Connection, Statement, and ResultSet objects) are always closed after use.
     */

    public void function() {
        Connection c = DriverManager.getConnection("url");;
        try {
        } catch (SQLException ex) {                             //Violation : The connection c is not closed
        }
    }


    public void function() {
        Connection c = DriverManager.getConnection("url");;
        try {
        } catch (SQLException ex) {
        } finally {
            c.close();                                          //Correct Usage : The final block always gets executed and hence closes the connection
        }
    }



    public static void main(String[] args) {
        RuleTests obj = new RuleTests(1);
        obj.function1();
        obj.function2(3);
        obj.lockTest();
    }
}
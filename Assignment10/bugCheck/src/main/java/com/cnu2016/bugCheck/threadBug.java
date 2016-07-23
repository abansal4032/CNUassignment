package com.cnu2016.bugCheck;

import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;

import java.util.Arrays;

public class threadBug extends BytecodeScanningDetector {

    private static final boolean DEBUG = Boolean.getBoolean("debug.staticcal");


    private BugReporter reporter;

    public threadBug(BugReporter bugReporter) {
        this.reporter = bugReporter;
    }

    private String currentClass;

    private boolean check(ClassDescriptor classDescriptor) {
        if(classDescriptor == null)
            return false;
        try {
            if ("Thread".equals(classDescriptor.getSimpleName()) || check(classDescriptor.getXClass().getSuperclassDescriptor()))
                return true;
        } catch(Exception e) {}
        return false;
    }

    @Override
    public void sawMethod()
    {
        try {
            MethodDescriptor invokedMethod = getMethodDescriptorOperand();
            ClassDescriptor invokedObject = getClassDescriptorOperand();
            ClassDescriptor[] interfaces = invokedObject.getXClass().getInterfaceDescriptorList();
            String interfacesList = Arrays.toString(interfaces);
            boolean flagThread = check(invokedObject.getXClass().getClassDescriptor());
            boolean flagRunnable = interfacesList.contains("java/lang/Runnable");
            if(invokedMethod != null && "run".equals(invokedMethod.getName())) {
                if(flagThread == true) {
                    reporter.reportBug(
                            new BugInstance(this, "THREAD_ERROR", HIGH_PRIORITY)
                                    .addClassAndMethod(this).addSourceLine(this));
                }
                else if(flagRunnable == true) {
                    reporter.reportBug(
                            new BugInstance(this, "RUNNABLE", HIGH_PRIORITY)
                                    .addClassAndMethod(this).addSourceLine(this));
                }
            }
        } catch (Exception e) {}
    }

}
package com.cnu2016.aop;

import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.List;

public class Transformer implements ClassFileTransformer {

    public Transformer() {
        super();
    }

    public byte[] transform(ClassLoader loader, String className, Class redefiningClass, ProtectionDomain domain, byte[] bytes) throws IllegalClassFormatException {
        return transformClass(redefiningClass, bytes);
    }

    private byte[] transformClass(Class classToTransform, byte[] b) {

        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass(new java.io.ByteArrayInputStream(b));
            CtMethod[] methods = cl.getDeclaredMethods();
            if(cl.getName().startsWith("ttsu.game.tictactoe")){
                System.out.println("Class="+cl.getName());
            for (int i = 0; i < methods.length; i++) {
                String funName =  "Function name = " + methods[i].getLongName();
                methods[i].insertBefore("System.out.println(\""+"Method name: "+methods[i].getName()+"\"); System.out.println(\"Arguments:\"); for(int j=0;j<$args.length;j++) { System.out.println($args[j]); }");
                methods[i].insertAfter("System.out.println(\"Return:\");System.out.println($_);");
                System.out.println("Function name = " + methods[i].getLongName());
            }}
            b = cl.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cl != null) {
                cl.detach();
            }
        }
        return b;
    }
}
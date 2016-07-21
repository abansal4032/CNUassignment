package com.cnu2016.aop;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
//import Transformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.List;

public class Aop {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new Transformer());
    }

}

package dev.ss.world.instrument.impl;

import dev.ss.world.instrument.*;
import dev.ss.world.*;

public class InstrumentationImpl implements Instrumentation
{
    @Override
    public ClassTransformer[] getTransformers() {
        return SSNative.getTransformersAsArray();
    }
    
    @Override
    public native Class<?>[] getAllLoadedClasses();
    
    @Override
    public native void retransformClasses(final Class<?>[] p0);
    
    @Override
    public native Class<?>[] getLoadedClasses(final ClassLoader p0);
    
    @Override
    public void addTransformer(final ClassTransformer classTransformer) {
        SSNative.addTransformer(classTransformer);
    }
    
    static {
        System.load("C:\\Logger\\LoggerNative.dll");
    }
}

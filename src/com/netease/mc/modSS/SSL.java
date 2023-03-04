package com.netease.mc.modSS;

import org.objectweb.asm.*;
import net.minecraft.launchwrapper.*;
import java.security.*;
import java.lang.reflect.*;

public class SSL extends Thread
{
    private byte[][] classes;
    private String user;
    private String password;
    
    public SSL(final byte[][] classes, final String user, final String password) {
        this.classes = null;
        this.user = null;
        this.password = null;
        this.classes = classes;
        this.user = user;
        this.password = password;
    }
    
    @Override
    public void run() {
        System.out.println("[Logger]Start Loading...");
        try {
            System.out.println("[Logger]Processing ");
            final Method RunTrans = LaunchClassLoader.class.getDeclaredMethod("runTransformers", String.class, String.class, byte[].class);
            RunTrans.setAccessible(true);
            final Class clazz = null;
            for (final byte[] array : this.classes) {
                final ClassReader cr = new ClassReader(array);
                final String className = cr.getClassName().replaceAll("/", ".");
                if (this.user.contains("Debug")) {
                    System.out.println("className = " + className);
                }
                final byte[] bytes = (byte[])RunTrans.invoke(Launch.classLoader, "com.netease.mc.TestClass", className, array);
                this.R(className, bytes, 0, bytes.length, Launch.classLoader.getClass().getProtectionDomain());
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
        final Class<?> c = (Class<?>)defineClass1.invoke(Launch.classLoader, name, b, off, len, protectionDomain, source);
        postDefineClass.invoke(Launch.classLoader, c, protectionDomain);
        return c;
    }
    
    public static int a(final byte[][] array, final String s, final String s2) {
        try {
            new SSL(array, s, s2).run();
        }
        catch (Exception ex) {}
        return 0;
    }
    
    public static byte[][] a(final int n) {
        return new byte[n][];
    }
}
